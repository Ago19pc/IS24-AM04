package Server.Controller;

import Server.Card.AchievementCard;
import Server.Card.CornerCardFace;
import Server.Card.StartingCard;
import Server.Enums.DeckPosition;
import Server.Enums.Decks;
import Server.Exception.AlreadyFinishedException;
import Server.Exception.AlreadyStartedException;
import Server.Exception.TooFewElementsException;
import Server.Exception.TooManyElementsException;
import Server.Player.Player;

import java.util.List;
import java.util.Map;

public class LeaderboardState implements ServerState{
    private Controller controller;

    public LeaderboardState(Controller controller){
        this.controller = controller;
    }

    @Override
    public void addPlayer(String name, String clientID) throws AlreadyStartedException {
        throw new AlreadyStartedException("Game already started");
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
    public void setPlayerColor() {

    }

    @Override
    public void setSecretObjectiveCard() {

    }

    @Override
    public void setStartingCard() {

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
        //do nothing
    }

    @Override
    public void reconnect(String oldId, String newId, Player player, Map<Player, List<AchievementCard>> givenSecretObjectiveCards, Map<Player, StartingCard> givenStartingCards) throws AlreadyFinishedException {
        throw  new AlreadyFinishedException("Game already finished");
    }

    @Override
    public boolean isInSavedGameLobby() {
        return false;
    }
}
