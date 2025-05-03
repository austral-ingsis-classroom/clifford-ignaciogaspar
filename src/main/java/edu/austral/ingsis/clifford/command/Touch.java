package edu.austral.ingsis.clifford.command;

import edu.austral.ingsis.clifford.Directory;
import edu.austral.ingsis.clifford.File;
import edu.austral.ingsis.clifford.FileSystem;
import edu.austral.ingsis.clifford.FileSystemObjects;
import edu.austral.ingsis.clifford.ImmutableTreeUpdater;
import edu.austral.ingsis.clifford.PathResolver; // Importa PathResolver
import edu.austral.ingsis.clifford.Result;

public class Touch implements Command {
  private final String fileName;

  public Touch(String fileName) {
    if (fileName == null
        || fileName.trim().isEmpty()
        || fileName.contains("/")
        || fileName.contains(" ")) {
      this.fileName = null;
    } else {
      this.fileName = fileName.trim();
    }
  }

  @Override
  public Result<FileSystem> execute(FileSystem fileSystem) {
    if (this.fileName == null) {
      return new Result.Error<>("invalid file name");
    }
    Directory parent = fileSystem.getCurrentDirectory();
    if (alreadyExist(parent, fileName)) {
      return new Result.Error<>("File already exists");
    }
    Result<Directory> addResult = addFileSystemObject(parent, this.fileName, fileSystem.getRoot());
    switch (addResult) {
      case Result.Success<Directory> done -> {
        return handleSuccessfulUpdate(fileSystem, done.getValue(), this.fileName);
      }
      case Result.Error<Directory> addError -> {
        return new Result.Error<>(addError.getMessage());
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
            new FileSystem(newRoot, success.getValue()), "'" + name + "' file created");
      }
      case Result.Error<Directory> error -> {
        return new Result.Error<>(error.getMessage());
      }
    }
  }

  private boolean alreadyExist(Directory parent, String fileName) {
    for (FileSystemObjects child : parent.children()) {
      if (!child.isDirectory() && child.name().equals(fileName)) {
        return true;
      }
    }
    return false;
  }

  private Result<Directory> addFileSystemObject(
      Directory parentInRoot, String fileName, Directory currentRoot) {
    String newPath =
        parentInRoot.path().equals("/") ? "/" + fileName : parentInRoot.path() + "/" + fileName;
    File newFileToAdd = new File(fileName, newPath);
    return ImmutableTreeUpdater.addChild(currentRoot, parentInRoot, newFileToAdd);
  }
}
