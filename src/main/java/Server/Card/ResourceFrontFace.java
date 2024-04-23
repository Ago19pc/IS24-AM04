package Server.Card;

import Server.Enums.CardCorners;
import Server.Enums.Symbol;


import java.util.List;
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
    public int getScore() throws UnsupportedOperationException{
        return score;
    }

    public Symbol getKingdom() throws UnsupportedOperationException{
        return kingdom;
    }

    @Override
    public Map<Symbol, Integer> getScoreRequirements() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Resource cards do not have score requirements");
    }

    @Override
    public Map<Symbol, Integer> getPlacementRequirements() throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Resource cards do not have placement requirements");
    }

    @Override
    public List<Symbol> getCenterSymbols() throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Resource cards do not have center symbols");
    }

}
