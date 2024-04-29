package Server.Messages;

import Server.Card.StartingCard;
import Server.Controller.Controller;
import Server.Enums.Face;
import Server.Exception.AlreadySetException;
import Server.Exception.PlayerNotFoundByNameException;

import java.io.Serializable;

public class StartingCardsMessage implements Serializable, GeneralMessage {

    private final String name;
    private final Face face;

    public StartingCardsMessage(String name, Face face) {
        this.name = name;
        this.face = face;
    }

    @Override
    public void serverExecute(Controller controller) {
        try{
            controller.setStartingCard(controller.getPlayerByName(this.name), this.face );
            System.out.println(controller.getPlayerByName(this.name).getName());
        }catch(PlayerNotFoundByNameException | AlreadySetException e){
            e.printStackTrace();
        }


    }

    @Override
    public void clientExecute() {

    }
}
