package edu.austral.ingsis.clifford;

public sealed interface Result permits Result.Error, Result.Success {
    record Success<T>(T value) implements Result {
        public Success(T value) {
            this.value = value;
        }
        public T getValue(){
            return value;
        }
    }
    record Error(String message) implements Result {
        public String getMessage(){
            return message;
        }
    }
}
