package edu.austral.ingsis.clifford;

public class File implements FileSystemObjects{
    private String name;
    private String path;
    private Directory father;

    public File(String name, Directory father) {
        this.name = name;
        this.father = father;
        this.path = father.getPath() + "/" + name;
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

    @Override
    public Directory getFather() {
        return father;
    }
}
