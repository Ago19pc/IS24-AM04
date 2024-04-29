package Server.Messages;

import Client.Controller.ClientController;
import Server.Controller.Controller;
import Server.Exception.ServerExecuteNotCallableException;

import java.io.Serializable;

public class EndGamePhaseMessage implements Serializable, GeneralMessage {


    @Override
    public void serverExecute(Controller controller)throws ServerExecuteNotCallableException {
        throw new ServerExecuteNotCallableException();

    }

    @Override
    public void clientExecute(ClientController controller) {

        System.out.println("Last turn");

    }
}
