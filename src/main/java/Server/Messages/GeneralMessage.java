package Server.Messages;

import Client.Controller.ClientController;
import Server.Controller.Controller;
import Server.Exception.ClientExecuteNotCallableException;
import Server.Exception.ServerExecuteNotCallableException;

import java.io.Serializable;

public interface GeneralMessage extends Serializable {

    public void serverExecute(Controller controller) throws ServerExecuteNotCallableException;
    public void clientExecute(ClientController controller) throws ClientExecuteNotCallableException;



}
