package edu.austral.ingsis.clifford.command;

import edu.austral.ingsis.clifford.FileSystem;
import edu.austral.ingsis.clifford.Result;

public interface Command {
  public Result execute(FileSystem fileSystem);
}
