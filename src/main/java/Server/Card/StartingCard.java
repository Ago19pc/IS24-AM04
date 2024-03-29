package main.java.Server.Card;

import main.java.Server.Enums.Face;

public class StartingCard implements Card {
    private final StartingFrontFace frontFace;
    private final CornerCardFace backFace;

    /**
     * Constructor for the StartingCard
     * @param frontFace the front face
     * @param backFace the back face
     */
    public StartingCard(StartingFrontFace frontFace, CornerCardFace backFace) {
        this.frontFace = frontFace;
        this.backFace = backFace;
    }

    /**
     * Returns the face of the card
     * @param face to return
     * @return CardFace the face of the card
     */
    public CardFace getFace(Face face) {
        return face == Face.FRONT ? frontFace : backFace;
    }

    /**
     * Returns the corner face of the card
     * @param face to return
     * @return CornerCardFace the face of the card
     */
    public CornerCardFace getCornerFace(Face face) {
        return face == Face.FRONT ? frontFace : backFace;
    }
}
