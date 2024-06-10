package Server.Messages;

import Client.Controller.ClientController;
import Server.Controller.Controller;
import Server.Enums.Actions;
import Server.Exception.*;
import Server.Player.Player;

import java.io.Serializable;
import java.rmi.RemoteException;

public class SetSecretCardMessage implements Serializable, ToClientMessage, ToServerMessage {
    private Integer chosenCard;
    private String idOrName;
    public SetSecretCardMessage(int chosenCard, String idOrName){
        this.chosenCard = chosenCard;
        this.idOrName = idOrName;
    }
    public SetSecretCardMessage(String player){
        this.idOrName = player;
        this.chosenCard = null;
    }

    @Override
    public void clientExecute(ClientController controller){
        if(chosenCard == null){
            controller.setSecretCard(idOrName);
        } else {
            controller.setSecretCard(idOrName, chosenCard);
        }
    }

    @Override
    public void serverExecute(Controller controller) {
        String playerName = "";
        try {
            playerName = controller.getConnectionHandler().getPlayerNameByID(this.idOrName);
            Player player = controller.getPlayerByName(playerName);
            if(chosenCard < 0 || chosenCard > 1){
                InvalidCardMessage invalidCardMessage = new InvalidCardMessage(Actions.SECRET_ACHIEVEMENT_CHOICE);
                controller.getConnectionHandler().sendMessage(invalidCardMessage, playerName);
            }
            controller.setSecretObjectiveCard(player, chosenCard);
        } catch (PlayerNotFoundByNameException e) {
            NameNotYetSetMessage message = new NameNotYetSetMessage();
            e.printStackTrace();
            try{
                controller.getConnectionHandler().getServerConnectionHandler(idOrName).sendMessage(message, idOrName);
            } catch (PlayerNotInAnyServerConnectionHandlerException exception) {
                System.out.println("Player not found");
            } catch (RemoteException exception) {
                System.out.println("Remote exception");
            }
        } catch (AlreadySetException e) {
            AlreadyDoneMessage alreadyDoneMessage = new AlreadyDoneMessage(Actions.SECRET_ACHIEVEMENT_CHOICE);
            controller.getConnectionHandler().sendMessage(alreadyDoneMessage, playerName);
        } catch (MissingInfoException e) {
            NotYetGivenCardMessage notYetGivenCardMessage = new NotYetGivenCardMessage(Actions.SECRET_ACHIEVEMENT_CHOICE);
            controller.getConnectionHandler().sendMessage(notYetGivenCardMessage, playerName);
        } catch (AlreadyStartedException e) {
            GameAlreadyStartedMessage gameAlreadyStartedMessage = new GameAlreadyStartedMessage();
            controller.getConnectionHandler().sendMessage(gameAlreadyStartedMessage, playerName);
        }
    }
}
