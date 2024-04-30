package Client.Controller;

import Server.Card.CardFace;
import Server.Card.CornerCardFace;

public class PossibleCardPlacementSave {
    private final int xCoord, yCoord;
    private final CornerCardFace cardFace;

    public PossibleCardPlacementSave(int xCoord, int yCoord, CornerCardFace cardFace) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.cardFace = cardFace;
    }

    public int getX() {
        return xCoord;
    }

    public int getY() {
        return yCoord;
    }

    public CornerCardFace getCardFace() {
        return cardFace;
    }
}
