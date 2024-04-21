package Server.Messages;

import Server.Card.Card;
import Server.Player.Player;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class GiveCardMessage implements Serializable, GeneralMessage {

    private final Map<Player, List<Card>> cards;

    public GiveCardMessage(Map<Player, List<Card>> cards){
        this.cards = cards;
    }

    public void printData(){
        for (Player player : cards.keySet()) {
            System.out.println("Player " + player.getName() + " received the following cards:");
            for (Card card : cards.get(player)) {
                System.out.println(card);
            }
        }
    }

    public Map<Player, List<Card>> getCards(){
        return cards;
    }

    public boolean equals(GeneralMessage other){
        System.out.println("GiveCardMessage equals still to be implemented.");
        return this.cards.equals(((GiveCardMessage) other).getCards());
    }
}
