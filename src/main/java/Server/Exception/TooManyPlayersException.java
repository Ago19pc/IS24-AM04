package Server.Exception;

/**
 * Exception thrown when there are too many players
 */
public class TooManyPlayersException extends Exception{
    public TooManyPlayersException(String message) {
        super(message);
    }
}
