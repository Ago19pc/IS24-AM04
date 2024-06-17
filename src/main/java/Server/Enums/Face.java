package Server.Enums;

/**
 * Contains the faces of a card
 */
public enum Face {
    FRONT, BACK;

    public Face getOpposite() {
        if (this == FRONT) {
            return BACK;
        } else {
            return FRONT;
        }
    }
}
