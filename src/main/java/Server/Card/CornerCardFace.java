package Server.Card;

import Server.Enums.CardCorners;
import Server.Enums.Symbol;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is the one from which all the corner card faces inherit. It is also used to represent the back face of starting cards
 */
public class CornerCardFace extends EmptyCardFace implements Serializable {
    /**
     * The corner symbols of the card
     */
    private final Map<CardCorners, Symbol> cornerSymbols;
    /**
     * The turn in which the card was placed
     */
    private int placementTurn;
    /**
     * The x coordinate of the card
     */
    private int xCoord;
    /**
     * The y coordinate of the card
     */
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

    public Map<CardCorners, Symbol> getCornerSymbols() throws UnsupportedOperationException {
        return cornerSymbols;
    }

    public void setPlacementTurn(int placementTurn) throws UnsupportedOperationException {
        this.placementTurn = placementTurn;
    }

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
    /**
     * @throws UnsupportedOperationException because basic corner card faces do not have score requirements
     */
    @Override
    public Map<Symbol, Integer> getScoreRequirements() throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Corner card Faces do not have score requirements");
    }
    /**
     * @throws UnsupportedOperationException because basic corner card faces do not have a score
     */
    @Override
    public int getScore() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Corner card Faces do not have scores");
    }
    /**
     * @throws UnsupportedOperationException because basic corner card faces do not have center symbols
     */
    @Override
    public List<Symbol> getCenterSymbols() throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Corner card Faces do not have center symbols");
    }
    /**
     * @throws UnsupportedOperationException because basic corner card faces do not have placement requirements
     */
    @Override
    public Map<Symbol, Integer> getPlacementRequirements() throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Corner card Faces do not have placement requirements");
    }
    /**
     * @throws UnsupportedOperationException because basic corner card faces do not have a kingdom
     */
    @Override
    public Symbol getKingdom() throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Corner card Faces do not have kingdom symbols");
    }

    /**
     * Gets a string representation of the card face (as a starting back face)
     * @return the string representation
     */
    public String toString(){
        String boundaries = "-".repeat(32);
        boundaries += "\n";
        String upper = String.format("|%-9s|%10s|%9s|\n", getCornerSymbols().get(CardCorners.TOP_LEFT), " ", getCornerSymbols().get(CardCorners.TOP_RIGHT));
        String middle = String.format("|%30s|\n", " ");
        String lower = String.format("|%-9s|%10s|%9s|\n", getCornerSymbols().get(CardCorners.BOTTOM_LEFT), " ", getCornerSymbols().get(CardCorners.BOTTOM_RIGHT));
        return boundaries + upper + middle + lower + boundaries;
    }
}