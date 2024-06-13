package Server.Messages;

import Client.Controller.ClientController;
import Server.Exception.ClientExecuteNotCallableException;
import Server.Exception.PlayerNotFoundByNameException;

import java.io.Serializable;

public class InvalidNameMessage implements ToClientMessage, Serializable {

    /**
     * This method is used to execute the message on the client side
     *
     * @param controller the controller where the message will be executed
     */
    @Override
    public void clientExecute(ClientController controller){
        controller.invalidName();
    }
}
