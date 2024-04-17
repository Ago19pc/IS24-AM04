package Server.Messages;

import Server.Card.CardFace;
import Server.Player.Player;

public class OthersStartingCardSelectionMessage implements GeneralMessage {

    private final CardFace cardFace;
    private final Player player;

    public OthersStartingCardSelectionMessage(CardFace cardFace, Player player){
        this.cardFace = cardFace;
        this.player = player;
    }

    public CardFace getCardFace(){
        return cardFace;
    }

    public Player getPlayer(){
        return player;
    }
    public void printData(){
        System.out.println("Player " + player.getName() + " selected a starting card.");
    }

    public boolean equals(GeneralMessage other){
        System.out.println("OthersStartingCardSelectionMessage equals still to be implemented.");
        return this.cardFace.equals(((OthersStartingCardSelectionMessage) other).getCardFace()) && this.player.equals(((OthersStartingCardSelectionMessage) other).getPlayer());
    }
}
