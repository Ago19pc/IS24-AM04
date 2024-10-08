package Server.Card;

import Server.Enums.Face;

import java.io.Serializable;

/**
 * This class represents starting cards. They are composed of a StartingFrontFace and a CornerCardFace
 * @see StartingFrontFace
 * @see CornerCardFace
 */
public class StartingCard implements Card, Serializable {
    /**
     * The front face of the card
     */
    private final StartingFrontFace frontFace;
    /**
     * The back face of the card
     */
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
        return "Carta iniziale: \n Faccia anteriore: \n" + frontFace.toString() + "\n Faccia posteriore: \n" + backFace.toString();
    }

}
