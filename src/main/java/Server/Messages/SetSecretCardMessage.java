package Server.Messages;

import Client.Controller.ClientController;
import Server.Controller.Controller;
import Server.Enums.Actions;
import Server.Exception.*;
import Server.Player.Player;

import java.io.Serializable;
import java.rmi.RemoteException;

public class SetSecretCardMessage implements Serializable, ToClientMessage, ToServerMessage {
    private String name;
    private Integer chosenCard;
    private String id;
    public SetSecretCardMessage(int chosenCard, String id){
        this.chosenCard = chosenCard;
        this.id = id;
    }
    public SetSecretCardMessage(String player){
        this.name = player;
        this.chosenCard = null;
    }

    @Override
    public void clientExecute(ClientController controller){
        if(chosenCard == null){
            controller.setSecretCard(name);
        } else {
            controller.setSecretCard(name, chosenCard);
        }
    }

    @Override
    public void serverExecute(Controller controller) {
        String playerName = "";
        try {
            playerName = controller.getConnectionHandler().getPlayerNameByID(this.id);
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
                controller.getConnectionHandler().getServerConnectionHandler(id).sendMessage(message, id);
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
