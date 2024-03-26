package main.java.Messages;

import main.java.Card.Card;
import main.java.Player.Player;

public class OthersSecretCardSelectionMessage implements GeneralMessage {
    private final Card card;
    private final Player player;

    public OthersSecretCardSelectionMessage(Card card, Player player){
        this.card = card;
        this.player = player;
    }

    public void printData(){
        System.out.println("Player " + player.getName() + " selected a secret card.");
    }
}
