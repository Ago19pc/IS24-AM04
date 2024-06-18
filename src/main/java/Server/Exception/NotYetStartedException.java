package Server.Exception;

/**
 * Exception thrown when a game is not yet started
 */
public class NotYetStartedException extends Exception {
    public NotYetStartedException(String message) {
        super(message);
    }
}
