package edu.austral.ingsis.clifford;

public interface FileSystemObjects {

    public String getPath();
    public String getName();
    public boolean isDirectory();
    public Directory getFather();

}
