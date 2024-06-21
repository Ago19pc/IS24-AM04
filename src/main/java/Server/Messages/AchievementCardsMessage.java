package Server.Messages;

import Client.Controller.ClientController;
import Server.Card.AchievementCard;

import java.io.Serializable;
import java.util.List;

/**
 * This message is used to send the achievement cards to the client
 */
public class AchievementCardsMessage implements Serializable, ToClientMessage {
    /**
     * The list of secret achievement cards from which the player can choose
     */
    private final List<AchievementCard> secretCards;
    /**
     * The list of common achievement cards
     */
    private final List<AchievementCard> commonCards;

    /**
     * Constructor. The messsage needs to contain the possible secret and the common achievement cards
     * @param secretCards the possible secret achievement cards for the player who receives the message
     * @param commonCards the possible common achievement cards
     */
    public AchievementCardsMessage(List<AchievementCard> secretCards, List<AchievementCard> commonCards){
        this.secretCards = secretCards;
        this.commonCards = commonCards;
    }

    @Override
    public void clientExecute(ClientController controller){
        controller.giveAchievementCards(secretCards, commonCards);
    }
}
