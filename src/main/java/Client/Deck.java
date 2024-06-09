package Client;

import Server.Card.Card;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Deck<T> implements Serializable {
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

    public Deck() {
        this.deckSize = 38;
        this.boardCards = new ArrayList<>();
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
        for (Card boardCard : boardCards) {
            this.boardCards.add((T) boardCard);
        }
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
