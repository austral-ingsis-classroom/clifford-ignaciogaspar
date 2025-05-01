package edu.austral.ingsis.clifford.command;

import edu.austral.ingsis.clifford.Directory;
import edu.austral.ingsis.clifford.FileSystem;
import edu.austral.ingsis.clifford.FileSystemObjects;
import edu.austral.ingsis.clifford.Result;

public class Rm implements Command {
  private final String path;
  private final boolean recursive;

  public Rm(String path, boolean recursive) {
    this.path = path;
    this.recursive = recursive;
  }

  @Override
  public Result<FileSystem> execute(FileSystem fileSystem) {
    Directory currentDirectory = fileSystem.getCurrentDirectory();
    FileSystemObjects target = findFileSystemObject(currentDirectory, path);
    if (target == null) {
      return new Result.Error<>("file or directory not found: " + path);
    }
    if (target.isDirectory() && !recursive) {
      return new Result.Error<>("cannot remove '" + path + "', is a directory");
    }
    Directory updatedCurrentDirectory = removeFileSystemObject(currentDirectory, target);
    return new Result.Success<>(new FileSystem(fileSystem.getRoot(), updatedCurrentDirectory),"'" + path + "' removed");
  }

  private FileSystemObjects findFileSystemObject(Directory directory, String path) {
    for (FileSystemObjects child : directory.getChildren()) {
      if (child.getName().equals(path)) { return child;}
    }
    return null;
  }

  private Directory removeFileSystemObject(Directory directory, FileSystemObjects target) {
    return directory.withoutChild(target);
  }
}