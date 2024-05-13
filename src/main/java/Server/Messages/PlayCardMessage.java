package Server.Messages;

import Client.Controller.ClientController;
import Server.Controller.Controller;
import Server.Enums.Actions;
import Server.Enums.Face;
import Server.Exception.*;
import Server.Player.Player;

import java.io.Serializable;

public class PlayCardMessage implements Serializable, ToServerMessage{
    private String id;
    private int cardNumber;
    private int x;
    private int y;
    private Face face;

    public PlayCardMessage(String id, int cardNumber, Face face, int x, int y){
        this.id = id;
        this.cardNumber = cardNumber;
        this.x = x;
        this.y = y;
        this.face = face;
    }

    @Override
    public void serverExecute(Controller controller) throws ServerExecuteNotCallableException, PlayerNotFoundByNameException {
        String playerName = "";
        try {
            playerName = controller.getConnectionHandler().getPlayerNameByID(this.id);
            Player player = controller.getPlayerByName(playerName);
            controller.playCard(player, this.cardNumber, this.x, this.y, this.face);
        } catch (TooFewElementsException e ) {
            AlreadyDoneMessage alreadyDoneMessage = new AlreadyDoneMessage(Actions.PLAY_CARD);
            controller.getConnectionHandler().sendMessage(alreadyDoneMessage, playerName);
        } catch (InvalidMoveException e) {
            CardNotPlaceableMessage invalidMoveMessage = new CardNotPlaceableMessage();
            controller.getConnectionHandler().sendMessage(invalidMoveMessage, playerName);
        }
    }
}
