package main.java.Card;

import main.java.Enums.Symbol;

import java.util.HashMap;
import java.util.Map;

public class CornerCardFace extends EmptyCardFace {
    private final Map<Integer, Symbol> cornerSymbols;

    /**
     * Constructor for the EmptyCardFace
     *
     * @param imageURI the URI of the image
     * @param card the card that owns the face
     * @param cornerSymbols the corner symbols
     */
    public CornerCardFace(String imageURI, Card card, Map<Integer, Symbol> cornerSymbols) {
        super(imageURI, card);
        this.cornerSymbols = new HashMap<Integer, Symbol>(cornerSymbols);

    }

    /**
     * Returns the corner symbols
     * @return Map<Integer, Symbol> the corner symbols
     */
    public Map<Integer, Symbol> getCornerSymbols() {
        return cornerSymbols;
    }
}
