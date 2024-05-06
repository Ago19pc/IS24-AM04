package Server.Messages;

import Client.Controller.ClientController;
import Server.Card.CardFace;
import Server.Card.CornerCardFace;
import Server.Connections.ClientHandler;
import Server.Controller.Controller;
import Server.Enums.Actions;
import Server.Enums.Face;
import Server.Exception.*;
import Server.Player.Player;

import java.io.Serializable;

public class SetStartingCardMessage implements Serializable, ToClientMessage, ToServerMessage {
    private Face face;
    private String name;
    private CardFace startingFace;

    public SetStartingCardMessage(Face face){
        this.face = face;
    }

    public SetStartingCardMessage(String name, CardFace startingFace){
        this.name = name;
        this.startingFace = startingFace;
    }

    @Override
    public void clientExecute(ClientController controller) throws ClientExecuteNotCallableException {
        controller.startingCardChosen(name, startingFace);
    }

    @Override
    public void serverExecute(Controller controller) throws ServerExecuteNotCallableException{
        String playerName = "";
        try {
            ClientHandler client = controller.getConnectionHandler().getServerConnectionHandlerSOCKET().getThreads()
                    .stream().filter(c -> c.getReceiver().threadId() == Thread.currentThread().threadId()).toList().getFirst();
            playerName = controller.getConnectionHandler().getServerConnectionHandlerSOCKET().getThreadName(client);
            Player player = controller.getPlayerByName(playerName);
            controller.setStartingCard(player, face);
        } catch (PlayerNotFoundByNameException e) {
            NameNotYetSetMessage nameNotYetSetMessage = new NameNotYetSetMessage();
            //todo: send message to correct client based on ip
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
