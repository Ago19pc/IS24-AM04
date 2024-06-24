package Server.Card;

import Server.Enums.Face;

import java.util.Map;

/**
 * This class represents achievement cards. They are composed of an AchievementFrontFace and an EmptyCardFace
 * @see AchievementFrontFace
 * @see EmptyCardFace
 */
public class AchievementCard implements Card {
    /**
     * The front face of the card. It contains the score and the requirements to obtain it
     */
    private final AchievementFrontFace frontFace;
    /**
     * The back face of the card. It's empty
     */
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

    public CardFace getFace(Face face) {
        return face == Face.FRONT ? frontFace : backFace;
    }

    /**
     * @throws UnsupportedOperationException because achievement cards do not have corner faces
     */
    public CornerCardFace getCornerFace(Face face) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Achievement cards do not have corner faces");
    }

    /**
     * Returns a string representation of the achievement card
     * @return a string representation of the achievement card
     */
    public String toString(){
        return "Puoi ottenere " + frontFace.getScore() + " punti completando i seguenti requisiti: " + frontFace.scoreReqForPrint();
    }

}
