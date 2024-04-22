package Server.Messages;

import Server.Controller.Controller;

import java.io.Serializable;

public class EndGamePhaseMessage implements Serializable, GeneralMessage {


    @Override
    public void serverExecute(Controller controller) {

    }

    @Override
    public void clientExecute() {
        // avvisa il client che è il suo ulitmo turno //

    }
}
