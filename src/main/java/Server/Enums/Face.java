package Server.Enums;

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
