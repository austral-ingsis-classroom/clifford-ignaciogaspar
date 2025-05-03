package edu.austral.ingsis.clifford;

public record File(String name, String path) implements FileSystemObjects {

  @Override
  public boolean isDirectory() {
    return false;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    File file = (File) o;
    return name.equals(file.name) && path.equals(file.path);
  }
}
