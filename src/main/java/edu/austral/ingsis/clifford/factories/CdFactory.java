package edu.austral.ingsis.clifford.factories;

import edu.austral.ingsis.clifford.Result;
import edu.austral.ingsis.clifford.command.Command;
import edu.austral.ingsis.clifford.command.Cd;

public class CdFactory implements CommandFactory {

    @Override
    public Result create(String input) {
        return new Result.Success<>(new Cd(input.trim()));
    }
}