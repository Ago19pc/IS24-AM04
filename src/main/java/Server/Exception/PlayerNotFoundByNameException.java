package Server.Exception;

/**
 * Exception thrown when trying to retrieve a player by name and the player is not found
 */
public class PlayerNotFoundByNameException extends Exception{
    /**
     * Constructor
     * @param name the name of the player
     */
    public PlayerNotFoundByNameException (String name){
        super(name);
    }

}
