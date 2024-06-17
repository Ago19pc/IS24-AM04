package Server.Exception;

/**
 * Exception thrown when asking to set a value that is already set
 */
public class AlreadySetException extends Exception {
    public AlreadySetException(String message) {
        super(message);
    }
}
