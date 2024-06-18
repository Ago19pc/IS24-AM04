package Server.Enums;

/**
 * Contains the faces of a card
 */
public enum Face {
    FRONT, BACK;

    /**
     * Returns the opposite face of the card
     * @return the opposite face

     */
    public Face getOpposite() {
        if (this == FRONT) {
            return BACK;
        } else {
            return FRONT;
        }
    }
}
