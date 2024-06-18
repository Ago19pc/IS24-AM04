package Server.Messages;

import Client.Controller.ClientController;
import Server.Card.AchievementCard;

import java.io.Serializable;
import java.util.List;

public class AchievementCardsMessage implements Serializable, ToClientMessage {

    private final List<AchievementCard> secretCards;
    private final List<AchievementCard> commonCards;
    public AchievementCardsMessage(List<AchievementCard> secretCards, List<AchievementCard> commonCards){
        this.secretCards = secretCards;
        this.commonCards = commonCards;
    }

    @Override
    public void clientExecute(ClientController controller){
        controller.giveAchievementCards(secretCards, commonCards);
    }
}
