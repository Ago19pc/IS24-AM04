package Server.Card;

import Server.Enums.CardCorners;
import Server.Enums.Symbol;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * This is the interface for handling the faces of the cards in the game.
 */
public interface CardFace extends Serializable {
    /**
     * Returns the URI of the image
     * @return String the URI of the image
     */
    String getImageURI();

    /**
     * Returns the corner symbols
     * @return the corner symbols as a map of CardCorners and Symbol
     */
    Map<CardCorners, Symbol> getCornerSymbols();
    /**
     * Returns the center symbols
     * @return the center symbols as a list of Symbol
     */
    List<Symbol> getCenterSymbols();

    /**
     * Returns the obtainable score
     * @return the score
     */
    int getScore();
   /**
     * Returns the score requirements.
     * @return the score requirements as a map of Symbol and Integer. For achievement cards There are either 1 or 3 symbols in the map keySet. If there are 3 symbols, the player gets 3 points for every set of those 3 symbols. If there is 1 symbol, if the symbol is a pattern, the player gets the map's value in points for every pattern of that type. If the symbol is a resource, the player gets 2 points for every set of the number corresponding to the map's value of resource. For gold cards, the player gets instead tbe number indicated in score of points for each of the symbol specified in the only key in the map.
     */
    Map<Symbol, Integer> getScoreRequirements();
    /**
     * Returns the placement requirements
     * @return the placement requirements as a map of Symbol and Integer. The map contains the requirements for the placement of the card. The keySet contains the symbols that are required for the placement of the card. The value is the number of symbols required.
     */
    Map<Symbol, Integer> getPlacementRequirements();

    /**
     * Returns the x coordinate of the CardFace
     * @return int the x coordinate where the face is placed. If the face is not placed, it returns 0.
     */
     int getXCoord();
    /**
     * Returns the y coordinate of the CardFace
     * @return int the y coordinate where the face is placed. If the face is not placed, it returns 0.
     */
     int getYCoord();
    /**
     * sets the turn in which the card was placed
     * @param placementTurn the placement turn of the card.
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
     * Returns the placement turn of card. If the card is not placed, it returns 0.
     * @return the placement turn
     */
     int getPlacementTurn();

    /**
     * Returns the kingdom of the card
     * @return the kingdom symbol
     */
    Symbol getKingdom();
}
