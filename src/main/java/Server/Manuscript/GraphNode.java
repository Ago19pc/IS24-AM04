package main.java.Server.Manuscript;

import main.java.Server.Card.Card;
import main.java.Server.Card.CardFace;
import main.java.Server.Enums.CardCorners;

import java.util.Map;

public class GraphNode {
    private CardFace root;
    private Map<CardFace, Map<CardCorners, CardFace>> neighbors;

    public GraphNode(CardFace startingCardFace){
        this.root = startingCardFace;

    }

    /* 
    public CardFace getCard(){
        return this.cardFace;
    }
    */
    public CardFace getRoot(){
        return this.root;
    }

    public Map<CardFace, Map<CardCorners, CardFace>> getNeighbors(){
        return this.neighbors;
    }
}
