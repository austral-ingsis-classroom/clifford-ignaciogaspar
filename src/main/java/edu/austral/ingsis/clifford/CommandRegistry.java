package edu.austral.ingsis.clifford;

import edu.austral.ingsis.clifford.factories.CommandFactory;
import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {
  private final Map<String, CommandFactory> factories;

  public CommandRegistry(Map<String, CommandFactory> factories) {
    this.factories = factories;
  }

  public Result<CommandFactory> getFactory(String name) {
    if (!factories.containsKey(name)) {
      return new Result.Error<>("command not found: " + name);
    }
    return new Result.Success<>(factories.get(name), "");
  }

  public Result<CommandRegistry> addNewCommand(String name, CommandFactory factory) {
    if (factories.containsKey(name)) {
      return new Result.Error<>("command already exists: " + name);
    }
    Map<String, CommandFactory> newFactories = new HashMap<>();
    for (String command : factories.keySet()) {
      newFactories.put(command, factories.get(command));
    }
    newFactories.put(name, factory);
    CommandRegistry newRegistry = new CommandRegistry(newFactories);
    return new Result.Success<>(newRegistry, "command added successfully");
  }
}
