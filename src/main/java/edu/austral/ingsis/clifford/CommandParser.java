package edu.austral.ingsis.clifford;

import edu.austral.ingsis.clifford.command.Command;
import edu.austral.ingsis.clifford.factories.CommandFactory;

public class CommandParser {
  private final CommandRegistry registry;

  public CommandParser(CommandRegistry registry) {
    this.registry = registry;
  }

  public Result<Command> parse(String input) {
    String[] parts = input.trim().split(" ", 2);
    String name = parts[0];
    String args = parts.length > 1 ? parts[1] : "";
    return switch (registry.getFactory(name)) {
      case Result.Success<CommandFactory> success -> success.getValue().create(args);
      case Result.Error<CommandFactory> error -> new Result.Error<>(error.getMessage());
    };
  }
}
