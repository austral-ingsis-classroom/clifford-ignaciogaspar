package edu.austral.ingsis.clifford.command;

import edu.austral.ingsis.clifford.Directory;
import edu.austral.ingsis.clifford.FileSystem;
import edu.austral.ingsis.clifford.FileSystemObjects;
import edu.austral.ingsis.clifford.ImmutableTreeUpdater;
import edu.austral.ingsis.clifford.PathResolver;
import edu.austral.ingsis.clifford.Result;
import java.util.ArrayList;

public class Mkdir implements Command {
  private final String directoryName;

  public Mkdir(String directoryName) {
    this.directoryName = directoryName;
  }

  @Override
  public Result<FileSystem> execute(FileSystem fileSystem) {
    Directory parent = fileSystem.getCurrentDirectory();
    if (directoryName.contains("/")
        || directoryName.contains(" ")
        || alreadyExist(parent, directoryName)) {
      return new Result.Error<>("invalid directory name: " + directoryName);
    }
    String newPath =
        parent.path().equals("/")
            ? "/" + this.directoryName
            : parent.path() + "/" + this.directoryName;
    Directory newDirectory = new Directory(this.directoryName, newPath, new ArrayList<>());

    Result<Directory> updateResult =
        ImmutableTreeUpdater.addChild(fileSystem.getRoot(), parent, newDirectory);
    switch (updateResult) {
      case Result.Error<Directory> error -> {
        return new Result.Error<>(error.getMessage());
      }
      case Result.Success<Directory> success -> {
        return handleSuccessfulUpdate(fileSystem, success.getValue(), this.directoryName);
      }
    }
  }

  private Result<FileSystem> handleSuccessfulUpdate(
      FileSystem fileSystem, Directory newRoot, String dirName) {
    Result<Directory> resolvedCurrentDir =
        PathResolver.resolvePath(
            fileSystem.getCurrentDirectory(), newRoot, fileSystem.getCurrentDirectory().path());
    switch (resolvedCurrentDir) {
      case Result.Success<Directory> success -> {
        return new Result.Success<>(
            new FileSystem(newRoot, success.getValue()), "'" + dirName + "' directory created");
      }
      case Result.Error<Directory> error -> {
        return new Result.Error<>(error.getMessage());
      }
    }
  }

  private boolean alreadyExist(Directory parent, String directoryName) {
    for (FileSystemObjects child : parent.children()) {
      if (child.isDirectory() && child.name().equals(directoryName)) {
        return true;
      }
    }
    return false;
  }
}
