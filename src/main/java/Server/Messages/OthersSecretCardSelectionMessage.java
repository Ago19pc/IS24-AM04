package Server.Messages;

import Server.Card.Card;
import Server.Player.Player;

public class OthersSecretCardSelectionMessage implements GeneralMessage {
    private final Card card;
    private final Player player;

    public OthersSecretCardSelectionMessage(Card card, Player player){
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
        return this.card.equals(((OthersSecretCardSelectionMessage) other).getCard()) && this.player.equals(((OthersSecretCardSelectionMessage) other).getPlayer());
    }
}
