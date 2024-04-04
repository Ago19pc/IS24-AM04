package Server.Messages;

import Server.Card.Card;
import Server.Player.Player;

import java.util.List;
import java.util.Map;

public class GiveCardMessage implements GeneralMessage {

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
}
