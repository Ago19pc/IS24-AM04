package main.java.Card;

public interface CardFace {
    /**
     * @return String the URI of the image
     */
    public String getImageURI();
    /**
     * @return Card the card that the face is on
     */
    public Card getCard();

    public void setCard(Card card);
}
