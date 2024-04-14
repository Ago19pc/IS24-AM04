package Server.Enums;

public enum Symbol {
    ANIMAL, FUNGUS, PLANT, BUG,
    QUILL, BOTTLE, PARCHMENT,
    EMPTY, NONE, COVERED_CORNER,
    PATTERN1F, PATTERN1A, PATTERN2P, PATTERN2B, PATTERN3, PATTERN4, PATTERN5, PATTERN6, PATTERN7, PATTERN8;

    public boolean isPattern(){
        return this == PATTERN1F || this == PATTERN1A || this == PATTERN2P || this == PATTERN2B || this == PATTERN3 || this == PATTERN4 || this == PATTERN5 || this == PATTERN6 || this == PATTERN7 || this == PATTERN8;
    }
}
