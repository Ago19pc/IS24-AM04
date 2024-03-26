package main.java.Server.Messages;

import main.java.Server.Card.CardFace;
import main.java.Server.Player.Player;
public class CardPlacementMessage implements GeneralMessage {

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
}
