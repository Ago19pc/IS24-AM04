package Server.Card;

import Server.Enums.Face;

import java.io.Serializable;

/**
 * This class represents starting cards. They are composed of a StartingFrontFace and a CornerCardFace
 * @see StartingFrontFace
 * @see CornerCardFace
 */
public class StartingCard implements Card, Serializable {
    private final StartingFrontFace frontFace;
    private final CornerCardFace backFace;

    private final String imageURI;

    /**
     * Constructor for the StartingCard
     * @param frontFace the front face
     * @param backFace the back face
     */
    public StartingCard(StartingFrontFace frontFace, CornerCardFace backFace, String imageURI) {
        this.frontFace = frontFace;
        this.backFace = backFace;
        this.imageURI = imageURI;
    }

    public CardFace getFace(Face face) {
        return face == Face.FRONT ? frontFace : backFace;
    }

    public CornerCardFace getCornerFace(Face face) throws UnsupportedOperationException{
        return face == Face.FRONT ? frontFace : backFace;
    }
    /**
     * Returns a string representation of the starting card
     * @return a string representation of the starting card
     */
    public String toString(){
        return "Carta iniziale: \n Faccia anteriore: " + frontFace.toString() + "\n Faccia posteriore: " + backFace.toString();
    }

    public String getImageURI() {
        return imageURI;
    }

}
