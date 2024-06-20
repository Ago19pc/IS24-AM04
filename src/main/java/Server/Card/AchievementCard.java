package Server.Card;

import Server.Enums.Face;

import java.util.Map;

/**
 * This class represents achievement cards. They are composed of an AchievementFrontFace and an EmptyCardFace
 * @see AchievementFrontFace
 * @see EmptyCardFace
 */
public class AchievementCard implements Card {
    private final AchievementFrontFace frontFace;
    private final EmptyCardFace backFace;

    private final String imageURI;


    /**
     * Constructor for the AchievementCard
     * @param frontFace the front face
     * @param backFace the back face
     * @param imageURI the image URI
     */
    public AchievementCard(AchievementFrontFace frontFace, EmptyCardFace backFace, String imageURI) {
        this.frontFace = frontFace;
        this.backFace = backFace;
        this.imageURI = imageURI;
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

    public String getImageURI() {
        return imageURI;
    }

}
