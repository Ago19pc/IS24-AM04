package Server.Exception;

/**
 * Exception thrown when a game is already started
 */
public class AlreadyStartedException extends Exception {
    public AlreadyStartedException(String message) {
        super(message);
    }
}
