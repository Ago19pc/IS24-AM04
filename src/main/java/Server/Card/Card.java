package Server.Card;

import Server.Enums.Decks;
import Server.Enums.Face;

/**
 * This is the interface for handling the cards in the game.
*/
public interface Card {
    /**
     *
     * @param face Enum face, used to select witch face to get
     * @return CardFace the face of the card
     */
    public CardFace getFace(Face face);

    public CornerCardFace getCornerFace(Face face);

    /**
     * return the Decks' card
     * @return Decks the type of the card
     */

}
