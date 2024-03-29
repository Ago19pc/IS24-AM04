package main.java.Server.Card;

import main.java.Server.Enums.Face;

public class GoldCard implements Card {
    private final GoldFrontFace frontFace;
    private final RegularBackFace backFace;

    /**
     * Constructor for the GoldCard
     * @param frontFace the front face
     * @param backFace the back face
     */
    public GoldCard(GoldFrontFace frontFace, RegularBackFace backFace) {
        this.frontFace = frontFace;
        this.backFace = backFace;
    }

    /**
     * Returns the face of the card
     * @param face to return
     * @return CardFace the face of the card
     */
    public CardFace getFace(Face face){
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
