package Server.Messages;

import Client.Controller.ClientController;
import Client.Deck;
import Client.Player;
import Server.Card.AchievementCard;
import Server.Card.Card;
import Server.Card.GoldCard;
import Server.Card.ResourceCard;
import Server.Chat.Chat;
import Server.Controller.Controller;
import Server.Enums.GameState;
import Server.Exception.AlreadyStartedException;
import Server.Exception.PlayerNotFoundByNameException;
import Server.Exception.PlayerNotInAnyServerConnectionHandlerException;

import java.io.Serializable;
import java.util.List;

/**
 * Message to ask the server to join a saved game and to notify the client with the game data
 */
public class SavedGameMessage implements Serializable, ToServerMessage, ToClientMessage {
    private final String id;
    private final String name;

    private List<AchievementCard> commonAchievements;
    private Deck<GoldCard> goldDeck;
    private Deck<ResourceCard> resourceDeck;
    private AchievementCard secretAchievement;
    private List<Card> hand;
    private int turn;
    private List<Player> players;
    private Chat chat;
    private GameState gameState;

    /**
     * ToServer constructor
     * @param id the id of the client
     * @param name the name of the player the client wants to reconnect to
     */
    public SavedGameMessage(String id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * ToClient constructor
     * @param name the name of the player
     * @param commonAchievements the common achievements
     * @param goldDeck the gold deck
     * @param resourceDeck the resource deck
     * @param secretAchievement the secret achievement of the player
     * @param hand the hand of the player
     * @param turn the current game turn
     * @param players the list of players
     * @param chat the chat messages
     * @param gameState the current game state
     */
    public SavedGameMessage(String name, List<AchievementCard> commonAchievements, Deck<GoldCard> goldDeck, Deck<ResourceCard> resourceDeck, AchievementCard secretAchievement, List<Card> hand, int turn, List<Player> players, Chat chat, GameState gameState) {
        this.id = null;
        this.name = name;
        this.commonAchievements = commonAchievements;
        this.goldDeck = goldDeck;
        this.resourceDeck = resourceDeck;
        this.secretAchievement = secretAchievement;
        this.hand = hand;
        this.turn = turn;
        this.players = players;
        this.chat = chat;
        this.gameState = gameState;
    }

    /**
     * Adds the player to the saved game
     * @param controller the controller where the message will be executed
     */
    @Override
    public void serverExecute(Controller controller) {
        try {
            controller.addSavedPlayer(id, name);
        } catch (AlreadyStartedException e) {
            GameAlreadyStartedMessage message = new GameAlreadyStartedMessage();
            try{
                controller.getConnectionHandler().getServerConnectionHandler(id).sendMessage(message, id);
            } catch (Exception exception) {
                System.out.println("Player not found");
            }
        } catch (PlayerNotFoundByNameException e) {
            PlayerAlreadyPlayingMessage message = new PlayerAlreadyPlayingMessage();
            try{
                controller.getConnectionHandler().getServerConnectionHandler(id).sendMessage(message, id);
            } catch (Exception exception) {
                System.out.println("Player not found");
            }
        } catch (IllegalArgumentException e) {
            InvalidNameMessage message = new InvalidNameMessage();
            try{
                controller.getConnectionHandler().getServerConnectionHandler(id).sendMessage(message, id);
            } catch (Exception exception) {
                System.out.println("Player not found");
            }
        }
    }

    /**
     * Make the client load the game data
     * @param controller the controller where the message will be executed
     */
    @Override
    public void clientExecute(ClientController controller) {
        controller.loadGame(commonAchievements, goldDeck, resourceDeck, secretAchievement, hand, turn, players, chat, gameState, name);
    }
}
