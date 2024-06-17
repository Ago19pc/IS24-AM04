package Server.Deck;

import Server.Card.Card;
import Server.Enums.DeckPosition;
import Server.Exception.AlreadyFinishedException;
import Server.Exception.IncorrectDeckPositionException;

import java.util.Map;

/**
 * Interface for the decks
 */
public interface Deckable {

    /**
     * Removes a card from the deck and returns it
     * @param position the position where to remove the card from
     * @see DeckPosition
     * @return the card from the position
     * @throws AlreadyFinishedException if the deck is empty in the selected position
     */
    public Card popCard(DeckPosition position) throws AlreadyFinishedException;

    /**
     * Checks if the deck is empty
     * @return true if the deck is empty in all of its positions
     */
    boolean isEmpty();

    /**
     * Add card to the board in the specified position
     * @param card card to add
     * @param position position to add the card to
     * @throws IncorrectDeckPositionException if the position is not in the board (i.e. the deck itself)
     * @see DeckPosition
     */
     public void addCard(Card card, DeckPosition position) throws IncorrectDeckPositionException;

    /**
     * Returns the card on top of the deck without removing it
     * @return the card on top of the deck
     */
     public Card getTopCardNoPop();
}
