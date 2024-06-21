package Server.Exception;

/**
 * Exception thrown when asking to set a value that is already set
 */
public class AlreadySetException extends Exception {
    /**
     * Constructor.
     * @param message the message to be displayed
     */
    public AlreadySetException(String message) {
        super(message);
    }
}
