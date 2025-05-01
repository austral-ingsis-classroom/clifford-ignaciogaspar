package edu.austral.ingsis.clifford.command;

import edu.austral.ingsis.clifford.Directory;
import edu.austral.ingsis.clifford.File;
import edu.austral.ingsis.clifford.FileSystem;
import edu.austral.ingsis.clifford.PathResolver;
import edu.austral.ingsis.clifford.Result;

public class Cd implements Command {
  private final String path;

  public Cd(String path) {
    this.path = path;
  }

  @Override
  public Result<FileSystem> execute(FileSystem fileSystem) {
    Result<Directory> resolutionResult = PathResolver.resolvePath(fileSystem.getCurrentDirectory(), fileSystem.getRoot(), path);

    return switch (resolutionResult) {
      case Result.Success<Directory> success -> {
        Directory targetDirectory = success.getValue();
        yield new Result.Success<>(fileSystem.withCurrentDirectory(targetDirectory),"moved to directory '" + targetDirectory.getName() + "'");
      }
      case Result.Error<Directory> error -> new Result.Error<>(error.getMessage());
    };
  }
}
