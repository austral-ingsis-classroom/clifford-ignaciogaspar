package edu.austral.ingsis.clifford.factories;

import edu.austral.ingsis.clifford.Result;
import edu.austral.ingsis.clifford.command.Cd;
import edu.austral.ingsis.clifford.command.Command;

public class CdFactory implements CommandFactory {

  @Override
  public Result<Command> create(String input) {
    return new Result.Success<>(new Cd(input.trim()), "");
  }
}
