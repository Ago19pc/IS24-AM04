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
    private final ResourceFrontFace frontFace;
    /**
     * The back face of the card
     */
    private final RegularBackFace backFace;
    /**
     * The URI of the image of the card
     */
    private final String imageURI;

    /**
     * Constructor for the ResourceCard
     * @param frontFace the front face
     * @param backFace the back face
     * @param imageURI the image URI
     */
    public ResourceCard(ResourceFrontFace frontFace, RegularBackFace backFace, String imageURI) {
        this.frontFace = frontFace;
        this.backFace = backFace;
        this.imageURI = imageURI;
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

    public String getImageURI() {
        return imageURI;
    }

}