package Server.Card;

import Server.Enums.Face;

import java.io.Serializable;

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
    public CornerCardFace getCornerFace(Face face) throws UnsupportedOperationException{
        return face == Face.FRONT ? frontFace : backFace;
    }

    public String toString(){
        return "Carta iniziale: \n Faccia anteriore: " + frontFace.toString() + "\n Faccia posteriore: " + backFace.toString();
    }

    public String getImageURI() {
        return imageURI;
    }

}
