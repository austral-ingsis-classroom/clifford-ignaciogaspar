package edu.austral.ingsis.clifford;

import java.util.List;

public class Directory implements FileSystemObjects {

    private String path;
    private String name;
    private Directory father;
    private List<FileSystemObjects> children;

    public Directory(String name, Directory father, List<FileSystemObjects> children) {
        this.name = name;
        this.father = father;
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

    @Override
    public Directory getFather() {
        return father;
    }

    public List<FileSystemObjects> getChildren() {
        return children;
    }
}
