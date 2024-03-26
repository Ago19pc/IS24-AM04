package main.java.Messages;

import main.java.Card.CardFace;
import main.java.Player.Player;

public class OthersStartingCardSelectionMessage implements GeneralMessage {

    private final CardFace cardFace;
    private final Player player;

    public OthersStartingCardSelectionMessage(CardFace cardFace, Player player){
        this.cardFace = cardFace;
        this.player = player;
    }

    public void printData(){
        System.out.println("Player " + player.getName() + " selected a starting card.");
    }
}
