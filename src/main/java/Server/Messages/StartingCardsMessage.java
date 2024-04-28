package Server.Messages;

import Server.Card.StartingCard;
import Server.Controller.Controller;
import Server.Enums.Face;
import Server.Exception.AlreadySetException;
import Server.Exception.PlayerNotFoundByNameException;

import java.io.Serializable;

public class StartingCardsMessage implements Serializable, GeneralMessage {

    private final String name;
    private final StartingCard startingCard;
    private final Face face;

    public StartingCardsMessage(String name, StartingCard startingCard, Face face) {
        this.name = name;
        this.startingCard = startingCard;
        this.face = face;
    }

    @Override
    public void serverExecute(Controller controller) {
        try{
            controller.setStartingCard(controller.getPlayerByName(this.name), this.startingCard, this.face );
            System.out.println(controller.getPlayerByName(this.name).getName());
        }catch(PlayerNotFoundByNameException | AlreadySetException e){
            e.printStackTrace();
        }


    }

    @Override
    public void clientExecute() {

    }
}
