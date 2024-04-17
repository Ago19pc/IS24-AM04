package Server.Manuscript;



import Server.Card.CornerCardFace;
import Server.Enums.CardCorners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Graph {
    private final CornerCardFace root;
    private final Map<CornerCardFace, Map<CardCorners, CornerCardFace>> neighbors;
    /**
     * Constructor for the Graph. Sets the root, creates the card map, and adds the starting card
     * @param startingCardFace the starting card face
     */
    public Graph(CornerCardFace startingCardFace){
        this.root = startingCardFace;
        this.neighbors = new HashMap<>();
        addNode(root);
    }
    /**
     * Returns the root of the graph
     * @return CornerCardFace the root of the graph
     */
    public CornerCardFace getRoot(){
        return this.root;
    }

    /**
     * Returns the neighbors of a node
     * @param node the node to get the neighbors of
     * @return Map<CardCorners, CornerCardFace> the neighbors of the node
     */
    public Map<CardCorners, CornerCardFace> getNeighbors(CornerCardFace node){
      return this.neighbors.get(node);
    }
    /**
     * Returns the neighbors of a node that were placed after the node
     * @param node the node to get the neighbors of
     * @return Map<CardCorners, CornerCardFace> the neighbors of the node
     */
    public Map<CardCorners, CornerCardFace> getCardsOver(CornerCardFace node){
        Map<CardCorners, CornerCardFace> cardsOver;
        cardsOver = getNeighbors(node).entrySet().stream()
                .filter(neighbor -> neighbor.getValue() != null && neighbor.getValue().getPlacementTurn() > node.getPlacementTurn())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return cardsOver;
    }
    /**
     * Returns the neighbors of a node that were placed before the node
     * @param node the node to get the neighbors of
     * @return Map<CardCorners, CornerCardFace> the neighbors of the node
     */
    public Map<CardCorners, CornerCardFace> getCardsUnder(CornerCardFace node){
        Map<CardCorners, CornerCardFace> cardsUnder;
        cardsUnder = getNeighbors(node).entrySet().stream()
                .filter(neighbor -> neighbor.getValue() != null && neighbor.getValue().getPlacementTurn() < node.getPlacementTurn())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return cardsUnder;
    }

    /**
     * Adds a node to the graph and sets the neighbors to null
     * @param node the node to add
     */
    private void addNode(CornerCardFace node){
        Map<CardCorners, CornerCardFace> corners = new HashMap<>();
        corners.put(CardCorners.TOP_LEFT, null);
        corners.put(CardCorners.TOP_RIGHT, null);
        corners.put(CardCorners.BOTTOM_LEFT, null);
        corners.put(CardCorners.BOTTOM_RIGHT, null);
        this.neighbors.put(node, corners);
    }

    /**
     * Adds an edge between two nodes
     * @param firstCard the first card
     * @param corner the corner of the first card. It will be the opposite corner of the second card
     * @param secondCard the second card
     */
    private void addEdge(CornerCardFace firstCard, CardCorners corner, CornerCardFace secondCard){
        this.neighbors.get(firstCard).put(corner, secondCard);
        this.neighbors.get(secondCard).put(corner.getOppositeCorner(), firstCard);
    }

    /**
     * Adds a card to the graph, setting the placement turn and adding the edges
     * @param card the card to add
     * @param positions the neighbors of the card
     * @param turnPlaced the turn when the card gets placed
     */
    public void addCard(CornerCardFace card, Map<CardCorners, CornerCardFace> positions, int turnPlaced){
        card.setPlacementTurn(turnPlaced);
        addNode(card);
        positions.keySet().forEach(corner -> {
            if(positions.get(corner) != null) {
                addEdge(card, corner, positions.get(corner));
                System.out.println(this.neighbors.get(positions.get(corner)));
            }
        });
        System.out.println(this.neighbors.get(card));
    }


    /**
     * Get all cards in a list, a copy of the original list
     */
    public List<CornerCardFace> getAllCardsCopy(){
        return new ArrayList<>(this.neighbors.keySet());
    }

    /**
     * Get all cards in a list, the original list
     */
    public List<CornerCardFace> getAllCards() {
        return this.neighbors.keySet().stream().toList();
    }
}