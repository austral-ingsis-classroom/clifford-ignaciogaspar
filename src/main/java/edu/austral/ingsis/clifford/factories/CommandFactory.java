package edu.austral.ingsis.clifford.factories;

import edu.austral.ingsis.clifford.Result;
import edu.austral.ingsis.clifford.command.Command;

public interface CommandFactory {
  Result<Command> create(String input);
}
