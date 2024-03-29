package main.java.Server.Manuscript;

import main.java.Server.Card.CornerCardFace;
import main.java.Server.Card.ResourceFrontFace;
import main.java.Server.Card.StartingCard;
import main.java.Server.Enums.Symbol;
import main.java.Server.Enums.Face;



import java.util.List;
import java.util.Map;

public class Manuscript {
    private final Graph graph;
    private Map<Symbol, Integer> activeSymbols;

    public Manuscript(StartingCard card, Face face){
        root = new Graph(card.getFace(face));
    }

    /**
     * Update the symbol count for the manuscript
     */
    private void updateSymbolCount() {
        System.out.println(this.toString() + " updateSymbolCount");
    }

    /**
     * Add a card to the manuscript
     * @param positions where to add the card
     * @param cardFace which face to add
     * @param turn the turn the card was placed
     */
    public void addCard(Map<CardCorners, CornerCardFace> positions, CornerCardFace cardFace, int turn){
        this.graph.addCard(cardFace, positions, turn)
    }

    /**
     * @return GraphNode the root of the manuscript
     */
    public int getSymbolCount(Symbol symbol){
        return this.activeSymbols.get(symbol);
    }
}
