package Server.Messages;

import Server.Card.Card;
import Server.Player.Player;

import java.io.Serializable;

public class OthersSecretCardMessage implements Serializable, GeneralMessage {
    private final Card card;
    private final Player player;

    public OthersSecretCardMessage(Card card, Player player){
        this.card = card;
        this.player = player;
    }

    public Card getCard(){
        return card;
    }
    public Player getPlayer(){
        return player;
    }
    public void printData(){
        System.out.println("Player " + player.getName() + " selected a secret card.");
    }

    public boolean equals(GeneralMessage other){
        System.out.println("OthersSecretCardSelectionMessage equals still to be implemented.");
        return this.card.equals(((OthersSecretCardMessage) other).getCard()) && this.player.equals(((OthersSecretCardMessage) other).getPlayer());
    }
}
