package Client.View;

/**
 * This interface is used to represent the state of the CLI. There is one for each scene of the game.
 */
public interface CLIState {
    /**
     * Decodes the input of the user and acts accordingly
     * @param args the input of the user
     */
    void decode(String[] args);
}
