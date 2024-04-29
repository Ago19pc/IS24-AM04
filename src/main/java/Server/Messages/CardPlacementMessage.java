package Server.Messages;

import Client.Controller.ClientController;
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

    private String name;
    private int hand_pos;
    private int xCoord;
    private int yCoord;
    private Face face;

    private boolean isPlayable;

    public CardPlacementMessage(boolean isPlayable){
        this.isPlayable = isPlayable;
    }

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
    public void clientExecute(ClientController controller){
        if (isPlayable){
            controller.cardPlacement();
        } else {
            // CLI
            System.out.println("Mossa non valida, ripetere");
            // GUI
        }

        // dare il nuovo punteggio del player e il nuovo symbols count oltre alla conferma di mossa valida//

    }



}
