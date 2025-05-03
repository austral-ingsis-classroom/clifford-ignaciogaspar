package edu.austral.ingsis.clifford.command;

import edu.austral.ingsis.clifford.FileSystem;
import edu.austral.ingsis.clifford.Result;

public class Pwd implements Command {

  @Override
  public Result<FileSystem> execute(FileSystem fileSystem) {
    String path = fileSystem.getCurrentDirectory().path();
    return new Result.Success<>(fileSystem, path);
  }
}
