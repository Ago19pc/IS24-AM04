package Server.Exception;

/**
 * Exception thrown when a game is already started
 */
public class AlreadyStartedException extends Exception {
    /**
     * Constructor.
     * @param message the message to be displayed
     */
    public AlreadyStartedException(String message) {
        super(message);
    }
}
