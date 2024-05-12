package Client;

import Server.Card.Card;

import java.util.List;

public class Deck {
    private int deckSize;
    private List<Card> boardCards;

    public Deck(List<Card> boardCards) {
        this.deckSize = 38;
        this.boardCards = boardCards;
    }

    /**
     * Get the board cards
     * @return List of Boardcards
     */
    public List<Card> getBoardCards() {
        return boardCards;
    }

    /**
     * Set the board cards
     * @param boardCards List of Boardcards
     */
    public void setBoardCards(List<Card> boardCards) {
        this.boardCards = boardCards;
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
