package Server.Exception;

/**
 * Exception thrown when there are too many players
 */
public class TooManyPlayersException extends Exception{
    /**
     * Constructor
     * @param message the message of the exception
     */
    public TooManyPlayersException(String message) {
        super(message);
    }
}
