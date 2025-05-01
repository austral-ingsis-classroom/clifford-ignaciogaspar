package edu.austral.ingsis.clifford;

import edu.austral.ingsis.clifford.factories.CommandFactory;
import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {
    private final Map<String, CommandFactory> factories = new HashMap<>();

    public void register(String name, CommandFactory factory) {
        factories.put(name, factory);
    }

    public Result<CommandFactory> getFactory(String name) {
        if (!factories.containsKey(name)) {
            return new Result.Error<>("command not found: " + name);
        }
        return new Result.Success<>(factories.get(name));
    }
    public Result<String> addNewCommand(String name, CommandFactory factory) {
        if (factories.containsKey(name)) {
            return new Result.Error<>("command already exists: " + name);
        }
        factories.put(name, factory);
        return new Result.Success<>("command added successfully");
    }
}