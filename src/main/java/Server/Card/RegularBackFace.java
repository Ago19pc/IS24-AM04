package Server.Card;

import Server.Enums.CardCorners;
import Server.Enums.Symbol;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static Server.Enums.Symbol.EMPTY;

/**
 * This class represents the back face of gold and resource cards
 */
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
        String boundaries = "-".repeat(32);
        boundaries += "\n";
        String upper = String.format("|%-9s|%10s|%9s|\n", getCornerSymbols().get(CardCorners.TOP_LEFT), " ", getCornerSymbols().get(CardCorners.TOP_RIGHT));
        String middle = String.format("|%20s|%9s|\n", centerSymbols.stream().map(Symbol::toString).collect(Collectors.joining(" ")), "REGNO: " + getKingdom().toString());
        String lower = String.format("|%-9s|%10s|%9s|\n", getCornerSymbols().get(CardCorners.BOTTOM_LEFT), " ", getCornerSymbols().get(CardCorners.BOTTOM_RIGHT));
        return boundaries + upper + middle + lower + boundaries;
    }
}
