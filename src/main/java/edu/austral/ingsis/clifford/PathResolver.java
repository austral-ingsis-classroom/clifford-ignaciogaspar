package edu.austral.ingsis.clifford;

public class PathResolver {

  public static Result<Directory> resolvePath(
          Directory currentDirectory, Directory rootDirectory, String path) {
    return switch (path) {
      case "." -> new Result.Success<>(currentDirectory, "Current directory");
      case ".." -> new Result.Success<>(getParentDirectory(rootDirectory, currentDirectory), "Parent directory");
      default -> {
        if (path.startsWith("/")) {
          yield resolveAbsolutePath(rootDirectory, path);
        } else {
          yield resolveRelativePath(currentDirectory, path);
        }
      }
    };
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

  private static Result<Directory> resolveAbsolutePath(Directory root, String path) {
    Result<Directory> result = findDirectory(root, path, true);
    if (result instanceof Result.Success) {
      return result;
    } else {
      return new Result.Error<>("'" + path + "' directory does not exist");
    }
  }

  private static Result<Directory> resolveRelativePath(Directory currentDirectory, String path) {
    Result<Directory> result =
        findDirectory(currentDirectory, path, false);
    if (result instanceof Result.Success) {
      return result;
    } else {
      return new Result.Error<>("'" + path + "' directory does not exist");
    }
  }

  private static Result<Directory> findDirectory(Directory start, String path, boolean isAbsolutePath) {
    String targetPath = isAbsolutePath ? path :start.getPath().equals("/") ? "/" + path : start.getPath() + "/" + path;

    if (start.getPath().equals(targetPath)) {
      return new Result.Success<>(start, "");
    }

    if (targetPath.startsWith(start.getPath())) {
      for (FileSystemObjects child : start.getChildren()) {
        if (child.isDirectory()) {
          Result<Directory> found = findDirectory( (Directory) child, targetPath, true);
          if (found instanceof Result.Success) {
            return found;
          }
        }
      }
    }
    return new Result.Error<>("'" + path + "' directory does not exist");
  }
}
