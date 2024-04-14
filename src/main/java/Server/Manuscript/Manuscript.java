package Server.Manuscript;

import Server.Card.AchievementCard;
import Server.Card.CornerCardFace;
import Server.Card.StartingCard;
import Server.Enums.CardCorners;
import Server.Enums.Symbol;
import Server.Enums.Face;


import java.util.HashMap;

import java.util.Map;

public class Manuscript {
    private final Graph graph;
    private Map<Symbol, Integer> activeSymbols;


    public Manuscript(StartingCard card, Face face){
        CornerCardFace cardFace = card.getCornerFace(face);
        graph = new Graph(cardFace);
        activeSymbols = new HashMap<>();
        for (Symbol symbol : Symbol.values()) {
            activeSymbols.put(symbol, 0);
        }
        updateSymbolCount(cardFace);
    }

    /**
     * Update the symbol count for the manuscript
     * @param cardFace the card face that will be added
     */
    private void updateSymbolCount(CornerCardFace cardFace){
        System.out.println(" updateSymbolCount");
        Map<CardCorners, CornerCardFace> cardsUnder = graph.getCardsUnder(cardFace);
        for(CardCorners corner : cardsUnder.keySet()){
            CornerCardFace neighbor = cardsUnder.get(corner);
            Symbol symbol = neighbor.getCornerSymbols().get(corner.getOppositeCorner());
            activeSymbols.put(symbol, activeSymbols.get(symbol) - 1);
        }
        try {
            for(Symbol symbol : cardFace.getCenterSymbols()){
                activeSymbols.put(symbol, activeSymbols.get(symbol) + 1);
            }
        } catch (UnsupportedOperationException e){
            //do nothing
        }
        for(CardCorners corner : cardFace.getCornerSymbols().keySet()){
            Symbol symbol = cardFace.getCornerSymbols().get(corner);
            activeSymbols.put(symbol, activeSymbols.get(symbol) + 1);
        }
    }

    /**
     * Add a card to the manuscript
     * @param xcoordinate where to add the card (x)
     * @param ycoordinate where to add the card (y)
     * @param cardFace which face to add
     * @param turn the turn the card was placed
     */
    public void addCard(int xcoordinate, int ycoordinate, CornerCardFace cardFace, int turn){
        cardFace.setXCoord(xcoordinate);
        cardFace.setYCoord(ycoordinate);
        Map<CardCorners, CornerCardFace> positions = new HashMap<>();
        CornerCardFace neighbor = this.graph.getCardByCoord(xcoordinate-1, ycoordinate+1);
        positions.put(CardCorners.TOP_LEFT, neighbor);
        neighbor = this.graph.getCardByCoord(xcoordinate+1, ycoordinate+1);
        positions.put(CardCorners.TOP_RIGHT, neighbor);
        neighbor = this.graph.getCardByCoord(xcoordinate-1, ycoordinate-1);
        positions.put(CardCorners.BOTTOM_LEFT, neighbor);
        neighbor = this.graph.getCardByCoord(xcoordinate+1, ycoordinate-1);
        positions.put(CardCorners.BOTTOM_RIGHT, neighbor);
        this.graph.addCard(cardFace, positions, turn);
        updateSymbolCount(cardFace);
    }

    /**
     * @return GraphNode the root of the manuscript
     */
    public int getSymbolCount(Symbol symbol) {
        return this.activeSymbols.get(symbol);
    }
    /**
     * Calculates the points given by an achievement card
     * @param achievementCard the achievement card to calculate points for
     * @return int the points given by the achievement card
     */
    public int calculatePoints(AchievementCard achievementCard) {
        Map<Symbol, Integer> scoreRequirements = achievementCard.getFace(Face.FRONT).getScoreRequirements();

    }
}
