package Server.Exception;

/**
 * Exception thrown when a game is already finished
 */
public class AlreadyFinishedException extends Exception {
    public AlreadyFinishedException(String message) {
        super(message);
    }
}
