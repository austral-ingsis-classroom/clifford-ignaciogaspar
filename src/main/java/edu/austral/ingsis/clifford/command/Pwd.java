package edu.austral.ingsis.clifford.command;

import edu.austral.ingsis.clifford.FileSystem;
import edu.austral.ingsis.clifford.Result;
import edu.austral.ingsis.clifford.Directory;

public class Pwd implements Command {

    @Override
    public Result execute(FileSystem fileSystem) {
        return new Result.Success<>(fileSystem.getCurrentDirectory().getPath());
    }
}