package Server.Exception;

/**
 * Exception thrown when there are too few elements to do an action.
 */
public class TooFewElementsException extends Exception {
    /**
     * Constructor
     * @param message the message of the exception
     */
    public TooFewElementsException(String message) {
        super(message);
    }
}
