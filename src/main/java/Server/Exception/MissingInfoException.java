package Server.Exception;

/**
 * Exception thrown when a required data is missing
 */
public class MissingInfoException extends Exception{
    public MissingInfoException(String message) {
        super(message);
    }
}
