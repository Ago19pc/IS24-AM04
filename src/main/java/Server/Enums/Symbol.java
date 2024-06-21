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
     * Gets a text representation of the symbol
     * @return a char with the text representation of the symbol
     */
    public char toChar(){
        return switch (this) {
            case ANIMAL -> 'A';
            case FUNGUS -> 'F';
            case PLANT -> 'P';
            case BUG -> 'B';
            case QUILL -> 'Q';
            case BOTTLE -> 'O';
            case PARCHMENT -> 'C';
            case EMPTY -> 'E';
            case NONE -> 'N';
            case COVERED_CORNER -> 'X';
            case PATTERN1F -> '1';
            case PATTERN1A -> '2';
            case PATTERN2P -> '3';
            case PATTERN2B -> '4';
            case PATTERN3 -> '5';
            case PATTERN4 -> '6';
            case PATTERN5 -> '7';
            case PATTERN6 -> '8';
            default -> ' ';
        };
    }
}
