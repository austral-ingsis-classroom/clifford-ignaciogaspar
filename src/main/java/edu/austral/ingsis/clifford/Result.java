package edu.austral.ingsis.clifford;

public sealed interface Result permits Result.Error, Result.Success {

    record Success(String message) implements Result {
        public String getMessage(){
            return message;
        }
    }
    record Error(String message) implements Result {
        public String getMessage(){
            return message;
        }
    }
}
