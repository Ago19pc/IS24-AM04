package main.java.Deck;

import main.java.Enums.DeckPosition;
import static main.java.Enums.DeckPosition.*;
import main.java.Card.Card;
import main.java.Exception.IncorrectDeckPositionException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;



public abstract  class Deck implements Deckable {
    private List<Card> cards;
    private Map<DeckPosition, Card> boardCards;

    public Deck(){
        System.out.println(this.toString() + "Deck");
        this.createCards();
    }

    /**
     * Shuffle the deck
     */
    private void shuffle() {
        // shuffle the deck
        Collections.shuffle(cards);
        System.out.println(this.toString() + "shuffle");
    }

    /**
     * @param where_to the position to add the card to
     */
    private void moveCardToBoard(DeckPosition where_to) throws IncorrectDeckPositionException {
        if (where_to == DECK)
            throw new IncorrectDeckPositionException("Cannot add card to the deck, only to FIST_CARD or SECOND_CARD.");
        else
            System.out.println(this.toString() + "moveCardToBoard");
    }

    /**
     * generate the cards
     */
    private void createCards() {
        System.out.println(this.toString() + " createCards");
    }

    /**
     * @return Card the card from the position
     */
    public Map<DeckPosition, Card> getBoardCard() {
        return boardCards;
    }

    /**
     * @param position the position pop the card from
     * @return Card the popped card
     */
    public Card popCard(DeckPosition position) {
        return switch (position) {
            case DECK -> cards.remove(0);
            case FIRST_CARD -> boardCards.remove(FIRST_CARD);
            case SECOND_CARD -> boardCards.remove(SECOND_CARD);
        };
    }

    /**
     * @return boolean true if the deck is empty
     */
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    /**
     * Add card to the board in the specified position
     * @param card card to add
     * @param position position to add the card to
     */
    public void addCard(Card card, DeckPosition position) throws IncorrectDeckPositionException {
        if (position == DECK)
            throw new IncorrectDeckPositionException("Cannot add card to the deck, only to FIST_CARD or SECOND_CARD.");
        else
            boardCards.put(position, card);
    }
}
