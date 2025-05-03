package edu.austral.ingsis.clifford.command;

import edu.austral.ingsis.clifford.Directory;
import edu.austral.ingsis.clifford.FileSystem;
import edu.austral.ingsis.clifford.FileSystemObjects;
import edu.austral.ingsis.clifford.ImmutableTreeUpdater;
import edu.austral.ingsis.clifford.PathResolver; // Importa PathResolver
import edu.austral.ingsis.clifford.Result;

public class Rm implements Command {
  private final String targetName;
  private final boolean recursive;

  public Rm(String targetName, boolean recursive) {
    if (targetName == null || targetName.trim().isEmpty() || targetName.contains("/")) {
      this.targetName = null;
    } else {
      this.targetName = targetName.trim();
    }
    this.recursive = recursive;
  }

  @Override
  public Result<FileSystem> execute(FileSystem fileSystem) {
    Directory parent = fileSystem.getCurrentDirectory();
    if (this.targetName == null) {
      return new Result.Error<>("rm: invalid name");
    }
    Result<FileSystemObjects> searchResult = searchFileSystemObject(parent, targetName);
    switch (searchResult) {
      case Result.Success<FileSystemObjects> success -> {
        FileSystemObjects target = success.getValue();
        Result<Directory> removeResult =
            removeFileSystemObject(parent, target, recursive, fileSystem);
        switch (removeResult) {
          case Result.Success<Directory> done -> {
            return handleSuccessfulUpdate(fileSystem, done.getValue(), this.targetName);
          }
          case Result.Error<Directory> error -> {
            return new Result.Error<>(error.getMessage());
          }
        }
      }
      case Result.Error<FileSystemObjects> error -> {
        return new Result.Error<>(error.getMessage());
      }
    }
  }

  private Result<FileSystem> handleSuccessfulUpdate(
      FileSystem fileSystem, Directory newRoot, String name) {
    Result<Directory> resolvedCurrentDir =
        PathResolver.resolvePath(
            fileSystem.getCurrentDirectory(), newRoot, fileSystem.getCurrentDirectory().path());
    switch (resolvedCurrentDir) {
      case Result.Success<Directory> success -> {
        return new Result.Success<>(
            new FileSystem(newRoot, success.getValue()), "'" + name + "' removed");
      }
      case Result.Error<Directory> error -> {
        return new Result.Error<>(error.getMessage());
      }
    }
  }

  private Result<FileSystemObjects> searchFileSystemObject(Directory parent, String name) {
    for (FileSystemObjects child : parent.children()) {
      if (child.name().equals(name)) {
        return new Result.Success<>(child, "target found");
      }
    }
    return new Result.Error<>("file or directory not found: " + name);
  }

  private Result<Directory> removeFileSystemObject(
      Directory currentDirectory,
      FileSystemObjects target,
      boolean recursive,
      FileSystem fileSystem) {
    if (target.isDirectory() && !recursive) {
      return new Result.Error<>("cannot remove '" + target.name() + "', is a directory");
    }
    if (!target.isDirectory() && recursive) {
      return new Result.Error<>(
          "cannot remove '" + target.name() + "', with recursive, it is a file");
    }
    return ImmutableTreeUpdater.removeChild(fileSystem.getRoot(), currentDirectory, target);
  }
}
