package edu.austral.ingsis.clifford;

import java.util.List;

public class PathResolver {

    public static Result resolvePath(Directory currentDirectory, Directory rootDirectory, String path) {
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
        Directory target = findDirectory(root, path);
        if (target != null) {
            return new Result.Success<>(target);
        } else {
            return new Result.Error("Directory not found: " + path);
        }
    }

    private static Result resolveRelativePath(Directory currentDirectory, String path) {
        Directory target = findDirectory(currentDirectory, currentDirectory.getPath() + "/" + path);
        if (target != null) {
            return new Result.Success<>(target);
        } else {
            return new Result.Error("Directory not found: " + path);
        }
    }

    private static Directory findDirectory(Directory start, String path) {
        if (start.getPath().equals(path)) {
            return start;
        }

        if (path.startsWith(start.getPath())) {
            for (FileSystemObjects child : start.getChildren()) {
                if (child.isDirectory()) {
                    Directory found = findDirectory((Directory) child, path);
                    if (found != null) {
                        return found;
                    }
                }
            }
        }
        return null;
    }
}