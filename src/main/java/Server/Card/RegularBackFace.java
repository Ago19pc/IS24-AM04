package Server.Card;

import Server.Enums.Symbol;

import java.util.List;
import java.util.Map;

import static Server.Enums.Symbol.EMPTY;

public class RegularBackFace extends CornerCardFace {
    private final List<Symbol> centerSymbols;

    /**
     * Constructor for the RegularBackFace
     *
     * @param imageURI the URI of the image
     * @param centerSymbols the center symbols
     */
    public RegularBackFace(String imageURI, List<Symbol> centerSymbols) {
        super(imageURI, Map.of(0, EMPTY, 1, EMPTY, 2, EMPTY, 3, EMPTY));
        this.centerSymbols = centerSymbols;
    }

    /**
     * Returns the center symbols
     * @return List<Symbol> the center symbols
     */
    public List<Symbol> getCenterSymbols() {
        return centerSymbols;
    }
}
