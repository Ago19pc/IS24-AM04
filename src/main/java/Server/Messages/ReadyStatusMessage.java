package Server.Messages;

import Client.Controller.ClientController;
import Server.Controller.Controller;
import Server.Exception.MissingInfoException;
import Server.Exception.PlayerNotFoundByNameException;

import java.io.Serializable;

public class ReadyStatusMessage implements Serializable, GeneralMessage {
    private boolean ready;
    private String name;

    public ReadyStatusMessage(boolean isReady, String name){
        this.ready = isReady;
        this.name= name;
    }
    @Override
    public void serverExecute(Controller controller) {
        try{
            controller.setReady(controller.getPlayerByName(this.name));
        } catch(PlayerNotFoundByNameException | MissingInfoException e){
            e.printStackTrace();
        }

    }

    @Override
    public void clientExecute(ClientController controller) {

    }
}
