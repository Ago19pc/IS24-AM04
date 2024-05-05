package Server.Messages;

import Server.Controller.Controller;
import Server.Exception.PlayerNotFoundByNameException;
import Server.Exception.ServerExecuteNotCallableException;

public interface ToServerMessage {
    public void serverExecute(Controller controller) throws ServerExecuteNotCallableException, PlayerNotFoundByNameException;
}
