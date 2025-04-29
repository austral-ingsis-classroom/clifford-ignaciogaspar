package edu.austral.ingsis.clifford.command;

import edu.austral.ingsis.clifford.Directory;
import edu.austral.ingsis.clifford.FileSystem;
import edu.austral.ingsis.clifford.FileSystemObjects;
import edu.austral.ingsis.clifford.Result;
import java.util.List;
import java.util.ListIterator;

public class Rm implements Command {
  private String path;
  private boolean recursive;

  public Rm(String path, boolean recursive) {
    this.path = path;
    this.recursive = recursive;
  }

  @Override
  public Result execute(FileSystem fileSystem) {
    Directory currentDirectory = fileSystem.getCurrentDirectory();
    FileSystemObjects target = findFileSystemObject(currentDirectory, path);
    if (target == null) {
      return new Result.Error("file or directory not found: " + path);
    }
    if (target.isDirectory() && !recursive) {
      return new Result.Error("cannot remove '" + path + "', is a directory");
    }
    removeFileSystemObject(currentDirectory, target);
    return new Result.Success<>("'" + path + "' removed");
  }

  private FileSystemObjects findFileSystemObject(Directory directory, String path) {
    for (FileSystemObjects child : directory.getChildren()) {
      if (child.getName().equals(path)) {
        return child;
      }
    }
    return null;
  }

  private void removeFileSystemObject(Directory directory, FileSystemObjects target) {
    List<FileSystemObjects> children = directory.getChildren();
    ListIterator<FileSystemObjects> iterator = children.listIterator();
    while (iterator.hasNext()) {
      FileSystemObjects child = iterator.next();
      if (child == target) {
        iterator.remove();
        return;
      }
    }
  }
}
