package Server.Messages;

import Client.Controller.ClientController;
import Server.Controller.Controller;
import Server.Deck.AchievementDeck;
import Server.Deck.GoldDeck;
import Server.Deck.ResourceDeck;
import Server.Exception.ServerExecuteNotCallableException;

import java.io.Serializable;

public class BoardInitMessage implements Serializable, GeneralMessage {

    private final ResourceDeck resourceDeck;
    private final GoldDeck goldDeck;

    private final AchievementDeck achievementDeck;


    public BoardInitMessage(ResourceDeck resourceDeck, GoldDeck goldDeck, AchievementDeck achievementDeck) {
        this.resourceDeck = resourceDeck;
        this.goldDeck = goldDeck;
        this.achievementDeck = achievementDeck;
    }

    public void serverExecute(Controller controller)throws ServerExecuteNotCallableException {
        throw new ServerExecuteNotCallableException();
    }
    public void clientExecute(ClientController controller){

        controller.boardInit(achievementDeck, goldDeck, resourceDeck);


    }


}
