package Server.Enums;

/**
 * Contains all the symbols found in cards
 */
public enum Symbol {
    ANIMAL, FUNGUS, PLANT, BUG,
    QUILL, BOTTLE, PARCHMENT,
    EMPTY, NONE, COVERED_CORNER,
    PATTERN1F, PATTERN1A, PATTERN2P, PATTERN2B, PATTERN3, PATTERN4, PATTERN5, PATTERN6;

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
