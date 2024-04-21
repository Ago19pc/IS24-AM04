package Server.Messages;

import Server.Card.CardFace;
import Server.Player.Player;

import java.io.Serializable;

public class CardPlacementMessage implements Serializable, GeneralMessage {

    private final Player player;
    private final CardFace cardFace;
    private final int placementTurn;
    private final int where; // THIS IS TO CORRECT

    CardPlacementMessage(Player player, CardFace cardFace, int placementTurn, int where){
        this.player = player;
        this.cardFace = cardFace;
        this.placementTurn = placementTurn;
        this.where = where;
    }

    public void printData(){
        System.out.println("Player " + player.getName() + " placed a card on turn " + placementTurn + " at position " + where + " with card face " + cardFace);
    }

    public Player getPlayer(){
        return player;
    }

    public CardFace getCardFace(){
        return cardFace;
    }

    public int getPlacementTurn(){
        return placementTurn;
    }

    public int getWhere(){
        return where;
    }

    public boolean equals(GeneralMessage other){
        System.out.println("CardPlacementMessage equals still to be implemented.");
        return this.player.equals(((CardPlacementMessage) other).getPlayer()) && this.cardFace.equals(((CardPlacementMessage) other).getCardFace()) && this.placementTurn == ((CardPlacementMessage) other).getPlacementTurn() && this.where == ((CardPlacementMessage) other).getWhere();
    }
}
