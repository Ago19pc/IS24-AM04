package Server.Card;


import Server.Enums.Face;

public class GoldCard extends ResourceCard implements Card{

    /**
     * Constructor for the GoldCard
     * @param frontFace the front face
     * @param backFace the back face
     */
    public GoldCard(GoldFrontFace frontFace, RegularBackFace backFace, String imageURI) {
        super(frontFace, backFace, imageURI);
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
        return "Carta oro. Faccia anteriore: \n" + frontFace.toString() + "\nFaccia posteriore: \n" + backFace.toString();
    }

}
