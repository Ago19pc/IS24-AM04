package main.java.Server.Card;

import main.java.Server.Enums.Symbol;

import java.util.HashMap;
import java.util.Map;

public class CornerCardFace extends EmptyCardFace {
    private final Map<Integer, Symbol> cornerSymbols;
    private int placementTurn;

    /**
     * Constructor for the EmptyCardFace
     *
     * @param imageURI the URI of the image
     * @param cornerSymbols the corner symbols
     */
    public CornerCardFace(String imageURI, Map<Integer, Symbol> cornerSymbols) {
        super(imageURI);
        this.cornerSymbols = new HashMap<Integer, Symbol>(cornerSymbols);

    }

    /**
     * Returns the corner symbols
     * @return Map<Integer, Symbol> the corner symbols
     */
    public Map<Integer, Symbol> getCornerSymbols() {
        return cornerSymbols;
    }

    /**
     * Sets the placement turn of card to know what card is on top
     * @param placementTurn the placement turn
     */
    public void setPlacementTurn(int placementTurn) {
        this.placementTurn = placementTurn;
    }
    /**
     * Returns the placement turn of card to know what card is on top
     * @return int the placement turn
     */
    public int getPlacementTurn() {
        return placementTurn;
    }
}
