package edu.austral.ingsis.clifford.factories;

import edu.austral.ingsis.clifford.Result;
import edu.austral.ingsis.clifford.command.Command;
import edu.austral.ingsis.clifford.command.Touch;

public class TouchFactory implements CommandFactory {

  @Override
  public Result<Command> create(String input) {
    return new Result.Success<>(new Touch(input.trim()), "Touch command created");
  }
}
