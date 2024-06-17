package Server.Messages;

import Client.Controller.ClientController;

import java.io.Serializable;

public class NotYourTurnMessage implements Serializable, ToClientMessage{
    public NotYourTurnMessage() {
    }
    @Override
    public void clientExecute(ClientController controller){
        controller.notYourTurn();
    }
}
