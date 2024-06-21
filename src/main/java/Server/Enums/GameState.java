package Server.Enums;

/**
 * Contains all the possible states of the game
 */
public enum GameState {
    /**
     * Lobby, where players can join the game, set their color and ready status.
     */
    LOBBY,
    /**
     * Phase where players choose their starting card.
     */
    CHOOSE_STARTING_CARD,
    /**
     * Phase where players choose their secret achievement.
     */
    CHOOSE_SECRET_ACHIEVEMENT,
    /**
     * Phase where a player can place a card.
     */
    PLACE_CARD,
    /**
     * Phase where a player can draw a card.
     */
    DRAW_CARD,
    /**
     * Phase where the game has ended and the leaderboard is shown.
     */
    LEADERBOARD,
    /**
     * Lobby for loaded games. People can join the game and then it starts
     */
    LOAD_GAME_LOBBY
}
