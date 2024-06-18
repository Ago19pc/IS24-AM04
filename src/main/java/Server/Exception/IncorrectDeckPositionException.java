package Server.Exception;

/**
 * Exception thrown when trying to use DECK as a board position
 */
public class IncorrectDeckPositionException extends Exception {
    public IncorrectDeckPositionException(String message) {
        super(message);
    }
}
