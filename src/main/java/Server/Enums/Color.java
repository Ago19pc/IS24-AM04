package Server.Enums;

/**
 * Contains all the colors of the players

 */
public enum Color {
    /**
     * Red color for the player
     */
    RED,
    /**
     * Yellow color for the player
     */
    YELLOW,
    /**
     * Blue color for the player
     */
    BLUE,
    /**
     * Green color for the player
     */
    GREEN;

    /**
     * Returns the string representation of the color
     * @return the string representation of the color
     */
    public String toString() {
        return switch (this) {
            case RED -> "Rosso";
            case YELLOW -> "Giallo";
            case BLUE -> "Blu";
            case GREEN -> "Verde";
        };
    }
}
