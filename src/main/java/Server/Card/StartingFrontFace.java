package Server.Card;

import Server.Enums.CardCorners;
import Server.Enums.Symbol;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StartingFrontFace extends  CornerCardFace {
    private final List<Symbol> centerSymbols;

    /**
     * Constructor for the StartingFrontFace
     *
     * @param imageURI the URI of the image
     * @param cornerSymbols the corner symbols
     * @param centerSymbols the center symbols
     */
    public StartingFrontFace(String imageURI, Map<CardCorners, Symbol> cornerSymbols, List<Symbol> centerSymbols) {
        super(imageURI, cornerSymbols);
        this.centerSymbols = new ArrayList<Symbol>(centerSymbols);
    }

    /**
     * Returns the center symbols
     * @return List<Symbol> the center symbols
     */
    public List<Symbol> getCenterSymbols() {
        return centerSymbols;
    }

    @Override
    public Map<Symbol, Integer> getScoreRequirements() {
        throw new UnsupportedOperationException("Starting cards do not have score requirements");
    }

    @Override
    public Map<Symbol, Integer> getPlacementRequirements() {
        throw new UnsupportedOperationException("Starting cards do not have placement requirements");
    }

    @Override
    public int getScore() {
        throw new UnsupportedOperationException("Starting cards do not have scores");
    }

    @Override
    public Symbol getKingdom() {
        throw new UnsupportedOperationException("Starting cards do not have kingdoms");
    }

    
}
