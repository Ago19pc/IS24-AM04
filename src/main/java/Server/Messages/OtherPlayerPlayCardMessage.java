package Server.Messages;

import Client.Controller.ClientController;
import Server.Card.CornerCardFace;

import java.io.Serializable;

/**
 * Message to inform the client that a player has placed a card on the board
 */
public class OtherPlayerPlayCardMessage implements Serializable, ToClientMessage {
    String playerName;
    CornerCardFace placedCardFace;
    int x;
    int y;
    int obtainedPoints;

    public OtherPlayerPlayCardMessage(String playerName, CornerCardFace placedCardFace, int x, int y, int obtainedPoints) {
        this.playerName = playerName;
        this.placedCardFace = placedCardFace;
        this.x = x;
        this.y = y;
        this.obtainedPoints = obtainedPoints;
    }

    @Override
    public void clientExecute(ClientController controller){
        controller.placeCard(playerName, placedCardFace, x, y, obtainedPoints);
    }
}
