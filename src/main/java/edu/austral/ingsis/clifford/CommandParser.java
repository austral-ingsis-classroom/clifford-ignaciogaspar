package edu.austral.ingsis.clifford;

import edu.austral.ingsis.clifford.factories.CommandFactory;

public class CommandParser {
  private final CommandRegistry registry;

  public CommandParser(CommandRegistry registry) {
    this.registry = registry;
  }

  public Result parse(String input) {
    String[] parts = input.trim().split(" ", 2);
    String name = parts[0];
    String args = parts.length > 1 ? parts[1] : "";

    return switch (registry.getFactory(name)) {
      case Result.Success<?> success -> {
        CommandFactory factory = (CommandFactory) success.getValue();
        yield factory.create(args);
      }
      case Result.Error error -> error;
    };
  }
}
