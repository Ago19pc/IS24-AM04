package Server.Messages;

import Server.Controller.Controller;
import Server.Enums.Actions;
import Server.Enums.Face;
import Server.Exception.*;
import Server.Player.Player;

import java.io.Serializable;

/**
 * Message to ask the server to place a card
 */
public class PlayCardMessage implements Serializable, ToServerMessage{
    /**
     * The id of the player who wants to place the card
     */
    private final String id;
    /**
     * The index in the player hand of the card to place
     */
    private final int cardNumber;
    /**
     * The x coordinate of the position where the card should be placed
     */
    private final int x;
    /**
     * The y coordinate of the position where the card should be placed
     */
    private final int y;
    /**
     * The face of the card that should be placed
     */
    private final Face face;

    /**
     * Constructor
     * @param id the id of the player who wants to place the card
     * @param cardNumber the number of the card to place in the player's hand
     * @param face the face of the card
     * @param x the x coordinate of the card
     * @param y the y coordinate of the card
     */
    public PlayCardMessage(String id, int cardNumber, Face face, int x, int y){
        this.id = id;
        this.cardNumber = cardNumber;
        this.x = x;
        this.y = y;
        this.face = face;
    }

    @Override
    public void serverExecute(Controller controller){
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
        } catch (PlayerNotFoundByNameException e) {
            NameNotYetSetMessage message = new NameNotYetSetMessage();
            try {
                controller.getConnectionHandler().getServerConnectionHandler(id).sendMessage(message, id);
            } catch (PlayerNotInAnyServerConnectionHandlerException exception) {
                System.out.println("Player not found");
            } catch (java.rmi.RemoteException exception) {
                System.out.println("Remote exception");
            }
        }
    }
}
