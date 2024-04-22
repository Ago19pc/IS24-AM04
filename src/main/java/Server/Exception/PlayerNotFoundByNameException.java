package Server.Exception;

public class PlayerNotFoundByNameException extends Exception{
    public PlayerNotFoundByNameException (String name){
        super(name);
    }

}
