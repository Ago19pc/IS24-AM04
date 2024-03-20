package main.java.Card;

import main.java.Enums.Symbol;


import java.util.Map;

public class ResourceFrontFace extends CornerCardFace {
    private final int score;

    /**
     * Constructor for the ResourceFrontFace
     *
     * @param imageURI the URI of the image
     * @param card the card that owns the face
     * @param cornerSymbols the corner symbols
     * @param score the score
     */
    public ResourceFrontFace(String imageURI, Card card, Map<Integer, Symbol> cornerSymbols, int score) {
        super(imageURI, card, cornerSymbols);
        this.score = score;
    }

    /**
     * Returns the score
     * @return int the score
     */
    public int getScore() {
        return score;
    }
}
