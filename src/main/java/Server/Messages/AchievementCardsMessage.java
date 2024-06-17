package Server.Messages;

import Client.Controller.ClientController;
import Server.Card.AchievementCard;

import java.io.Serializable;
import java.util.List;

/**
 * This message is used to send the achievement cards to the client
 */
public class AchievementCardsMessage implements Serializable, ToClientMessage {

    private List<AchievementCard> secretCards;
    private List<AchievementCard> commonCards;
    public AchievementCardsMessage(List<AchievementCard> secretCards, List<AchievementCard> commonCards){
        this.secretCards = secretCards;
        this.commonCards = commonCards;
    }

    @Override
    public void clientExecute(ClientController controller){
        controller.giveAchievementCards(secretCards, commonCards);
    }
}
