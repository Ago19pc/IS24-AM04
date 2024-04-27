package Server.Card;

import Server.Enums.CardCorners;
import Server.Enums.Symbol;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CornerCardFace extends EmptyCardFace implements Serializable {
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
        this.placementTurn = 0;
        this.xCoord = 0;
        this.yCoord = 0;
    }

    /**
     * Returns the corner symbols
     * @return Map<Integer, Symbol> the corner symbols
     */
    public Map<CardCorners, Symbol> getCornerSymbols() throws UnsupportedOperationException {
        return cornerSymbols;
    }

    /**
     * @param placementTurn the placement turn
     */
    public void setPlacementTurn(int placementTurn) throws UnsupportedOperationException {
        this.placementTurn = placementTurn;
    }
    /**
     * Returns the placement turn of card to know what card is on top
     * @return int the placement turn
     */
    public int getPlacementTurn() throws UnsupportedOperationException {
        return placementTurn;
    }

    @Override
    public int getXCoord() throws UnsupportedOperationException{
        return xCoord;
    }

    @Override
    public int getYCoord() throws UnsupportedOperationException {
        return yCoord;
    }

    @Override
    public void setXCoord(int xCoord) throws UnsupportedOperationException{
        this.xCoord = xCoord;
    }

    @Override
    public void setYCoord(int yCoord) throws UnsupportedOperationException{
        this.yCoord = yCoord;
    }

    @Override
    public Map<Symbol, Integer> getScoreRequirements() throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Corner card Faces do not have score requirements");
    }

    @Override
    public int getScore() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Corner card Faces do not have scores");
    }

    @Override
    public List<Symbol> getCenterSymbols() throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Corner card Faces do not have center symbols");
    }

    @Override
    public Map<Symbol, Integer> getPlacementRequirements() throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Corner card Faces do not have placement requirements");
    }

    @Override
    public Symbol getKingdom() throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Corner card Faces do not have kingdom symbols");
    }
}