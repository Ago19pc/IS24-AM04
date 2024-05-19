package Server.Messages;

import Client.Controller.ClientController;
import Server.Enums.Actions;
import Server.Exception.ClientExecuteNotCallableException;

import java.io.Serializable;

public class InvalidCardMessage implements ToClientMessage, Serializable {
    private final Actions cardType;

    public InvalidCardMessage(Actions cardType) {
        this.cardType = cardType;
    }

    @Override
    public void clientExecute(ClientController controller){
        controller.invalidCard(cardType);
    }
}
