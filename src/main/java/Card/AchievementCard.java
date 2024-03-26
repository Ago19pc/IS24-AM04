package main.java.Card;

import main.java.Enums.Face;
public class AchievementCard implements Card {
    private final AchievementFrontFace frontFace;
    private final EmptyCardFace backFace;

    /**
     * Constructor for the AchievementCard
     * @param frontFace the front face
     * @param backFace the back face
     */
    public AchievementCard(AchievementFrontFace frontFace, EmptyCardFace backFace) {
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
