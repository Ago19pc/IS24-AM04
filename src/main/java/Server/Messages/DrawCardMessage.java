package Server.Messages;

import Client.Controller.ClientController;
import Server.Card.Card;
import Server.Connections.ClientHandler;
import Server.Controller.Controller;
import Server.Enums.Actions;
import Server.Enums.DeckPosition;
import Server.Enums.Decks;
import Server.Exception.*;
import Server.Player.Player;

import java.io.Serializable;

public class DrawCardMessage implements Serializable, ToServerMessage, ToClientMessage {
    private Decks from;
    private DeckPosition deckPosition;
    private Card drawnCard;


    public DrawCardMessage(DeckPosition deckPosition, Decks from) {
        this.from = from;
        this.deckPosition= deckPosition;
    }

    public DrawCardMessage(Card drawnCard){
        this.drawnCard = drawnCard;
    }


    @Override
    public void serverExecute(Controller controller) {
        String playerName = "";
        try {
            ClientHandler client = controller.getConnectionHandler().getThreads()
                    .stream().filter(c -> c.getReceiver().threadId() == Thread.currentThread().threadId()).toList().getFirst();
            playerName = controller.getConnectionHandler().getThreadName(client);
            Player player = controller.getPlayerByName(playerName);
            if(from == Decks.ACHIEVEMENT){
                AchievementDeckDrawInvalidMessage achievementDeckDrawInvalidMessage = new AchievementDeckDrawInvalidMessage();
                controller.getConnectionHandler().sendMessage(achievementDeckDrawInvalidMessage, playerName);
            }
            controller.drawCard(player, deckPosition, from);
        } catch (PlayerNotFoundByNameException e) {
            NameNotYetSetMessage nameNotYetSetMessage = new NameNotYetSetMessage();
            //todo: send message to correct client based on ip
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
    @Override
    public void clientExecute(ClientController controller){
        controller.giveDrawnCard(drawnCard);
    }


}
