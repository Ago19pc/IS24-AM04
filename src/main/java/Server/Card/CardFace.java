package Server.Card;

import Server.Enums.CardCorners;
import Server.Enums.Symbol;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface CardFace extends Serializable {
    /**
     * @return String the URI of the image
     */
    String getImageURI();

    /**
     * Returns the corner symbols
     * @return Map<Integer, Symbol> the corner symbols
     */
    Map<CardCorners, Symbol> getCornerSymbols();
    /**
     * Returns the center symbols
     * @return List<Symbol> the center symbols
     */
    List<Symbol> getCenterSymbols();

    /**
     * Returns the obtainable score
     * @return int the score
     */
    int getScore();
   /**
     * Returns the score requirements
     * @return Map<Symbol, Integer> the score requirements for each symbol
     */
    Map<Symbol, Integer> getScoreRequirements();
    /**
     * Returns the placement requirements
     * @return Map<Symbol, Integer> the placement requirements for each symbol
     */
    Map<Symbol, Integer> getPlacementRequirements();

    /**
     * Returns the x coordinate of the CardFace
     * @return int the x coordinate
     */
     int getXCoord();
    /**
     * Returns the y coordinate of the CardFace
     * @return int the y coordinate
     */
     int getYCoord();
    /**
     * sets the turn in which the card was placed
     * @param placementTurn the placement turn
     */
     void setPlacementTurn(int placementTurn);
    /**
     * sets the x coordinate of the CardFace
     * @param xCoord the x coordinate
     */
     void setXCoord(int xCoord);
    /**
     * sets the y coordinate of the CardFace
     * @param yCoord the y coordinate
     */
     void setYCoord(int yCoord);
    /**
     * Returns the placement turn of card to know what card is on top
     * @return int the placement turn
     */
     int getPlacementTurn();

    /**
     * Returns the kingdom of the card
     * @return Symbol the kingdom symbol
     */
    Symbol getKingdom();
}
