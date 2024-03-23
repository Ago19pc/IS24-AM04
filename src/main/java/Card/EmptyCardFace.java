package main.java.Card;

public class EmptyCardFace implements CardFace {
    private final String imageURI;
    private Card card;

    /**
     * Constructor for the EmptyCardFace
     * @param imageURI the URI of the image
     * @param card the card that owns the face
     */
    public EmptyCardFace(String imageURI, Card card) {
        this.imageURI = imageURI;
        this.card = card;
    }

    /**
     * @return String the URI of the image
     */
    @Override
    public String getImageURI() {
        return imageURI;
    }
    /**
     * @return Card the card that the face is on
     */
    @Override
    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }
}
