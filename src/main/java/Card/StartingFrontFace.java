package main.java.Card;

import main.java.Enums.Symbol;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StartingFrontFace extends  CornerCardFace {
    private final List<Symbol> centerSymbols;

    /**
     * Constructor for the StartingFrontFace
     *
     * @param imageURI the URI of the image
     * @param card the card that owns the face
     * @param cornerSymbols the corner symbols
     * @param centerSymbols the center symbols
     */
    public StartingFrontFace(String imageURI, Card card, Map<Integer, Symbol> cornerSymbols, List<Symbol> centerSymbols) {
        super(imageURI, card, cornerSymbols);
        this.centerSymbols = new ArrayList<Symbol>(centerSymbols);
    }

    /**
     * Returns the center symbols
     * @return List<Symbol> the center symbols
     */
    public List<Symbol> getCenterSymbols() {
        return centerSymbols;
    }
}
