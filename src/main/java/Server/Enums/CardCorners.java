package Server.Enums;

/**
 * Contains all the corners of a card
 */
public enum CardCorners {
    TOP_LEFT, TOP_RIGHT, BOTTOM_RIGHT, BOTTOM_LEFT;

    public CardCorners getOppositeCorner() {
        return switch (this) {
            case TOP_LEFT -> BOTTOM_RIGHT;
            case TOP_RIGHT -> BOTTOM_LEFT;
            case BOTTOM_LEFT -> TOP_RIGHT;
            case BOTTOM_RIGHT -> TOP_LEFT;
        };
    }

}
