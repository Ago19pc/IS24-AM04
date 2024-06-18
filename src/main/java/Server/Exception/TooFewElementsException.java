package Server.Exception;

/**
 * Exception thrown when there are too few elements to do an action.
 */
public class TooFewElementsException extends Exception {
    public TooFewElementsException(String message) {
        super(message);
    }
}
