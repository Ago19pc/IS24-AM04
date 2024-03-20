package main.java.Card;

import main.java.Enums.Face;

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
}
