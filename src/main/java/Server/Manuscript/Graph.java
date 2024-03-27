package main.java.Server.Manuscript;

import main.java.Server.Card.Card;
import main.java.Server.Card.CardFace;
import main.java.Server.Enums.CardCorners;

import java.util.Map;

public class Graph {
    private CardFace root;
    private Map<CardFace, Map<CardCorners, CardFace>> neighbors;

    public Graph(CardFace startingCardFace){
        this.root = startingCardFace;
        this.neighbors = new HashMap<>();
        Map<CardCorners, CardFace> corners = new HashMap<>();
        corners.put(CardCorners.TOP_LEFT, null);
        corners.put(CardCorners.TOP_RIGHT, null);
        corners.put(CardCorners.BOTTOM_LEFT, null);
        corners.put(CardCorners.BOTTOM_RIGHT, null);
        this.neighbors.put(startingCardFace, corners);
    }
    public CardFace getRoot(){
        return this.root;
    }

    public Map<CardFace, Map<CardCorners, CardFace>> getNeighbors(CardFace node

      return this.neighbors.get(node);

    }
    public Map<CardCorners, CardFace> getCardsOver(CardFace node){
        Map<CardCorners, CardFace> allCorners = this.neighbors.get(node);
        Map<CardCorners, CardFace> cardsOver = new HashMap<>();
        allCorners.keySet().forEach(corner -> {
            CardFace card = allCorners.get(corner);
            if(card != null && card.getPlacementTurn() > node.getPlacementTurn()){
                cardsOver.put(corner, card);
            } else {
                cardsOver.put(corner, null);
            }
        })
    }
    public Map<CardCorners, CardFace> getCardsUnder(CardFace node){
        Map<CardCorners, CardFace> allCorners = this.neighbors.get(node);
        Map<CardCorners, CardFace> cardsUnder = new HashMap<>();
        allCorners.keySet().forEach(corner -> {
            CardFace card = allCorners.get(corner);
            if(card != null && card.getPlacementTurn() < node.getPlacementTurn()){
                cardsUnder.put(corner, card);
            } else {
                cardsUnder.put(corner, null);
            }
        })
    }
    public void addNode(CornerCardFace node){
        Map<CardCorners, CardFace> corners = new HashMap<>();
        corners.put(CardCorners.TOP_LEFT, null);
        corners.put(CardCorners.TOP_RIGHT, null);
        corners.put(CardCorners.BOTTOM_LEFT, null);
        corners.put(CardCorners.BOTTOM_RIGHT, null);
        this.neighbors.put(node, corners);
    }
    public void addEdge(CornerCardFace overCard, CardCorners corner, CardFace underCard){
        this.neighbors.get(overCard).put(corner, underCard);
        this.neighbors.get(underard).put(corner.getOppositeCorner(), overCard);
        overCard.setPlacementTurn(turn);
    }
}
