package main.java.Server.Manuscript;

import main.java.Server.Card.Card;
import main.java.Server.Card.CardFace;

import java.util.Map;

public class GraphNode {
    private final CardFace cardFace;
    private Map<Integer, GraphNode> neighbors;

    public GraphNode(CardFace cardFace){
        this.cardFace = cardFace;
        //this.neighbors.put(0, null);
        //this.neighbors.put(1, null);
        //this.neighbors.put(2, null);
        //this.neighbors.put(3, null);
    }

    public CardFace getCard(){
        return this.cardFace;
    }

    public Map<Integer, GraphNode> getNeighbors(){
        return this.neighbors;
    }
}
