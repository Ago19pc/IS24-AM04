package Server.Card;

import Server.Enums.CardCorners;
import Server.Enums.Symbol;

import java.util.List;
import java.util.Map;

/**
 * This class represents the front face of gold cards
 */
public class GoldFrontFace extends ResourceFrontFace {
    private final Map<Symbol, Integer> placementRequirements;
    private final Map<Symbol, Integer> scoreRequirements;

    /**
     * Constructor for the GoldFrontFace
     *
     * @param imageURI the URI of the image
     * @param cornerSymbols the corner symbols
     * @param score the score
     * @param placementRequirements the placement requirements
     * @param scoreRequirements the score requirements
     */
    public GoldFrontFace(String imageURI, Map<CardCorners, Symbol> cornerSymbols, int score, Map<Symbol, Integer> placementRequirements, Map<Symbol, Integer> scoreRequirements, Symbol kingdom){
        super(imageURI, cornerSymbols, score, kingdom);
        this.placementRequirements = placementRequirements;
        this.scoreRequirements = scoreRequirements;
    }

    public Map<Symbol, Integer> getPlacementRequirements() throws UnsupportedOperationException{
        return placementRequirements;
    }

    public Map<Symbol, Integer> getScoreRequirements() throws UnsupportedOperationException {
        return scoreRequirements;
    }
    /**
     * @throws UnsupportedOperationException because front faces of gold cards do not have score requirements
     */
    @Override
    public List<Symbol> getCenterSymbols() throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Gold cards do not have center symbols");
    }

    /**
     * Returns a string representation of the card face
     * @return a string representation of the card face
     */
    @Override
    public String toString() {
        String toReturn =   "----------\n" +
                            "|" + getCornerSymbols().get(CardCorners.TOP_LEFT) + "        " + getCornerSymbols().get(CardCorners.TOP_RIGHT) + "|\n" +
                            "|" + "        |\n" +
                            "|" + "    "  + getKingdom() + "    |\n" +
                            "|" + "        |\n" +
                            "|" + getCornerSymbols().get(CardCorners.BOTTOM_LEFT) + "        " + getCornerSymbols().get(CardCorners.BOTTOM_RIGHT) + "|\n" +
                            "----------\n";

        return toReturn;
    }
}
