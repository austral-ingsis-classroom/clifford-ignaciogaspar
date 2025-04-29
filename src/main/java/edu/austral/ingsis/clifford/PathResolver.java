package edu.austral.ingsis.clifford;

public class PathResolver {

  public static Result resolvePath(
      Directory currentDirectory, Directory rootDirectory, String path) {
    if (path.equals(".")) {
      return new Result.Success<>(currentDirectory);
    } else if (path.equals("..")) {
      return new Result.Success<>(getParentDirectory(rootDirectory, currentDirectory));
    } else if (path.startsWith("/")) {
      return resolveAbsolutePath(rootDirectory, path);
    } else {
      return resolveRelativePath(currentDirectory, path);
    }
  }

  private static Directory getParentDirectory(Directory root, Directory current) {
    if (current == root) {
      return root;
    }
    return findParent(root, current);
  }

  private static Directory findParent(Directory current, Directory target) {
    for (FileSystemObjects child : current.getChildren()) {
      if (child.isDirectory()) {
        if (child == target) {
          return current;
        }
        Directory found = findParent((Directory) child, target);
        if (found != null) {
          return found;
        }
      }
    }
    return current;
  }

  private static Result resolveAbsolutePath(Directory root, String path) {
    Result result = findDirectory(root, path, true);
    if (result instanceof Result.Success) {
      return result;
    } else {
      return new Result.Error("'" + path + "' directory does not exist");
    }
  }

  private static Result resolveRelativePath(Directory currentDirectory, String path) {
    Result result =
        findDirectory(currentDirectory, path, false); // Pass false to indicate relative path
    if (result instanceof Result.Success) {
      return result;
    } else {
      return new Result.Error("'" + path + "' directory does not exist");
    }
  }

  private static Result findDirectory(Directory start, String path, boolean isAbsolutePath) {
    String targetPath =
        isAbsolutePath
            ? path
            : start.getPath().equals("/") ? "/" + path : start.getPath() + "/" + path;

    if (start.getPath().equals(targetPath)) {
      return new Result.Success<>(start);
    }

    if (targetPath.startsWith(start.getPath())) {
      for (FileSystemObjects child : start.getChildren()) {
        if (child.isDirectory()) {
          Result found =
              findDirectory(
                  (Directory) child, targetPath, true); // Always use absolute path for recursion
          if (found instanceof Result.Success) {
            return found;
          }
        }
      }
    }
    return new Result.Error("'" + path + "' directory does not exist");
  }
}
