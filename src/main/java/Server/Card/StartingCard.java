package main.java.Server.Card;

import main.java.Server.Enums.Face;

public class StartingCard implements Card {
    private final StartingFrontFace frontFace;
    private final RegularBackFace backFace;

    /**
     * Constructor for the StartingCard
     * @param frontFace the front face
     * @param backFace the back face
     */
    public StartingCard(StartingFrontFace frontFace, RegularBackFace backFace) {
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
}
