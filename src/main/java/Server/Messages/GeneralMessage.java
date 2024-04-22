package Server.Messages;

import Server.Controller.Controller;

import java.io.Serializable;

public interface GeneralMessage extends Serializable {

    public void serverExecute(Controller controller);
    public void clientExecute();



}
