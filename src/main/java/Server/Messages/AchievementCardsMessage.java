package Server.Messages;

import Client.Controller.ClientController;
import Server.Card.Card;
import Server.Exception.*;

import java.io.Serializable;
import java.util.List;

public class AchievementCardsMessage implements Serializable, ToClientMessage {

    private List<Card> secretCards;
    private List<Card> commonCards;
    public AchievementCardsMessage(List<Card> secretCards, List<Card> commonCards){
        this.secretCards = secretCards;
        this.commonCards = commonCards;
    }

    @Override
    public void clientExecute(ClientController controller) throws ClientExecuteNotCallableException {
        controller.giveAchievementCards(secretCards, commonCards);
    }
}
