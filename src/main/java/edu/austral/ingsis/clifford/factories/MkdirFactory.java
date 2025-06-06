package edu.austral.ingsis.clifford.factories;

import edu.austral.ingsis.clifford.Result;
import edu.austral.ingsis.clifford.command.Command;
import edu.austral.ingsis.clifford.command.Mkdir;

public class MkdirFactory implements CommandFactory {

  @Override
  public Result<Command> create(String input) {
    return new Result.Success<>(new Mkdir(input.trim()), "");
  }
}
