package Server.Messages;

import Server.Controller.Controller;
import Server.Exception.PlayerNotFoundByNameException;
import Server.Exception.ServerExecuteNotCallableException;

public interface ToServerMessage {
    /**
     * This method is used to execute the message on the server side
     * @param controller the controller where the message will be executed
     * @throws ServerExecuteNotCallableException if the message can't be executed
     * @throws PlayerNotFoundByNameException if the player is not found by name
     */
    public void serverExecute(Controller controller) throws ServerExecuteNotCallableException, PlayerNotFoundByNameException;
}
