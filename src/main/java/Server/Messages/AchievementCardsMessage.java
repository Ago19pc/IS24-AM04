package Server.Messages;

import Client.Controller.ClientController;
import Server.Card.AchievementCard;
import Server.Exception.*;

import java.io.Serializable;
import java.util.List;

public class AchievementCardsMessage implements Serializable, ToClientMessage {

    private List<AchievementCard> secretCards;
    private List<AchievementCard> commonCards;
    public AchievementCardsMessage(List<AchievementCard> secretCards, List<AchievementCard> commonCards){
        this.secretCards = secretCards;
        this.commonCards = commonCards;
    }

    @Override
    public void clientExecute(ClientController controller) throws ClientExecuteNotCallableException {
        controller.giveAchievementCards(secretCards, commonCards);
    }
}
