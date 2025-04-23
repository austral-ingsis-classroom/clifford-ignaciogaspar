package edu.austral.ingsis.clifford.command;

import edu.austral.ingsis.clifford.FileSystem;
import edu.austral.ingsis.clifford.Result;

public class Ls implements Command {
    private FileSystem fileSystem;

    public Ls(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    public Result execute(String parameter) {
        return fileSystem.list(parameter);
    }
}
