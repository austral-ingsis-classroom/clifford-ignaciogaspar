package edu.austral.ingsis.clifford.factories;

import edu.austral.ingsis.clifford.CommandParser;
import edu.austral.ingsis.clifford.Result;
import edu.austral.ingsis.clifford.command.Command;
import edu.austral.ingsis.clifford.command.Pwd;

public class PwdFactory implements CommandFactory {

  @Override
  public Result<Command> create(String input) {
    return new Result.Success<>(new Pwd());
  }
}
