package Server.Enums;

/**
 * Contains the faces of a card
 */
public enum Face {
    /**
     * The front face of the card
     */
    FRONT,
    /**
     * The back face of the card
     */
    BACK;

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
