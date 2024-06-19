package Server.Enums;

/**
 * Contains all the colors of the players

 */
public enum Color {
    RED, YELLOW, BLUE, GREEN;

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
