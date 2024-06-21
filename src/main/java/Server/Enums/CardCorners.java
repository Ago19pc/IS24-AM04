package Server.Enums;

/**
 * Contains all the corners of a card
 */
public enum CardCorners {
    /**
     * Top left corner
     */
    TOP_LEFT,
    /**
     * Top right corner
     */
    TOP_RIGHT,
    /**
     * Bottom right corner
     */
    BOTTOM_RIGHT,
    /**
     * Bottom left corner
     */
    BOTTOM_LEFT;

    /**
     * Returns the opposite corner of the card
     * @return the opposite corner
     */
    public CardCorners getOppositeCorner() {
        return switch (this) {
            case TOP_LEFT -> BOTTOM_RIGHT;
            case TOP_RIGHT -> BOTTOM_LEFT;
            case BOTTOM_LEFT -> TOP_RIGHT;
            case BOTTOM_RIGHT -> TOP_LEFT;
        };
    }

}
