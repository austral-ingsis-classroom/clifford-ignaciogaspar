package edu.austral.ingsis.clifford.factories;

import edu.austral.ingsis.clifford.Result;
import edu.austral.ingsis.clifford.command.Command;
import edu.austral.ingsis.clifford.command.Rm;

public class RmFactory implements CommandFactory {

  @Override
  public Result<Command> create(String input) {
    String fileName = input.trim();
    boolean recursive = false;

    if (input.contains("--recursive")) {
      fileName = input.replace("--recursive", "").trim();
      recursive = true;
    }

    return new Result.Success<>(new Rm(fileName, recursive), "");
  }
}
