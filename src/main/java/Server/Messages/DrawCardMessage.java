package Server.Messages;

import Client.Controller.ClientController;
import Server.Card.Card;
import Server.Controller.Controller;
import Server.Enums.Actions;
import Server.Enums.DeckPosition;
import Server.Enums.Decks;
import Server.Exception.*;
import Server.Player.Player;

import java.io.Serializable;
import java.rmi.RemoteException;

/**
 * This message is used to draw a card from a deck and to give the drawn card to the client who drew it
 */
public class DrawCardMessage implements Serializable, ToServerMessage, ToClientMessage {
    private Decks from;
    private DeckPosition deckPosition;
    private Card drawnCard;
    private String id;

    /**
     * ToServer Constructor
     * @param deckPosition the position of the deck where the card will be drawn
     * @param from the deck where the card will be drawn from
     * @param id the id of the player who will draw the card
     */
    public DrawCardMessage(DeckPosition deckPosition, Decks from, String id) {
        this.from = from;
        this.deckPosition= deckPosition;
        this.id = id;
    }

    /**
     * ToClient Constructor
     * @param drawnCard the card that was drawn
     */
    public DrawCardMessage(Card drawnCard){
        this.drawnCard = drawnCard;
    }

    /**
     * Draws a card from a deck
     * @param controller the controller where the message will be executed
     */
    @Override
    public void serverExecute(Controller controller) {
        String playerName = "";
        try {
            playerName = controller.getConnectionHandler().getPlayerNameByID(this.id);
            Player player = controller.getPlayerByName(playerName);
            if(from == Decks.ACHIEVEMENT){
                AchievementDeckDrawInvalidMessage achievementDeckDrawInvalidMessage = new AchievementDeckDrawInvalidMessage();
                controller.getConnectionHandler().sendMessage(achievementDeckDrawInvalidMessage, playerName);
            }
            controller.drawCard(player, deckPosition, from);
        } catch (PlayerNotFoundByNameException e) {
            NameNotYetSetMessage message = new NameNotYetSetMessage();
            try{
                controller.getConnectionHandler().getServerConnectionHandler(id).sendMessage(message, id);
            } catch (PlayerNotInAnyServerConnectionHandlerException exception) {
                System.out.println("Player not found");
            } catch (RemoteException exception) {
                System.out.println("Remote exception");
            }
        } catch (NotYetStartedException e) {
            GameNotYetStartedMessage gameNotYetStartedMessage = new GameNotYetStartedMessage();
            controller.getConnectionHandler().sendMessage(gameNotYetStartedMessage, playerName);
        } catch (InvalidMoveException e) {
            NotYourTurnMessage notYourTurnMessage = new NotYourTurnMessage();
            controller.getConnectionHandler().sendMessage(notYourTurnMessage, playerName);
        } catch (TooManyElementsException e) {
            ToDoFirstMessage toDoFirstMessage = new ToDoFirstMessage(Actions.PLAY_CARD);
        } catch (AlreadyFinishedException e) {
            EmptyDeckMessage emptyDeckMessage = new EmptyDeckMessage();
            controller.getConnectionHandler().sendMessage(emptyDeckMessage, playerName);
        }
    }
    /**
     * Gives the drawn card to the client who drew it
     * @param controller the controller where the message will be executed
     */
    @Override
    public void clientExecute(ClientController controller){
        controller.giveDrawnCard(drawnCard);
    }


}
