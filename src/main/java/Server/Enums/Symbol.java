package Server.Enums;

/**
 * Contains all the symbols found in cards
 */
public enum Symbol {
    /**
     * The animal kingdom symbol
     */
    ANIMAL,
    /**
     * The fungus kingdom symbol
     */
    FUNGUS,
    /**
     * The plant kingdom symbol
     */
    PLANT,
    /**
     * The bug kingdom symbol
     */
    BUG,
    /**
     * The quill object symbol
     */
    QUILL,
    /**
     * The bottle object symbol
     */
    BOTTLE,
    /**
     * The parchment object symbol
     */
    PARCHMENT,
    /**
     * The empty symbol
     */
    EMPTY,
    /**
     * No symbol
     */
    NONE,
    /**
     * The symbol for covered corners
     */
    COVERED_CORNER,
    /**
     * Pattern 1 done with fungi
     */
    PATTERN1F,
    /**
     * Pattern 1 done with animals
     */
    PATTERN1A,
    /**
     * Pattern 2 done with plants
     */
    PATTERN2P,
    /**
     * Pattern 2 done with bugs
     */
    PATTERN2B,
    /**
     * Pattern 3
     */
    PATTERN3,
    /**
     * Pattern 4
     */
    PATTERN4,
    /**
     * Pattern 5
     */
    PATTERN5,
    /**
     * Pattern 6
     */
    PATTERN6;

    /**
     * Checks if the symbol is a pattern
     * @return true if the symbol is a pattern, false otherwise
     */
    public boolean isPattern(){
        return this == PATTERN1F || this == PATTERN1A || this == PATTERN2P || this == PATTERN2B || this == PATTERN3 || this == PATTERN4 || this == PATTERN5 || this == PATTERN6;
    }

    /**
     * Returns a short string representation of the symbol
     * @return a short string representation of the symbol
     */
    public String toString(){
        return switch (this) {
            case ANIMAL -> "A";
            case FUNGUS -> "F";
            case PLANT -> "P";
            case BUG -> "B";
            case QUILL -> "Q";
            case BOTTLE -> "️BL";
            case PARCHMENT -> "PR";
            case EMPTY -> " ";
            case NONE -> "NO";
            case COVERED_CORNER -> "XX";
            case PATTERN1F -> "1F";
            case PATTERN1A -> "1A";
            case PATTERN2P -> "2P";
            case PATTERN2B -> "2B";
            case PATTERN3 -> "3";
            case PATTERN4 -> "4";
            case PATTERN5 -> "5";
            case PATTERN6 -> "6";
        };
    }

    /**
     * Returns a string representation of the symbol used in maps
     * @return a string representation of the symbol used in maps
     */
    public String toShortString(){
        return switch (this) {
            case ANIMAL -> "\uD83D\uDC3A";
            case FUNGUS -> "\uD83C\uDF44";
            case PLANT -> "\uD83C\uDF32";
            case BUG -> "\uD83E\uDD8B";
            case QUILL -> "\uD83E\uDEB6";
            case BOTTLE -> "⚗️";
            case PARCHMENT -> "\uD83D\uDCDC";
            case EMPTY -> " ";
            case NONE -> "NO";
            case COVERED_CORNER -> "XX";
            case PATTERN1F -> "1F";
            case PATTERN1A -> "1A";
            case PATTERN2P -> "2P";
            case PATTERN2B -> "2B";
            case PATTERN3 -> "3";
            case PATTERN4 -> "4";
            case PATTERN5 -> "5";
            case PATTERN6 -> "6";
        };
    }
}
