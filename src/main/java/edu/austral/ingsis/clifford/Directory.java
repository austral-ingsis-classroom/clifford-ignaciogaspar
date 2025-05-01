package edu.austral.ingsis.clifford;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Directory implements FileSystemObjects {

  private final String path;
  private final String name;
  private final List<FileSystemObjects> children;

  public Directory(String name, String path, List<FileSystemObjects> children) {
    this.name = name;
    this.path = path;
    this.children = new ArrayList<>(children); // Defensive copy
  }

  @Override
  public String getPath() {
    return path;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean isDirectory() {
    return true;
  }

  public List<FileSystemObjects> getChildren() {
    return children;
  }

  public Directory withChild(FileSystemObjects newChild) {
    List<FileSystemObjects> newChildren = new ArrayList<>(this.children);
    newChildren.add(newChild);
    return new Directory(this.name, this.path, newChildren);
  }

  public Directory withName(String newName) {
    String newPath = this.path.substring(0, this.path.lastIndexOf('/')) + "/" + newName;
    return new Directory(newName, newPath, this.children);
  }

  public Directory withoutChild(FileSystemObjects childToRemove) {
    List<FileSystemObjects> newChildren = new ArrayList<>(this.children);
    newChildren.remove(childToRemove);
    return new Directory(this.name, this.path, newChildren);
  }
}