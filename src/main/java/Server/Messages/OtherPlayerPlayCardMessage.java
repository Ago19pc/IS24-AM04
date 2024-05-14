package Server.Messages;

import Client.Controller.ClientController;
import Server.Card.CornerCardFace;
import Server.Exception.ClientExecuteNotCallableException;
import Server.Exception.PlayerNotFoundByNameException;

import java.io.Serializable;

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
    public void clientExecute(ClientController controller) throws ClientExecuteNotCallableException, PlayerNotFoundByNameException {
        controller.placeCard(playerName, placedCardFace, x, y, obtainedPoints);
    }
}
