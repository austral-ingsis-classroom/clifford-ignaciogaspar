package edu.austral.ingsis.clifford; // O tu paquete correcto

import java.util.ArrayList;
import java.util.List;

public class ImmutableTreeUpdater {

  public static Result<Directory> addChild(
      Directory root, Directory targetParent, FileSystemObjects newChild) {
    if (root == null || targetParent == null || newChild == null) {
      return new Result.Error<>("invalid arguments provided to addChild");
    }
    return updateTreeRecursive(root, targetParent, newChild, true); // true para añadir
  }

  public static Result<Directory> removeChild(
      Directory root, Directory targetParent, FileSystemObjects childToRemove) {
    if (root == null || targetParent == null || childToRemove == null) {
      return new Result.Error<>("invalid arguments provided to removeChild");
    }
    return updateTreeRecursive(root, targetParent, childToRemove, false); // false para eliminar
  }

  /**
   * @param currentNode El nodo actual en la recursión.
   * @param targetParent El padre lógico a modificar.
   * @param childToModify El hijo a añadir/eliminar.
   * @param add true para añadir, false para eliminar.
   */
  private static Result<Directory> updateTreeRecursive(
      Directory currentNode, Directory targetParent, FileSystemObjects childToModify, boolean add) {
    if (currentNode.equals(targetParent)) {
      Directory updatedNode;
      if (add) {
        updatedNode = currentNode.withChild(childToModify);
      } else {
        if (currentNode.children().stream().noneMatch(c -> c.equals(childToModify))) {
          return new Result.Error<>(
              "Child to remove does not exist in target parent '" + targetParent.name() + "'");
        }
        updatedNode = currentNode.withoutChild(childToModify);
      }
      return new Result.Success<>(updatedNode, "Parent node updated successfully");
    }
    List<FileSystemObjects> newChildrenList = new ArrayList<>();
    boolean subtreeUpdated = false;
    Result<Directory> recursiveResult;

    for (FileSystemObjects child : currentNode.children()) {
      if (child.isDirectory()) {
        Directory currentChildDir = (Directory) child;
        recursiveResult = updateTreeRecursive(currentChildDir, targetParent, childToModify, add);
        if (recursiveResult instanceof Result.Success<Directory> successResult) {
          newChildrenList.add(successResult.getValue());
          subtreeUpdated = true;
        } else if (recursiveResult instanceof Result.Error) {
          newChildrenList.add(currentChildDir);
        } else {
          newChildrenList.add(currentChildDir);
        }
      } else {
        newChildrenList.add(child);
      }
    }
    if (subtreeUpdated) {
      Directory newNode = new Directory(currentNode.name(), currentNode.path(), newChildrenList);
      return new Result.Success<>(newNode, "Node updated due to child update");
    } else {
      return new Result.Error<>(
          "Target parent not found in subtree of '" + currentNode.name() + "'");
    }
  }
}
