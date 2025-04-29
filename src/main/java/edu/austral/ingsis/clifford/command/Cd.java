package edu.austral.ingsis.clifford.command;

import edu.austral.ingsis.clifford.FileSystem;
import edu.austral.ingsis.clifford.PathResolver;
import edu.austral.ingsis.clifford.Result;
import edu.austral.ingsis.clifford.Directory;

public class Cd implements Command {
    private String path;

    public Cd(String path) {
        this.path = path;
    }

    @Override
    public Result execute(FileSystem fileSystem) {
        Result resolutionResult = PathResolver.resolvePath(fileSystem.getCurrentDirectory(), fileSystem.getRoot(), path);

        if (resolutionResult instanceof Result.Success) {
            Directory targetDirectory = ((Result.Success<Directory>) resolutionResult).getValue();
            fileSystem.setCurrentDirectory(targetDirectory);
            return new Result.Success<>("Moved to directory: '" + targetDirectory.getName() + "'");
        } else {
            return new Result.Error(((Result.Error) resolutionResult).getMessage());
        }
    }
}