package Server.Card;

import Server.Enums.Face;

/**
 * This class represents resource cards. They are composed of a ResourceFrontFace and a RegularBackFace
 * @see ResourceFrontFace
 * @see RegularBackFace
 */
public class ResourceCard implements Card {
    /**
     * The front face of the card
     */
    final ResourceFrontFace frontFace;
    /**
     * The back face of the card
     */
    final RegularBackFace backFace;

    /**
     * Constructor for the ResourceCard
     * @param frontFace the front face
     * @param backFace the back face
     */
    public ResourceCard(ResourceFrontFace frontFace, RegularBackFace backFace) {
        this.frontFace = frontFace;
        this.backFace = backFace;
    }


    public CardFace getFace(Face face){
        return face == Face.FRONT ? frontFace : backFace;
    }

    public CornerCardFace getCornerFace(Face face) throws UnsupportedOperationException{
        return face == Face.FRONT ? frontFace : backFace;
    }

    /**
     * Returns a string representation of the resource card
     * @return a string representation of the resource card
     */
    public String toString(){
        return "Carta risorsa. Faccia anteriore: \n" + frontFace.toString() + "\nFaccia posteriore: \n" + backFace.toString();
    }
}