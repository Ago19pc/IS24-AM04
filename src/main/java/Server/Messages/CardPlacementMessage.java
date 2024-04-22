package Server.Messages;

import Server.Card.Card;
import Server.Card.CardFace;
import Server.Controller.Controller;
import Server.Enums.Face;
import Server.Exception.PlayerNotFoundByNameException;
import Server.Player.Player;

import java.io.Serializable;

public class CardPlacementMessage implements Serializable, GeneralMessage {

    private final String name;
    private final Card card;
    private final int xCoord;
    private final int yCoord;
    private final Face face;

    public CardPlacementMessage(String name, Card card, int xCoord, int yCoord, Face face) {
        this.name = name;
        this.face = face;
        this.card = card;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }
    public void serverExecute(Controller controller)  {
        try {
            controller.playCard(controller.getPlayerByName(name), this.card, this.xCoord, this.yCoord, this.face);
        } catch (PlayerNotFoundByNameException e) {
            e.printStackTrace();
        }

    }
    public void clientExecute(){
        // dare il nuovo punteggio del player e il nuovo symbols count oltre alla conferma di mossa valida//

    }



}
