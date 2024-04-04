package Server.Enums;

public enum CardCorners {
    TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT;

    public CardCorners getOppositeCorner() {
        CardCorners opposite = null;
        switch(this) {
            case TOP_LEFT:
                opposite = BOTTOM_RIGHT;
            case TOP_RIGHT:
                opposite = BOTTOM_LEFT;
            case BOTTOM_LEFT:
                opposite = TOP_RIGHT;
            case BOTTOM_RIGHT:
                opposite = TOP_LEFT;
        }
        return opposite;
    }

}
