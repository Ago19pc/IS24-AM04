package Server.Messages;

import Client.Controller.ClientController;

import java.io.Serializable;

/**
 * Message to inform the client that there are too many players in the game, so he cannot join
 */
public class TooManyPlayersMessage implements ToClientMessage, Serializable {

    @Override
    public void clientExecute(ClientController controller){
        controller.tooManyPlayers();
    }
}
