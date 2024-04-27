package Server.Exception;

public class IllegalMessageTypeException extends Exception{
    public IllegalMessageTypeException(String type){
        super(type);
    }
}
