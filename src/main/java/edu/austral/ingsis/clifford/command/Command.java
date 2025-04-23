package edu.austral.ingsis.clifford.command;
import edu.austral.ingsis.clifford.Result;
public interface Command{
    public Result execute(String parameter);
}
