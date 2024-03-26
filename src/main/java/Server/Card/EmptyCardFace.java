package main.java.Server.Card;

public class EmptyCardFace implements CardFace {
    private final String imageURI;

    /**
     * Constructor for the EmptyCardFace
     * @param imageURI the URI of the image
     */
    public EmptyCardFace(String imageURI) {
        this.imageURI = imageURI;
    }

    /**
     * @return String the URI of the image
     */
    @Override
    public String getImageURI() {
        return imageURI;
    }
}
