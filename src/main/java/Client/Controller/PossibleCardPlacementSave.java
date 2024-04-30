package Client.Controller;

import Server.Card.CardFace;
import Server.Card.CornerCardFace;

public class PossibleCardPlacementSave {
    private final int xCoord, yCoord;
    private final CornerCardFace cardFace;

    /**
     * Constructor for the PossibleCardPlacementSave class, it saved the x and y coordinates of the card and the card face
     * @param xCoord
     * @param yCoord
     * @param cardFace
     */
    public PossibleCardPlacementSave(int xCoord, int yCoord, CornerCardFace cardFace) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.cardFace = cardFace;
    }

    /**
     * @return the xCoordinate of the placementMove
     */
    public int getX() {
        return xCoord;
    }

    /**
     * @return the yCoordinate of the placementMove
     */
    public int getY() {
        return yCoord;
    }

    /**
     * @return the cardFace of the placementMove
     */
    public CornerCardFace getCardFace() {
        return cardFace;
    }
}
