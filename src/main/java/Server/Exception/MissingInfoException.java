package Server.Exception;

/**
 * Exception thrown when a required data is missing
 */
public class MissingInfoException extends Exception{
    /**
     * Constructor
     * @param message the message of the exception
     */
    public MissingInfoException(String message) {
        super(message);
    }
}
