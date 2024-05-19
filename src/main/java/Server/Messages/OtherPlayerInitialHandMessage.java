package Server.Messages;

import Client.Controller.ClientController;
import Server.Exception.ClientExecuteNotCallableException;
import Server.Exception.PlayerNotFoundByNameException;

import java.io.Serializable;

public class OtherPlayerInitialHandMessage implements Serializable, ToClientMessage {
    private final String name;
    public OtherPlayerInitialHandMessage(String name){
        this.name = name;
    }


    @Override
    public void clientExecute(ClientController controller){
        controller.giveOtherPlayerInitialHand(name);
    }
}
