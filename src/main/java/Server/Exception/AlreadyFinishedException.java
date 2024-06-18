package Server.Exception;

/**
 * Exception thrown when a game is already finished
 */
public class AlreadyFinishedException extends Exception {
    /**
     * Constructor.
     * @param message the message to be displayed
     */
    public AlreadyFinishedException(String message) {
        super(message);
    }
}
