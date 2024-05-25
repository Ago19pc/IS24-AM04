package Client;

import Server.Card.Card;

import java.util.ArrayList;
import java.util.List;

public class Deck<T> {
    private int deckSize;
    private List<T> boardCards;

    public Deck(List<T> boardCards) {
        this.deckSize = 38;
        this.boardCards = boardCards;
    }

    public Deck(int deckSize, List<T> boardCards) {
        this.deckSize = deckSize;
        this.boardCards = boardCards;
    }

    /**
     * Get the board cards
     * @return List of Boardcards
     */
    public List<T> getBoardCards() {
        return boardCards;
    }

    /**
     * Set the board cards
     * @param boardCards List of Boardcards
     */
    public void setBoardCards(List<Card> boardCards) {
        this.boardCards = new ArrayList<>();
        boardCards.forEach(card -> this.boardCards.add((T) card));
    }

    /**
     * Get the deck size
     * @return deckSize
     */
    public int getDeckSize() {
        return deckSize;
    }

    /**
     * Set the deckSize
     * @param deckSize the deck size
     */
    public void setDeckSize(int deckSize) {
        this.deckSize = deckSize;
    }
}
