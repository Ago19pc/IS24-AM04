package Server.Messages;

import Server.Controller.Controller;

import java.io.Serializable;

public class UnavaiableColorsMessage implements Serializable, GeneralMessage {

    @Override
    public void serverExecute(Controller controller) {


    }

    @Override
    public void clientExecute() {

    }
}
