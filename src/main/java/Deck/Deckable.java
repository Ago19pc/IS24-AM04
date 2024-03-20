package main.java.Deck;

import main.java.Card.Card;
import main.java.Enums.DeckPosition;

import java.util.Map;

public interface Deckable {
    /**
     * @param card a card from the board
     * @param position the position to add the card to
     */
    public Map<DeckPosition, Card> getBoardCard();


    /**
     * @param position the position pop the card from
     * @return Card the card from the position
     */
    public Card popCard(DeckPosition position);

    /**
     * @return boolean true if the deck is empty
     */
    boolean isEmpty();

}
