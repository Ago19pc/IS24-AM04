package Server.Enums;

/**
 * Contains the positions of a deck. These can be the deck itself, the first board card or the second board card. In the case of the achievements deck, the board cards are the common achievements.
 */
public enum DeckPosition {
    /**
     * The deck itself, meaning the top card of the deck.
     */
    DECK,
    /**
     * The first board card.
     */
    FIRST_CARD,
    /**
     * The second board card.
     */
    SECOND_CARD
}
