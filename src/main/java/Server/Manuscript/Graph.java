package Server.Manuscript;


import Server.Card.CornerCardFace;
import Server.Enums.CardCorners;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class that represents a graph of cards
 */
public class Graph implements Serializable {
    /**
     * The root of the graph
     */
    private final CornerCardFace root;
    /**
     * A list of all the cards in the graph
     */
    private final List<CornerCardFace> containedCards;
    /**
     * A list of maps that represent the neighbors of each card. For each card, the map contains the corner and the card that is connected to that corner
     */
    private final List<Map<CardCorners, CornerCardFace>> cardNeighbors;
    /**
     * Constructor for the Graph. Sets the root, creates the card map, and adds the starting card
     * @param startingCardFace the starting card face
     */
    public Graph(CornerCardFace startingCardFace){
        this.root = startingCardFace;
        this.containedCards = new ArrayList<>();
        this.cardNeighbors = new ArrayList<>();
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
     * @return a map of the neighbors of the node
     * @throws IllegalArgumentException if the node is not in the graph
     */
    public Map<CardCorners, CornerCardFace> getNeighbors(CornerCardFace node) throws IllegalArgumentException{
        if(!this.containedCards.contains(node)){
            throw new IllegalArgumentException("Node not in graph");
        }
        return this.cardNeighbors.get(this.containedCards.indexOf(node));
    }
    /**
     * Returns the neighbors of a node that were placed after the node
     * @param node the node to get the neighbors of
     * @return the neighbors of the node
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
     * @return the neighbors of the node
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
     * @throws IllegalArgumentException if the node is already in the graph
     */
    private void addNode(CornerCardFace node) throws IllegalArgumentException{
        if(this.containedCards.contains(node)){
            throw new IllegalArgumentException("Node already in graph");
        }
        Map<CardCorners, CornerCardFace> corners = new HashMap<>();
        corners.put(CardCorners.TOP_LEFT, null);
        corners.put(CardCorners.TOP_RIGHT, null);
        corners.put(CardCorners.BOTTOM_LEFT, null);
        corners.put(CardCorners.BOTTOM_RIGHT, null);
        this.containedCards.add(node);
        this.cardNeighbors.add(corners);
    }

    /**
     * Adds an edge between two nodes
     * @param firstCard the first card
     * @param corner the corner of the first card. It will be the opposite corner of the second card
     * @param secondCard the second card
     * @throws IllegalArgumentException if at least one of the nodes is not in the graph
     */
    private void addEdge(CornerCardFace firstCard, CardCorners corner, CornerCardFace secondCard) throws IllegalArgumentException{
        if(!this.containedCards.contains(firstCard) || !this.containedCards.contains(secondCard)){
            throw new IllegalArgumentException("At least one of the nodes is not in graph");
        }
        this.cardNeighbors.get(this.containedCards.indexOf(firstCard)).put(corner, secondCard);
        this.cardNeighbors.get(this.containedCards.indexOf(secondCard)).put(corner.getOppositeCorner(), firstCard);
    }

    /**
     * Adds a card to the graph, setting the placement turn and adding the edges
     * @param card the card to add
     * @param positions the neighbors of the card
     * @param turnPlaced the turn when the card gets placed
     * @throws IllegalArgumentException if the card is already in the graph or at least one of the nodes is not in the graph
     */
    public void addCard(CornerCardFace card, Map<CardCorners, CornerCardFace> positions, int turnPlaced) throws IllegalArgumentException{
        if(this.containedCards.contains(card)) {
            throw new IllegalArgumentException("Node already in graph");
        }
        for (CornerCardFace neighbor : positions.values()) {
            if (neighbor != null && !this.containedCards.contains(neighbor)) {
                throw new IllegalArgumentException("At least one of the nodes is not in graph");
            }
        }
        card.setPlacementTurn(turnPlaced);
        addNode(card);
        positions.keySet().forEach(corner -> {
            if(positions.get(corner) != null) {
                addEdge(card, corner, positions.get(corner));
            }
        });
    }

    /**
     * Get all cards in a list, the original list
     * @return the list of all cards
     */
    public List<CornerCardFace> getAllCards() {
        return this.containedCards;
    }
}