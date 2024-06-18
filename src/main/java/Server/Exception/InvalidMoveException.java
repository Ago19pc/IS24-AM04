package Server.Exception;

/**
 * Exception thrown when a move is invalid
 */
public class InvalidMoveException extends Exception {
    public InvalidMoveException(String message) {
        super(message);
    }
}
