package main.java.Server.Card;

import main.java.Server.Enums.Symbol;

import java.util.List;
import java.util.Map;

public class GoldFrontFace extends ResourceFrontFace {
    private final Map<Symbol, Integer> placementRequirements;
    private final List<Symbol> scoreRequirements;

    /**
     * Constructor for the GoldFrontFace
     *
     * @param imageURI the URI of the image
     * @param cornerSymbols the corner symbols
     * @param score the score
     * @param placementRequirements the placement requirements
     * @param scoreRequirements the score requirements
     */
    public GoldFrontFace(String imageURI, Map<Integer, Symbol> cornerSymbols, int score, Map<Symbol, Integer> placementRequirements, List<Symbol> scoreRequirements) {
        super(imageURI, cornerSymbols, score);
        this.placementRequirements = placementRequirements;
        this.scoreRequirements = scoreRequirements;
    }

    /**
     * Returns the placement requirements
     * @return Map<Symbol, Integer> the placement requirements
     */
    public Map<Symbol, Integer> getPlacementRequirements() {
        return placementRequirements;
    }

    /**
     * Returns the score requirements
     * @return List<Symbol> the score requirements
     */
    public List<Symbol> getScoreRequirements() {
        return scoreRequirements;
    }
}
