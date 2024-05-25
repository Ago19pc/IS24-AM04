package Server.Messages;

import Server.Controller.Controller;
import Server.Exception.PlayerNotFoundByNameException;
import Server.Exception.ServerExecuteNotCallableException;

public interface ToServerMessage {
    /**
     * This method is used to execute the message on the server side
     * @param controller the controller where the message will be executed
     */
    public void serverExecute(Controller controller);
}
