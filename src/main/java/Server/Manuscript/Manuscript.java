package Server.Manuscript;

import Server.Card.CornerCardFace;
import Server.Card.ResourceFrontFace;
import Server.Card.StartingCard;
import Server.Enums.CardCorners;
import Server.Enums.Symbol;
import Server.Enums.Face;



import java.util.List;
import java.util.Map;

public class Manuscript {
    private final Graph graph;
    private Map<Symbol, Integer> activeSymbols;

    public Manuscript(StartingCard card, Face face){
        graph = new Graph(card.getCornerFace(face));
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
        this.graph.addCard(cardFace, positions, turn);
    }

    /**
     * @return GraphNode the root of the manuscript
     */
    public int getSymbolCount(Symbol symbol){
        return this.activeSymbols.get(symbol);
    }
}
