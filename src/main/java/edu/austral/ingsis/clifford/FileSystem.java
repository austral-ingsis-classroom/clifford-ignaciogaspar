package edu.austral.ingsis.clifford;

import java.util.ArrayList;

public class FileSystem {
  private final Directory root;
  private final Directory currentDirectory;

  public FileSystem() {
    this(new Directory("/", "/", new ArrayList<>()), new Directory("/", "/", new ArrayList<>()));
  }

  public FileSystem(Directory root, Directory currentDirectory) {
    this.root = root;
    this.currentDirectory = currentDirectory;
  }

  public Directory getRoot() {
    return root;
  }

  public Directory getCurrentDirectory() {
    return currentDirectory;
  }

  public FileSystem withCurrentDirectory(Directory currentDirectory) {
    return new FileSystem(this.root, currentDirectory);
  }
}
