package Server.Exception;

/**
 * Exception thrown when a game is not yet started
 */
public class NotYetStartedException extends Exception {
    /**
     * Constructor
     * @param message the message of the exception
     */
    public NotYetStartedException(String message) {
        super(message);
    }
}
