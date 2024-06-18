package Server.Messages;

import Client.Controller.ClientController;
import Server.Card.CornerCardFace;

import java.io.Serializable;

/**
 * Message to inform the client that a player has placed a card on the board
 */
public class OtherPlayerPlayCardMessage implements Serializable, ToClientMessage {
    final String playerName;
    final CornerCardFace placedCardFace;
    final int x;
    final int y;
    final int obtainedPoints;

    /**
     * Constructor
     * @param playerName the name of the player who placed the card
     * @param placedCardFace the face of the card that was placed
     * @param x the x coordinate of the card
     * @param y the y coordinate of the card
     * @param obtainedPoints the points obtained by placing the card
     */
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
