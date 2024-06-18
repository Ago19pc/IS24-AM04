package Server.Controller;

import Server.Card.AchievementCard;
import Server.Card.CornerCardFace;
import Server.Card.StartingCard;
import Server.Enums.DeckPosition;
import Server.Enums.Decks;
import Server.Exception.*;
import Server.Player.Player;

import java.util.List;
import java.util.Map;

public interface ServerState {
    void addPlayer(String name, String clientID) throws AlreadyStartedException, TooManyPlayersException, IllegalArgumentException;

    void addSavedPlayer(String clientId, String name) throws AlreadyStartedException, IllegalArgumentException, PlayerNotFoundByNameException;

    void removePlayer(Player player);

    void setPlayerColor();
    void setSecretObjectiveCard();
    void setStartingCard();
    void playCard(Player player, int position, int xCoord, int yCoord, CornerCardFace face) throws TooFewElementsException;
    void drawCard(Player player, DeckPosition deckPosition, Decks deck) throws TooManyElementsException, AlreadyFinishedException;
    void reactToDisconnection(String id);
    void reconnect(String oldId, String newId, Player player, Map<Player, List<AchievementCard>> givenSecretObjectiveCards, Map<Player, StartingCard> givenStartingCards) throws AlreadyFinishedException, NotYetStartedException;

    boolean isInSavedGameLobby();
}
