package Server.Messages;

import Client.Controller.ClientController;
import Server.Card.CornerCardFace;
import Server.Controller.Controller;
import Server.Enums.Actions;
import Server.Enums.Face;
import Server.Exception.*;
import Server.Player.Player;

import java.io.Serializable;

/**
 * Message to ask the server to set a player's starting card and to notify the clients that a player's starting card has been set
 */
public class SetStartingCardMessage implements Serializable, ToClientMessage, ToServerMessage {
    private Face face;
    private String name;
    private CornerCardFace startingFace;
    private String id;

    /**
     * ToServer constructor
     * @param face the chosen face
     * @param id the id of the client
     */
    public SetStartingCardMessage(Face face, String id){
        this.face = face;
        this.id = id;
    }

    /**
     * ToClient constructor
     * @param name the name of the player who chose the starting card
     * @param startingFace the starting face of the player
     */
    public SetStartingCardMessage(String name, CornerCardFace startingFace){
        this.name = name;
        this.startingFace = startingFace;
    }

    /**
     * Sets the starting card for the corresponding player
     * @param controller the controller where the message will be executed
     */
    @Override
    public void clientExecute(ClientController controller){
        controller.startingCardChosen(name, startingFace);
    }

    /**
     * Asks the server controller to set a player's starting card
     * @param controller the controller where the message will be executed
     */
    @Override
    public void serverExecute(Controller controller){
        String playerName = "";
        try {
            playerName = controller.getConnectionHandler().getPlayerNameByID(this.id);
            Player player = controller.getPlayerByName(playerName);
            controller.setStartingCard(player, face);
        } catch (PlayerNotFoundByNameException e) {
            NameNotYetSetMessage message = new NameNotYetSetMessage();
            try{
                controller.getConnectionHandler().getServerConnectionHandler(id).sendMessage(message, id);
            } catch (PlayerNotInAnyServerConnectionHandlerException exception) {
                System.out.println("Player not found");
            } catch (java.rmi.RemoteException exception) {
                System.out.println("Remote exception");
            }
        } catch (AlreadySetException e) {
            AlreadyDoneMessage alreadyDoneMessage = new AlreadyDoneMessage(Actions.STARTING_CARD_CHOICE);
            controller.getConnectionHandler().sendMessage(alreadyDoneMessage, playerName);
        } catch (AlreadyStartedException e) {
            GameAlreadyStartedMessage gameAlreadyStartedMessage = new GameAlreadyStartedMessage();
            controller.getConnectionHandler().sendMessage(gameAlreadyStartedMessage, playerName);
        } catch (MissingInfoException e) {
            NotYetGivenCardMessage notYetGivenCardMessage = new NotYetGivenCardMessage(Actions.STARTING_CARD_CHOICE);
            controller.getConnectionHandler().sendMessage(notYetGivenCardMessage, playerName);
        }
    }
}
