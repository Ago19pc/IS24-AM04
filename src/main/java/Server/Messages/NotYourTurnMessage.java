package Server.Messages;

import Client.Controller.ClientController;

import java.io.Serializable;

/**
 * Message to inform the client that it is not his turn, so he cannot do what he tried to do

 */
public class NotYourTurnMessage implements Serializable, ToClientMessage{
    public NotYourTurnMessage() {
    }
    @Override
    public void clientExecute(ClientController controller){
        controller.notYourTurn();
    }
}
