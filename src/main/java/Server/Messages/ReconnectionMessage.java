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
import Server.Exception.AlreadyFinishedException;
import Server.Exception.AlreadySetException;
import Server.Exception.NotYetStartedException;
import Server.Exception.PlayerNotInAnyServerConnectionHandlerException;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Message to ask the server to reconnect and to notify the reconnecting client with the game data
 */
public class ReconnectionMessage implements Serializable, ToClientMessage, ToServerMessage {
    private final String id;
    private String newId;
    private List<AchievementCard> commonAchievements;
    private Deck<GoldCard> goldDeck;
    private Deck<ResourceCard> resourceDeck;
    private String name;
    private AchievementCard secretAchievement;
    private List<Card> hand;
    private int turn;
    private List<Player> players;
    private Chat chat;
    private GameState gameState;

    /**
     * ToServer constructor
     * @param id the current id of the client
     * @param newId the id of the player the client wants to reconnect with
     */
    public ReconnectionMessage(String id, String newId){
        this.id = id;
        this.newId = newId;
    }

    /**
     * ToClient constructor
     * @param id the id of the player
     * @param commonAchievements the common achievements
     * @param goldDeck the gold deck
     * @param resourceDeck the resource deck
     * @param name the name of the player
     * @param secretAchievement the secret achievement of the player
     * @param hand the hand of the player
     * @param turn the current game turn
     * @param players the list of players
     * @param chat the chat messages
     * @param gameState the current game state
     */
    public ReconnectionMessage(String id, List<AchievementCard> commonAchievements, Deck<GoldCard> goldDeck, Deck<ResourceCard> resourceDeck, String name, AchievementCard secretAchievement, List<Card> hand, int turn, List<Player> players, Chat chat, GameState gameState) {
        this.id = id;
        this.commonAchievements = commonAchievements;
        this.goldDeck = goldDeck;
        this.resourceDeck = resourceDeck;
        this.name = name;
        this.secretAchievement = secretAchievement;
        this.hand = hand;
        this.turn = turn;
        this.players = players;
        this.chat = chat;
        this.gameState = gameState;
    }

    /**
     * Make the client load the game data
     * @param controller the controller where the message will be executed
     */
    @Override
    public void clientExecute(ClientController controller) {
        controller.setGameInfo(id, commonAchievements, goldDeck, resourceDeck, name, secretAchievement, hand, turn, players, chat, gameState);
    }

    /**
     * Asks the server controller to reconnect a player
     * @param controller the controller where the message will be executed
     */
    @Override
    public void serverExecute(Controller controller) {
        try {
            controller.reconnect(id, newId);
        } catch (IllegalArgumentException e) {
            PlayerNotFoundMessage message = new PlayerNotFoundMessage();
            try {
                controller.getConnectionHandler().getServerConnectionHandler(id).sendMessage(message, id);
            } catch (PlayerNotInAnyServerConnectionHandlerException | RemoteException playerNotInAnyServerConnectionHandlerException) {
                System.err.println("Player not found in any server connection handler (ReconnectionMessage serverExecute)");
            }
        } catch (AlreadySetException e) {
            PlayerAlreadyPlayingMessage message = new PlayerAlreadyPlayingMessage();
            try {
                controller.getConnectionHandler().getServerConnectionHandler(id).sendMessage(message, id);
            } catch (PlayerNotInAnyServerConnectionHandlerException | RemoteException playerNotInAnyServerConnectionHandlerException) {
                System.err.println("Player not found in any server connection handler (ReconnectionMessage serverExecute)");
            }
        } catch (NotYetStartedException e) {
            GameNotYetStartedMessage message = new GameNotYetStartedMessage();
            try {
                controller.getConnectionHandler().getServerConnectionHandler(id).sendMessage(message, id);
            } catch (PlayerNotInAnyServerConnectionHandlerException | RemoteException playerNotInAnyServerConnectionHandlerException) {
                System.err.println("Player not found in any server connection handler (ReconnectionMessage serverExecute)");
            }
        } catch (AlreadyFinishedException e) {
            GameAlreadyFinishedMessage message = new GameAlreadyFinishedMessage();
            try {
                controller.getConnectionHandler().getServerConnectionHandler(id).sendMessage(message, id);
            } catch (PlayerNotInAnyServerConnectionHandlerException | RemoteException playerNotInAnyServerConnectionHandlerException) {
                System.err.println("Player not found in any server connection handler (ReconnectionMessage serverExecute)");
            }
        }
    }
}
