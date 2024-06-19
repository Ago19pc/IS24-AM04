package Server.Card;

import Server.Enums.CardCorners;
import Server.Enums.Symbol;

import java.util.List;
import java.util.Map;

/**
 * This class represents the front face of resource cards. Gold front faces extend this class
 * @see GoldFrontFace
 */
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


    public int getScore() throws UnsupportedOperationException{
        return score;
    }

    public Symbol getKingdom() throws UnsupportedOperationException{
        return kingdom;
    }
    /**
     * @throws UnsupportedOperationException because resource cards do not have score requirements
     */
    @Override
    public Map<Symbol, Integer> getScoreRequirements() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Resource cards do not have score requirements");
    }
    /**
     * @throws UnsupportedOperationException because resource cards do not have placement requirements
     */
    @Override
    public Map<Symbol, Integer> getPlacementRequirements() throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Resource cards do not have placement requirements");
    }
    /**
     * @throws UnsupportedOperationException because front faces of resource cards do not have center symbols
     */
    @Override
    public List<Symbol> getCenterSymbols() throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Resource cards do not have center symbols");
    }

    /**
     * Returns a string representation of the card
     * @return a string representation of the card
     */
    @Override
    public String toString() {
        String boundaries = "-".repeat(32);
        boundaries += "\n";
        String upper = String.format("|%-9s|%10s|%9s|\n", getCornerSymbols().get(CardCorners.TOP_LEFT), score != 0 ? score : " ", getCornerSymbols().get(CardCorners.TOP_RIGHT));
        String middle = String.format("|%30s|\n", "REGNO: " + getKingdom());
        String lower = String.format("|%-9s|%10s|%9s|\n", getCornerSymbols().get(CardCorners.BOTTOM_LEFT), " ", getCornerSymbols().get(CardCorners.BOTTOM_RIGHT));

        return boundaries + upper + middle + lower + boundaries;
    }
}
