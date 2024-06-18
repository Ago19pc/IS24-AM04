package Client;

import Server.Card.Card;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the deck of cards as viewed by the client. It is different from the deck of cards on the server.
 * @see Server.Deck
 * @param <T> the type of cards
 */
public class Deck<T> implements Serializable {
    /**
     * The remaining cards in the deck, excluding the board cards but not the top card
     */
    private int deckSize;
    /**
     * List of Boardcards, containing the top card of the deck, the first board card and the second board card
     */
    private List<T> boardCards;

    /**
     * Constructor using boardCards
     * @param boardCards List of Boardcards
     */
    public Deck(List<T> boardCards) {
        this.deckSize = 38;
        this.boardCards = boardCards;
    }

    /**
     * Constructor using boardCards and custom deck size. Used for saved games and reconnecting to a game.
     * @param deckSize the deck size
     * @param boardCards List of Boardcards
     */
    public Deck(int deckSize, List<T> boardCards) {
        this.deckSize = deckSize;
        this.boardCards = boardCards;
    }

    /**
     * Constructor that initializes the deck with an empty list of board cards
     */
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
