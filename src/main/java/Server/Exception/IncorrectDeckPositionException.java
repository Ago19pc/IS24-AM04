package Server.Exception;

/**
 * Exception thrown when trying to use DECK as a board position
 */
public class IncorrectDeckPositionException extends Exception {
    /**
     * Constructor
     * @param message the message to be shown
     */
    public IncorrectDeckPositionException(String message) {
        super(message);
    }
}
