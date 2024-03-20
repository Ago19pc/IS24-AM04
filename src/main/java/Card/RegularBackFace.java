package main.java.Card;

import main.java.Enums.Symbol;

import java.util.List;
import java.util.Map;

import static main.java.Enums.Symbol.EMPTY;

public class RegularBackFace extends CornerCardFace {
    private final List<Symbol> centerSymbols;

    /**
     * Constructor for the RegularBackFace
     *
     * @param imageURI the URI of the image
     * @param card the card that owns the face
     * @param centerSymbols the center symbols
     */
    public RegularBackFace(String imageURI, Card card, List<Symbol> centerSymbols) {
        super(imageURI, card, Map.of(0, EMPTY, 1, EMPTY, 2, EMPTY, 3, EMPTY));
        this.centerSymbols = centerSymbols;
    }

    /**
     * Returns the center symbols
     * @return List<Symbol> the center symbols
     */
    public List<Symbol> getCenterSymbols() {
        return centerSymbols;
    }
}
