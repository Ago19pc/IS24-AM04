package Server.Exception;

public class TooManyPlayersException extends Exception{
    public TooManyPlayersException(String message) {
        super(message);
    }
}
