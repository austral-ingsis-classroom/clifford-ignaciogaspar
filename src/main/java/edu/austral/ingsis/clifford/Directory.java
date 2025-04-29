package edu.austral.ingsis.clifford;

import java.util.List;

public class Directory implements FileSystemObjects {

    private String path;
    private String name;
    private List<FileSystemObjects> children;

    public Directory(String name, Directory father, List<FileSystemObjects> children) {
        this.name = name;
        this.path = (father != null ? father.getPath() : "") + "/" + name;
        this.children = children;
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
}
