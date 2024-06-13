package Server.Card;

import Server.Enums.CardCorners;
import Server.Enums.Symbol;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class represents the front face of starting cards
 */
public class StartingFrontFace extends  CornerCardFace implements Serializable {
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

    public List<Symbol> getCenterSymbols() throws UnsupportedOperationException{
        return centerSymbols;
    }
    /**
     * @throws UnsupportedOperationException because starting cards do not have score requirements
     */
    @Override
    public Map<Symbol, Integer> getScoreRequirements() throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Starting cards do not have score requirements");
    }
    /**
     * @throws UnsupportedOperationException because starting cards do not have placement requirements
     */
    @Override
    public Map<Symbol, Integer> getPlacementRequirements() throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Starting cards do not have placement requirements");
    }
    /**
     * @throws UnsupportedOperationException because starting cards do not have a score
     */
    @Override
    public int getScore() throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Starting cards do not have scores");
    }
    /**
     * @throws UnsupportedOperationException because starting cards do not have a kingdom
     */
    @Override
    public Symbol getKingdom() throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Starting cards do not have kingdoms");
    }

    /**
     * Returns a string representation of the card face
     * @return a string representation of the card face
     */
    public String toString(){
        return super.toString() + "\nSimboli centrali: " + centerSymbols;
    }
}
