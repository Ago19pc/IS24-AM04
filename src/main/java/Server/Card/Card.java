package Server.Card;

import Server.Enums.Face;

import java.io.Serializable;

/**
 * This is the interface for handling the cards in the game.
*/
public interface Card extends Serializable {
    /**
     *
     * @param face Enum face, used to select witch face to get
     * @return CardFace the face of the card
     */
    CardFace getFace(Face face);

    CornerCardFace getCornerFace(Face face) throws UnsupportedOperationException;

    String getImageURI();
}
