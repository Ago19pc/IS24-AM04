package Server.Card;

import Server.Enums.Face;

public class ResourceCard implements Card {
    protected final ResourceFrontFace frontFace;
    protected final RegularBackFace backFace;
    private final String imageURI;

    /**
     * Constructor for the ResourceCard
     * @param frontFace the front face
     * @param backFace the back face
     */
    public ResourceCard(ResourceFrontFace frontFace, RegularBackFace backFace, String imageURI) {
        this.frontFace = frontFace;
        this.backFace = backFace;
        this.imageURI = imageURI;
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
    public CornerCardFace getCornerFace(Face face) throws UnsupportedOperationException{
        return face == Face.FRONT ? frontFace : backFace;
    }

    public String toString(){
        return "Carta risorsa. Faccia anteriore: \n" + frontFace.toString() + "\nFaccia posteriore: \n" + backFace.toString();
    }

    public String getImageURI() {
        return imageURI;
    }

}