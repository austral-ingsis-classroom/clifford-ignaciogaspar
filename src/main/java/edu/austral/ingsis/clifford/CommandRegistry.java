package edu.austral.ingsis.clifford;

import edu.austral.ingsis.clifford.factories.CommandFactory;

import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {
    private final Map<String, CommandFactory> factories = new HashMap<>();

    public void register(String name, CommandFactory factory) {
        factories.put(name, factory);
    }

    public Result getFactory(String name) {
        if (!factories.containsKey(name)) {
            return new Result.Error("Command not found: " + name);
        }
        return new Result.Success<>(factories.get(name));
    }

    public boolean hasCommand(String name) {
        return factories.containsKey(name);
    }
}