package Server.Enums;

public enum Symbol {
    ANIMAL, FUNGUS, PLANT, BUG,
    QUILL, BOTTLE, PARCHMENT,
    EMPTY, NONE, COVERED_CORNER,
    PATTERN1F, PATTERN1A, PATTERN2P, PATTERN2B, PATTERN3, PATTERN4, PATTERN5, PATTERN6;

    public boolean isPattern(){
        return this == PATTERN1F || this == PATTERN1A || this == PATTERN2P || this == PATTERN2B || this == PATTERN3 || this == PATTERN4 || this == PATTERN5 || this == PATTERN6;
    }

    public char toChar(){
        switch(this){
            case ANIMAL:
                return 'A';
            case FUNGUS:
                return 'F';
            case PLANT:
                return 'P';
            case BUG:
                return 'B';
            case QUILL:
                return 'Q';
            case BOTTLE:
                return 'O';
            case PARCHMENT:
                return 'C';
            case EMPTY:
                return 'E';
            case NONE:
                return 'N';
            case COVERED_CORNER:
                return 'X';
            case PATTERN1F:
                return '1';
            case PATTERN1A:
                return '2';
            case PATTERN2P:
                return '3';
            case PATTERN2B:
                return '4';
            case PATTERN3:
                return '5';
            case PATTERN4:
                return '6';
            case PATTERN5:
                return '7';
            case PATTERN6:
                return '8';
            default:
                return ' ';
        }
    }
}
