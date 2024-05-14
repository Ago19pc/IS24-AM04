package Server.Card;

import Server.Enums.CardCorners;
import Server.Enums.Symbol;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class EmptyCardFace implements CardFace, Serializable {
    private final String imageURI;

    /**
     * Constructor for the EmptyCardFace
     * @param imageURI the URI of the image
     */
    public EmptyCardFace(String imageURI) {
        this.imageURI = imageURI;
    }

    /**
     * @return String the URI of the image
     */
    @Override
    public String getImageURI() {
        return imageURI;
    }

    public int getScore() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Empty cards do not have scores");
    }

    public Map<CardCorners, Symbol> getCornerSymbols() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Empty cards do not have corner symbols");
    }

    public List<Symbol> getCenterSymbols() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Empty cards do not have center symbols");
    }

    public Map<Symbol, Integer> getScoreRequirements() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Empty cards do not have score requirements");
    }

    public Map<Symbol, Integer> getPlacementRequirements() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Empty cards do not have placement requirements");
    }

    /**
     * Returns the x coordinate of the CardFace
     *
     * @return int the x coordinate
     */
    @Override
    public int getXCoord() throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Empty cards do not have coordinates");
    }
    /**
     * Returns the y coordinate of the CardFace
     *
     * @return int the y coordinate
     */
    @Override
    public int getYCoord() throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Empty cards do not have coordinates");
    }

    /**
     * sets the turn in which the card was placed
     *
     * @param placementTurn the placement turn
     */
    @Override
    public void setPlacementTurn(int placementTurn) throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Empty cards do not have placement turns");

    }

    /**
     * sets the x coordinate of the CardFace
     *
     * @param xCoord the x coordinate
     */
    @Override
    public void setXCoord(int xCoord) throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Empty cards do not have coordinates");

    }

    /**
     * sets the y coordinate of the CardFace
     *
     * @param yCoord the y coordinate
     */
    @Override
    public void setYCoord(int yCoord) throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Empty cards do not have coordinates");

    }

    public int getPlacementTurn() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Empty cards do not have placement turns");
    }

    public Symbol getKingdom() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Empty cards do not have kingdoms");
    }
}




