package main.java.Manuscript;

import main.java.Card.CornerCardFace;
import main.java.Card.ResourceFrontFace;
import main.java.Card.StartingCard;
import main.java.Enums.Symbol;
import main.java.Enums.Face;



import java.util.List;
import java.util.Map;

public class Manuscript {
    private GraphNode root;
    private Map<Symbol, Integer> activeSymbols;

    public Manuscript(StartingCard card, Face face){
        root = new GraphNode(card.getFace(face));
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
     */
    public void addCard(List<CornerCardFace> positions, ResourceFrontFace cardFace){
        System.out.println(this.toString() + " addCard");
    }

    /**
     * @return GraphNode the root of the manuscript
     */
    public int getSymbolCount(Symbol symbol){
        return this.activeSymbols.get(symbol);
    }
}
