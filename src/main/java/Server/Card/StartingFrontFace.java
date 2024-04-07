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

    
}
