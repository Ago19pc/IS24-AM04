package Server.Exception;

/**
 * Exception thrown when trying to retrieve a player by name and the player is not found
 */
public class PlayerNotFoundByNameException extends Exception{
    public PlayerNotFoundByNameException (String name){
        super(name);
    }

}
