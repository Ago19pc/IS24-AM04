package main.java.Server.Card;

import main.java.Server.Enums.Face;
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

    /**
     * Returns the corner face of the card
     * @param face to return
     * @return CornerCardFace the face of the card
     */
    public CornerCardFace getCornerFace(Face face) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Achievement cards do not have corner faces");
    }
}
