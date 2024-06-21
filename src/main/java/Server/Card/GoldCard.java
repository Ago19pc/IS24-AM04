package Server.Card;


import Server.Enums.Face;

/**
 * This class represents gold cards. They are composed of a GoldFrontFace and a RegularBackFace
 * @see GoldFrontFace
 * @see RegularBackFace
 */
public class GoldCard extends ResourceCard implements Card{

    /**
     * Constructor for the GoldCard
     * @param frontFace the front face
     * @param backFace the back face
     * @param imageURI the image URI
     */
    public GoldCard(GoldFrontFace frontFace, RegularBackFace backFace, String imageURI) {
        super(frontFace, backFace, imageURI);
    }

    public CardFace getFace(Face face){
        return face == Face.FRONT ? frontFace : backFace;
    }

    public CornerCardFace getCornerFace(Face face) throws UnsupportedOperationException{
        return face == Face.FRONT ? frontFace : backFace;
    }

    /**
     * Returns a string representation of the gold card
     * @return a string representation of the gold card
     */
    public String toString(){
        return "Carta oro. Faccia anteriore: \n" + frontFace.toString() + "\nFaccia posteriore: \n" + backFace.toString();
    }

}
