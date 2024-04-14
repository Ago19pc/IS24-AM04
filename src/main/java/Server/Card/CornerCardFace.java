package Server.Card;

import Server.Enums.CardCorners;
import Server.Enums.Symbol;

import java.util.HashMap;
import java.util.Map;

public class CornerCardFace extends EmptyCardFace {
    private final Map<CardCorners, Symbol> cornerSymbols;
    private int placementTurn;
    private int xCoord;
    private int yCoord;


    /**
     * Constructor for the EmptyCardFace
     *
     * @param imageURI the URI of the image
     * @param cornerSymbols the corner symbols
     */
    public CornerCardFace(String imageURI, Map<CardCorners, Symbol> cornerSymbols) {
        super(imageURI);
        this.cornerSymbols = new HashMap<>(cornerSymbols);
    }

    /**
     * Returns the corner symbols
     * @return Map<Integer, Symbol> the corner symbols
     */
    public Map<CardCorners, Symbol> getCornerSymbols() {
        return cornerSymbols;
    }

    /**
     * @param placementTurn the placement turn
     */
    public void setPlacementTurn(int placementTurn) {
        this.placementTurn = placementTurn;
    }
    /**
     * Returns the placement turn of card to know what card is on top
     * @return int the placement turn
     */
    public int getPlacementTurn() {
        return placementTurn;
    }

    @Override
    public int getXCoord() {
        return xCoord;
    }

    @Override
    public int getYCoord() {
        return yCoord;
    }

    @Override
    public void setXCoord(int xCoord) {
        this.xCoord = xCoord;
    }

    @Override
    public void setYCoord(int yCoord) {
        this.yCoord = yCoord;
    }
}