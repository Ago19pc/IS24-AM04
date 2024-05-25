package Client.View;

import Server.Card.AchievementCard;
import Server.Card.Card;
import Server.Chat.Message;
import Server.Enums.Actions;
import Server.Enums.DeckPosition;
import Server.Enums.Decks;
import Server.Exception.PlayerNotFoundByNameException;

import java.util.LinkedHashMap;
import java.util.List;

public interface UI {
    void askConnectionMode();

    void nameChanged(String name);

    void colorChanged();

    void successfulConnection();

    void displayNewCardInHand();

    void nameChangeFailed();

    void needName();

    void needColor();

    void needNameOrColor();

    void unavailableColor();

    void colorChangeFailed();

    void invalidColor();

    void needConnection();

    void connectionFailed();

    void cantDrawAchievementCards();

    void alreadyDone(Actions action);

    void cardNotPlaceable();

    void chatMessageIsEmpty();

    void deckIsEmpty();

    void gameAlreadyStarted();

    void gameNotYetStarted();

    void invalidCardForAction(Actions cardType);

    void notYetGivenCard(Actions cardType);

    void notYourTurn();

    void playerListChanged();

    void displayPlayerColors();

    void updateReady(String name, boolean ready);

    void chat(Message message);

    void chooseSecretAchievement(List<AchievementCard> possibleAchievements);

    void endGameStarted();

    void displayHand();

    void displayLeaderboard(LinkedHashMap<String, Integer> playerPoints);

    void displayNewPlayer();

    void otherPlayerDraw(String name, Decks deckFrom, DeckPosition position);

    void newTurn();

    void otherPlayerInitialHand(String name);
    void secretAchievementChosen(String name);
    void startingCardChosen(String name);
    void gameStarted();
    void displayPlayerOrder();
    void displayBoardCards();
    void chooseStartingCardFace(Card card);
    void doFirst(Actions action);
    void displayLobby();
    void displayPlayerPoints(String playerName) throws PlayerNotFoundByNameException;
    void cardPlaced(String playerName, int x, int y);
    void playerDisconnected(String playerName);
    void displayCommonAchievements();
    void tooManyPlayers();
    void displayId();
    void playerRemoved(String name);
    void otherPlayerReconnected(String name);
    void idNotInGame();
    void playerAlreadyPlaying();
    void displayGameInfo();

}