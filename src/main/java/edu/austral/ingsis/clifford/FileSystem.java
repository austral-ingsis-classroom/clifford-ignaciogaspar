package edu.austral.ingsis.clifford;

import java.util.ArrayList;
import java.util.List;

public class FileSystem {
    private Directory root;
    private Directory currentDirectory;

    public FileSystem() {
        this.root = new Directory("/", null, new ArrayList<>());
        this.currentDirectory = root;
    }

    public Directory getRoot() {
        return root;
    }

    public Directory getCurrentDirectory() {
        return currentDirectory;
    }

    public void setCurrentDirectory(Directory currentDirectory) {
        this.currentDirectory = currentDirectory;
    }
}
