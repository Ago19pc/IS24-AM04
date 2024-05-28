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

public class ReconnectionMessage implements Serializable, ToClientMessage, ToServerMessage {
    private String id;
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
    public ReconnectionMessage(String id, String newId){
        this.id = id;
        this.newId = newId;
    }

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

    @Override
    public void clientExecute(ClientController controller) {
        controller.setGameInfo(id, commonAchievements, goldDeck, resourceDeck, name, secretAchievement, hand, turn, players, chat, gameState);
    }

    @Override
    public void serverExecute(Controller controller) {
        try {
            controller.reconnect(id, newId);
        } catch (IllegalArgumentException e) {
            PlayerNotFoundMessage message = new PlayerNotFoundMessage();
            try {
                controller.getConnectionHandler().getServerConnectionHandler(id).sendMessage(message, id);
            } catch (PlayerNotInAnyServerConnectionHandlerException | RemoteException playerNotInAnyServerConnectionHandlerException) {
                playerNotInAnyServerConnectionHandlerException.printStackTrace();
            }
        } catch (AlreadySetException e) {
            PlayerAlreadyPlayingMessage message = new PlayerAlreadyPlayingMessage();
            try {
                controller.getConnectionHandler().getServerConnectionHandler(id).sendMessage(message, id);
            } catch (PlayerNotInAnyServerConnectionHandlerException | RemoteException playerNotInAnyServerConnectionHandlerException) {
                playerNotInAnyServerConnectionHandlerException.printStackTrace();
            }
        } catch (NotYetStartedException e) {
            GameNotYetStartedMessage message = new GameNotYetStartedMessage();
            try {
                controller.getConnectionHandler().getServerConnectionHandler(id).sendMessage(message, id);
            } catch (PlayerNotInAnyServerConnectionHandlerException | RemoteException playerNotInAnyServerConnectionHandlerException) {
                playerNotInAnyServerConnectionHandlerException.printStackTrace();
            }
        } catch (AlreadyFinishedException e) {
            GameAlreadyFinishedMessage message = new GameAlreadyFinishedMessage();
            try {
                controller.getConnectionHandler().getServerConnectionHandler(id).sendMessage(message, id);
            } catch (PlayerNotInAnyServerConnectionHandlerException | RemoteException playerNotInAnyServerConnectionHandlerException) {
                playerNotInAnyServerConnectionHandlerException.printStackTrace();
            }
        }
    }
}
