package Interface;
/**
 * This enum contains the names of the scenes.
 */
public enum SceneName {
    /**
     * Scene where the user chooses between rmi and socket
     */
    NETWORK,
    /**
     * Scene where the user joins a server
     */
    JOIN,
    /**
     * Scene where the user enters a game
     */
    SETNAME,
    /**
     * Scene where the user chooses the color and sets ready
     */
    SETCOLOR,
    /**
     * Scene where the user chooses the starting card
     */
    STARTINGCARDCHOICE,
    /**
     * Scene where the user chooses the secret achievement card
     */
    SECRETCARDCHOICE,
    /**
     * Main scene where the user plays the game
     */
    GAME,
    /**
     * End scene
     */
    END,
    /**
     * Scene where the user waits for the other players to reconnect
     */
    WAITING
}
