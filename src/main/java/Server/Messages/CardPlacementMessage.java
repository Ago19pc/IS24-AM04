package Server.Messages;

import Server.Card.Card;
import Server.Card.CardFace;
import Server.Controller.Controller;
import Server.Enums.Face;
import Server.Exception.InvalidMoveException;
import Server.Exception.PlayerNotFoundByNameException;
import Server.Exception.TooFewElementsException;
import Server.Player.Player;

import java.io.Serializable;

public class CardPlacementMessage implements Serializable, GeneralMessage {

    private final String name;
    private final int hand_pos;
    private final int xCoord;
    private final int yCoord;
    private final Face face;

    public CardPlacementMessage(String name, int hand_pos, int xCoord, int yCoord, Face face) {
        this.name = name;
        this.face = face;
        this.hand_pos = hand_pos;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }
    public void serverExecute(Controller controller)  {
        try {
            controller.playCard(controller.getPlayerByName(name), this.hand_pos, this.xCoord, this.yCoord, this.face);
        } catch (PlayerNotFoundByNameException | TooFewElementsException | InvalidMoveException e) {
            e.printStackTrace();
        }

    }
    public void clientExecute(){
        // dare il nuovo punteggio del player e il nuovo symbols count oltre alla conferma di mossa valida//

    }



}
