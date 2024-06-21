package Server.Enums;

/**
 * Contains all the actions that can be performed by the player after the lobby
 */
public enum Actions {
    /**
     * At least one player has to choose his secret achievement card
     */
    SECRET_ACHIEVEMENT_CHOICE,
    /**
     * At least one player has to choose his starting card
     */
    STARTING_CARD_CHOICE,
    /**
     * A player has to place a card
     */
    PLAY_CARD,
    /**
     * A player has to draw a card
     */
    DRAW_CARD
}
