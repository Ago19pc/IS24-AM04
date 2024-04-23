package Server.Deck;

import Server.Card.Card;
import Server.Enums.DeckPosition;
import Server.Exception.AlreadyFinishedException;
import Server.Exception.IncorrectDeckPositionException;

import java.util.Map;

public interface Deckable {
    /**
     * @return Map<DeckPosition, Card> the cards on the board
     */
    public Map<DeckPosition, Card> getBoardCard();


    /**
     * @param position the position pop the card from
     * @return Card the card from the position
     */
    public Card popCard(DeckPosition position) throws IncorrectDeckPositionException, AlreadyFinishedException;

    /**
     * @return boolean true if the deck is empty
     */
    boolean isEmpty();

    /**
     * Add card to the board in the specified position
     * @param card card to add
     * @param position position to add the card to
     */
     public void addCard(Card card, DeckPosition position) throws IncorrectDeckPositionException;

}
