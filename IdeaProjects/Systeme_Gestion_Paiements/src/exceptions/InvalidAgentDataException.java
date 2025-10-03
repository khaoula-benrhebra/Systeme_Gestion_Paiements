package exceptions;

public class InvalidAgentDataException extends RuntimeException {
    public InvalidAgentDataException(String message) {
        super("Données agent invalides: " + message);
    }
}