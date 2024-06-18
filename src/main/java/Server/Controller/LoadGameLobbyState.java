package Server.Controller;

import Client.Deck;
import Server.Card.*;
import Server.Connections.GeneralServerConnectionHandler;
import Server.Enums.DeckPosition;
import Server.Enums.Decks;
import Server.Exception.*;
import Server.GameModel.GameModel;
import Server.Messages.NewPlayerMessage;
import Server.Messages.PlayerNameMessage;
import Server.Messages.SavedGameMessage;
import Server.Player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * Class for the state where the saved game lobby is

 */
public class LoadGameLobbyState implements ServerState{
    private GeneralServerConnectionHandler connectionHandler;
    private GameModel gameModel;
    private Controller controller;

    /**
     * Constructor
     * @param connectionHandler the connection handler to use
     * @param gameModel the game model to use
     * @param controller the controller to use
     */
    public LoadGameLobbyState(GeneralServerConnectionHandler connectionHandler, GameModel gameModel, Controller controller){
        this.connectionHandler = connectionHandler;
        this.gameModel = gameModel;
        this.controller = controller;
    }

    @Override
    public void addPlayer(String name, String clientID) throws AlreadyStartedException {
        throw new AlreadyStartedException("Game already started");
    }

    @Override
    public void addSavedPlayer(String clientId, String name) throws IllegalArgumentException, PlayerNotFoundByNameException {
        if(connectionHandler.isNameConnectedToId(name)) throw new IllegalArgumentException("Player has already connected");
        Player player = null;
        for (Player p : gameModel.getPlayerList()){
            if (p.getName().equals(name)){
                player = p;
            }
        }
        if(player == null) throw new PlayerNotFoundByNameException("Player not found");
        connectionHandler.addPlayerByID(name, clientId);
        PlayerNameMessage playerNameMessage = new PlayerNameMessage(name, true);
        connectionHandler.sendMessage(playerNameMessage, name);
        NewPlayerMessage playerMessage = new NewPlayerMessage(gameModel.getPlayerList());
        connectionHandler.sendAllMessage(playerMessage);
        boolean allSet = true;
        for (Player p : gameModel.getPlayerList()){
            if (!connectionHandler.isNameConnectedToId(p.getName())){
                allSet = false;
                break;
            }
        }
        if (allSet) {
            controller.changeState(new PlaceCardState(controller, gameModel));
            List<AchievementCard> commonAchievementCards = new ArrayList<>();
            commonAchievementCards.add(gameModel.getAchievementDeck().getBoardCard().get(DeckPosition.FIRST_CARD));
            commonAchievementCards.add(gameModel.getAchievementDeck().getBoardCard().get(DeckPosition.SECOND_CARD));
            Deck<GoldCard> goldDeck = new Deck<GoldCard>(
                    gameModel.getGoldDeck().getNumberOfCards(),
                    new ArrayList<>(List.of(gameModel.getGoldDeck().getTopCardNoPop(), gameModel.getGoldDeck().getBoardCard().get(DeckPosition.FIRST_CARD), gameModel.getGoldDeck().getBoardCard().get(DeckPosition.SECOND_CARD)))
            );
            Deck<ResourceCard> resourceDeck = new Deck<ResourceCard>(
                    gameModel.getResourceDeck().getNumberOfCards(),
                    new ArrayList<>(List.of(gameModel.getResourceDeck().getTopCardNoPop(), gameModel.getResourceDeck().getBoardCard().get(DeckPosition.FIRST_CARD), gameModel.getResourceDeck().getBoardCard().get(DeckPosition.SECOND_CARD)))
            );
            List<Client.Player> playerList = new ArrayList<>();
            System.out.println("Il giocatore attivo è il giocatore numero " + gameModel.getActivePlayerIndex());
            System.out.println("E' il turno di " + gameModel.getPlayerList().get(gameModel.getActivePlayerIndex()).getName());
            for (Player p : gameModel.getPlayerList()){
                playerList.add(new Client.Player(
                        p.getName(),
                        p.getPoints(),
                        p.getHand().size(),
                        gameModel.getActivePlayerIndex() == controller.getPlayerList().indexOf(p),
                        p.getColor(),
                        p.getManuscript()
                ));
            }
            for(Player p : gameModel.getPlayerList()){
                SavedGameMessage message = new SavedGameMessage(
                        p.getName(),
                        commonAchievementCards,
                        goldDeck,
                        resourceDeck,
                        p.getSecretObjective(),
                        p.getHand(),
                        gameModel.getTurn(),
                        playerList,
                        gameModel.getChat()
                );
                connectionHandler.sendMessage(message, p.getName());
            }
        }
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
        connectionHandler.removePlayerByName(playerName);
    }

    @Override
    public void reconnect(String oldId, String newId, Player player, Map<Player, List<AchievementCard>> givenSecretObjectiveCards, Map<Player, StartingCard> givenStartingCards) throws NotYetStartedException {
        throw new NotYetStartedException("Game not started");
    }

    @Override
    public boolean isInSavedGameLobby() {
        return true;
    }
}
