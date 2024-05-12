package Server.Messages;

import Client.Controller.ClientController;
import Server.Controller.Controller;
import Server.Enums.Actions;
import Server.Exception.*;
import Server.Player.Player;

import java.io.Serializable;

public class SetSecretCardMessage implements Serializable, ToClientMessage, ToServerMessage {
    private String name;
    private int chosenCard;
    private String id;
    public SetSecretCardMessage(int chosenCard, String id){
        this.chosenCard = chosenCard;
        this.id = id;
    }
    public SetSecretCardMessage(String player){
        this.name = player;
    }

    @Override
    public void clientExecute(ClientController controller) throws ClientExecuteNotCallableException {
        controller.setSecretCard(name);
    }

    @Override
    public void serverExecute(Controller controller) throws ServerExecuteNotCallableException {
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
            NameNotYetSetMessage nameNotYetSetMessage = new NameNotYetSetMessage();
            //todo: send message to correct client based on ip
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
