package Server.Exception;

/**
 * Exception thrown when a move is invalid
 */
public class InvalidMoveException extends Exception {
    /**
     * Constructor
     * @param message the message to be shown
     */
    public InvalidMoveException(String message) {
        super(message);
    }
}
