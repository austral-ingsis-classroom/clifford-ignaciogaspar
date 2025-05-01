package edu.austral.ingsis.clifford;

public class File implements FileSystemObjects {
  private final String name;
  private final String path;

  public File(String name, String path) {
    this.name = name;
    this.path = path;
  }

  public File withName(String newName){
    return new File(newName, this.path.substring(0, this.path.lastIndexOf('/')) + "/" + newName);
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
    return false;
  }
}
