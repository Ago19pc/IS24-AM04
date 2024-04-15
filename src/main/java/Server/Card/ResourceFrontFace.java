package Server.Card;

import Server.Enums.CardCorners;
import Server.Enums.Symbol;


import java.util.Map;

public class ResourceFrontFace extends CornerCardFace {
    private final int score;
    private final Symbol kingdom;

    /**
     * Constructor for the ResourceFrontFace
     *
     * @param imageURI the URI of the image
     * @param cornerSymbols the corner symbols
     * @param score the score
     */
    public ResourceFrontFace(String imageURI, Map<CardCorners, Symbol> cornerSymbols, int score, Symbol kingdom) {
        super(imageURI, cornerSymbols);
        this.score = score;
        this.kingdom = kingdom;
    }

    /**
     * Returns the score
     * @return int the score
     */
    public int getScore() {
        return score;
    }

    public Symbol getKingdom() {
        return kingdom;
    }
}
