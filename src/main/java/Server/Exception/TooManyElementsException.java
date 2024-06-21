package Server.Exception;

/**
 * Exception thrown when there are too many elements to do an action.
 */
public class TooManyElementsException extends Exception {
    /**
     * Constructor
     * @param message the message of the exception
     */
    public TooManyElementsException(String message) {
        super(message);
    }
}
