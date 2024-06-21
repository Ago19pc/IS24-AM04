package Server.Card;

import Server.Enums.CardCorners;
import Server.Enums.Symbol;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * This is the class from which all card faces inherit. It is also used to represent achievement cards' back faces
 */
public class EmptyCardFace implements CardFace, Serializable {
    /**
     * The URI of the image of the card
     */
    private final String imageURI;

    /**
     * Constructor for the EmptyCardFace
     * @param imageURI the URI of the image
     */
    public EmptyCardFace(String imageURI) {
        this.imageURI = imageURI;
    }


    @Override
    public String getImageURI() {
        return imageURI;
    }

    /**
     * @throws UnsupportedOperationException because empty card faces do not have scores
     */
    public int getScore() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Empty cards do not have scores");
    }
    /**
     * @throws UnsupportedOperationException because empty card faces do not have corner symbols
     */
    public Map<CardCorners, Symbol> getCornerSymbols() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Empty cards do not have corner symbols");
    }
    /**
     * @throws UnsupportedOperationException because empty card faces do not have center symbols
     */
    public List<Symbol> getCenterSymbols() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Empty cards do not have center symbols");
    }
    /**
     * @throws UnsupportedOperationException because empty card faces do not have score requirements
     */
    public Map<Symbol, Integer> getScoreRequirements() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Empty cards do not have score requirements");
    }
    /**
     * @throws UnsupportedOperationException because empty card faces do not have placement requirements
     */
    public Map<Symbol, Integer> getPlacementRequirements() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Empty cards do not have placement requirements");
    }

    /**
     * @throws UnsupportedOperationException because empty card faces do not have coordinates
     */
    @Override
    public int getXCoord() throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Empty cards do not have coordinates");
    }
    /**
     * @throws UnsupportedOperationException because empty card faces do not have coordinates
     */
    @Override
    public int getYCoord() throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Empty cards do not have coordinates");
    }

    /**
        * @throws UnsupportedOperationException because empty card faces do not have placement turns
     */
    @Override
    public void setPlacementTurn(int placementTurn) throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Empty cards do not have placement turns");

    }

    /**
     * @throws UnsupportedOperationException because empty card faces do not have coordinates
     */
    @Override
    public void setXCoord(int xCoord) throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Empty cards do not have coordinates");

    }

    /**
     * @throws UnsupportedOperationException because empty card faces do not have coordinates
     */
    @Override
    public void setYCoord(int yCoord) throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Empty cards do not have coordinates");

    }
    /**
     * @throws UnsupportedOperationException because empty card faces do not have placement turns
     */
    public int getPlacementTurn() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Empty cards do not have placement turns");
    }
    /**
     * @throws UnsupportedOperationException because empty card faces do not have a kingdom
     */
    public Symbol getKingdom() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Empty cards do not have kingdoms");
    }
}




