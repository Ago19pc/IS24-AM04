package Server.Messages;

import Server.Controller.Controller;
import Server.Exception.ServerExecuteNotCallableException;

import java.io.Serializable;

public class OtherNewPointsMessage implements Serializable, GeneralMessage {
    @Override
    public void serverExecute(Controller controller)throws ServerExecuteNotCallableException {
        throw new ServerExecuteNotCallableException();

    }

    @Override
    public void clientExecute() {

    }
}
