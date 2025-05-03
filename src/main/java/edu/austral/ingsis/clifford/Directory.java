package edu.austral.ingsis.clifford;

import java.util.ArrayList;
import java.util.List;

public record Directory(String name, String path, List<FileSystemObjects> children)
    implements FileSystemObjects {

  public Directory(String name, String path, List<FileSystemObjects> children) {
    this.name = name;
    this.path = path;
    this.children = new ArrayList<>(children);
  }

  @Override
  public boolean isDirectory() {
    return true;
  }

  public Directory withChild(FileSystemObjects newChild) {
    List<FileSystemObjects> newChildren = new ArrayList<>(this.children);
    newChildren.add(newChild);
    return new Directory(this.name, this.path, newChildren);
  }

  public Directory withoutChild(FileSystemObjects childToRemove) {
    List<FileSystemObjects> newChildren = new ArrayList<>(this.children);
    newChildren.remove(childToRemove);
    return new Directory(this.name, this.path, newChildren);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Directory directory = (Directory) o;
    return name.equals(directory.name) && path.equals(directory.path);
  }
}
