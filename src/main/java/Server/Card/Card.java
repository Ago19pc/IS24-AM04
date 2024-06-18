package Server.Card;

import Server.Enums.Face;

import java.io.Serializable;

/**
 * This is the interface for handling the cards in the game.
*/
public interface Card extends Serializable {
    /**
     * Returns a face of class CardFace of the card
     * @param face Enum face, used to select which face to get
     * @return CardFace the face of the card
     */
    CardFace getFace(Face face);
    /**
     * Returns a face of class CornerCardFace of the card
     * @param face Enum face, used to select which face to get
     * @return CardFace the face of the card
     * @throws UnsupportedOperationException if the card does not have corners
     */
    CornerCardFace getCornerFace(Face face) throws UnsupportedOperationException;

    /**
     * Returns the image URI of the card
     * @return the image URI
     */
    String getImageURI();
}
