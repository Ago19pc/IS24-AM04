package Server.Card;

import Server.Enums.CardCorners;
import Server.Enums.Symbol;

import java.util.List;
import java.util.Map;

import static Server.Enums.Symbol.EMPTY;

/**
 * This class represents the back face of gold and resource cards
 */
public class RegularBackFace extends CornerCardFace {
    /**
     * The center symbols of the card
     */
    private final List<Symbol> centerSymbols;
    /**
     * The kingdom symbol of the card
     */
    private final Symbol kingdom;
    /**
     * Constructor for the RegularBackFace
     *
     * @param imageURI the URI of the image
     * @param centerSymbols the center symbols
     */
    public RegularBackFace(String imageURI, List<Symbol> centerSymbols) {
        super(imageURI, Map.of(
                CardCorners.TOP_LEFT, EMPTY,
                CardCorners.TOP_RIGHT, EMPTY,
                CardCorners.BOTTOM_RIGHT, EMPTY,
                CardCorners.BOTTOM_LEFT, EMPTY));
        this.centerSymbols = centerSymbols;
        this.kingdom = centerSymbols.getFirst();
    }


    public List<Symbol> getCenterSymbols() throws UnsupportedOperationException{
        return centerSymbols;
    }


    public Symbol getKingdom() throws UnsupportedOperationException{
        return kingdom;
    }
    /**
     * @throws UnsupportedOperationException because regular back card faces do not have score requirements
     */
    @Override
    public Map<Symbol, Integer> getScoreRequirements() throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Regular cards do not have score requirements");
    }
    /**
     * @throws UnsupportedOperationException because regular back card faces do not have placement requirements
     */
    @Override
    public Map<Symbol, Integer> getPlacementRequirements() throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Regular cards do not have placement requirements");
    }
    /**
     * @throws UnsupportedOperationException because regular back card faces do not have a score
     */
    @Override
    public int getScore() throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Regular cards do not have scores");
    }

    /**
     * Gets a string representation of the card face
     * @return the string representation
     */
    public String toString() {
        String toReturn = "Regno: " + kingdom;
        toReturn += "\n" + super.toString() + "\n";
        toReturn += "Simboli centrali: " + centerSymbols;
        return toReturn;
    }
}
