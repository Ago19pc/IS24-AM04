package Server.Controller;

import Server.Card.AchievementCard;
import Server.Card.CornerCardFace;
import Server.Card.StartingCard;
import Server.Connections.GeneralServerConnectionHandler;
import Server.Enums.DeckPosition;
import Server.Enums.Decks;
import Server.Exception.*;
import Server.GameModel.GameModel;
import Server.Messages.NewPlayerMessage;
import Server.Messages.PlayerNameMessage;
import Server.Player.Player;
import Server.Player.PlayerInstance;

import java.util.List;
import java.util.Map;

/**
 * Class for the state where the lobby is

 */
public class LobbyState implements ServerState{
    private GameModel gameModel;
    private GeneralServerConnectionHandler connectionHandler;
    private Controller controller;
    /**
     * Constructor
     * @param gameModel the game model to use
     * @param connectionHandler the connection handler to use
     * @param controller the controller to use
     */
    public LobbyState(GameModel gameModel, GeneralServerConnectionHandler connectionHandler, Controller controller){
        this.gameModel = gameModel;
        this.connectionHandler = connectionHandler;
        this.controller = controller;
    }

    @Override
    public void addPlayer(String name, String clientID) throws TooManyPlayersException, IllegalArgumentException {
        Player player = new PlayerInstance(name);
        for (Player p : gameModel.getPlayerList()){
            if (p.getName().equals(player.getName())) throw new IllegalArgumentException("Player with same name already exists");
        }
        if(gameModel.getPlayerList().size()<4) {
            gameModel.addPlayer(player);
            connectionHandler.addPlayerByID(name, clientID);
            PlayerNameMessage playerNameMessage = new PlayerNameMessage(player.getName(), true);
            connectionHandler.sendMessage(playerNameMessage, player.getName());
            NewPlayerMessage playerMessage = new NewPlayerMessage(gameModel.getPlayerList());
            connectionHandler.sendAllMessage(playerMessage);
        } else {
            throw new TooManyPlayersException("Too many players");
        }
    }

    @Override
    public void addSavedPlayer(String clientId, String name) throws AlreadyStartedException {
        throw new AlreadyStartedException("Game already started");
    }

    @Override
    public void removePlayer(Player player) {
        //do nothing
    }

    @Override
    public void playCard(Player player, int position, int xCoord, int yCoord, CornerCardFace face) throws TooFewElementsException {
        System.out.println("Player has already played");
        throw new TooFewElementsException("Already played");
    }

    @Override
    public void drawCard(Player player, DeckPosition deckPosition, Decks deck) throws TooManyElementsException {
        throw new TooManyElementsException("Play a card first");
    }

    @Override
    public void reactToDisconnection(String id) {
        String playerName = connectionHandler.getPlayerNameByID(id);
        try {
            controller.removePlayer(controller.getPlayerByName(playerName));
        } catch (PlayerNotFoundByNameException e) {
            throw new RuntimeException(e);
        }
        if (controller.getPlayerList().stream().allMatch(Player::isReady) && controller.getPlayerList().size() > 1){
            try {
                controller.start();
            } catch (TooFewElementsException | AlreadySetException e) {
                //do nothing as it's normal that it's already set
            }
        }
    }

    @Override
    public void reconnect(String oldId, String newId, Player player, Map<Player, List<AchievementCard>> givenSecretObjectiveCards, Map<Player, StartingCard> givenStartingCards) throws NotYetStartedException {
        throw new NotYetStartedException ("Game not started");
    }

    @Override
    public boolean isInSavedGameLobby() {
        return false;
    }
}
