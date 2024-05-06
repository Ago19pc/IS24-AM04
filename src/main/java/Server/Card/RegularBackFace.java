package Server.Card;

import Server.Enums.CardCorners;
import Server.Enums.Symbol;

import java.util.List;
import java.util.Map;

import static Server.Enums.Symbol.EMPTY;

public class RegularBackFace extends CornerCardFace {
    private final List<Symbol> centerSymbols;
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
        this.kingdom = centerSymbols.get(0);
    }

    /**
     * Returns the center symbols
     * @return List<Symbol> the center symbols
     */
    public List<Symbol> getCenterSymbols() throws UnsupportedOperationException{
        return centerSymbols;
    }

    /**
     * Returns the kingdom symbol
     * @return Symbol the kingdom symbol
     */
    public Symbol getKingdom() throws UnsupportedOperationException{
        return kingdom;
    }
    @Override
    public Map<Symbol, Integer> getScoreRequirements() throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Regular cards do not have score requirements");
    }
    @Override
    public Map<Symbol, Integer> getPlacementRequirements() throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Regular cards do not have placement requirements");
    }
    @Override
    public int getScore() throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Regular cards do not have scores");
    }

    public String toString() {
        String toReturn = "Regno: " + kingdom;
        toReturn += "\n" + super.toString() + "\n";
        toReturn += "Simboli centrali: " + centerSymbols;
        return toReturn;
    }
}
