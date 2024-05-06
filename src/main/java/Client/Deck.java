package Client;

import Server.Card.Card;

import java.util.ArrayList;
import java.util.List;

public class Deck {
    private int deckSize;
    private List<Card> boardCards;

    public Deck(List<Card> boardCards) {
        this.deckSize = 38;
        this.boardCards = boardCards;
    }

    public List<Card> getBoardCards() {
        return boardCards;
    }

    public void setBoardCards(List<Card> boardCards) {
        this.boardCards = boardCards;
    }

    public int getDeckSize() {
        return deckSize;
    }

    public void setDeckSize(int deckSize) {
        this.deckSize = deckSize;
    }
}
