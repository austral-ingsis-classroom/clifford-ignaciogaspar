package edu.austral.ingsis.clifford.factories;

import edu.austral.ingsis.clifford.Result;
import edu.austral.ingsis.clifford.command.Command;
import edu.austral.ingsis.clifford.command.Ls;

public class LsFactory implements CommandFactory {

  @Override
  public Result<Command> create(String input) {
    String order = null;
    if (input.contains("--ord=")) {
      int startIndex = input.indexOf("--ord=") + 6;
      if (startIndex < input.length()) {
        order = input.substring(startIndex).trim();
      }
    }
    return new Result.Success<>(new Ls(order));
  }
}
