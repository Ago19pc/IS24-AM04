package Client.Controller;

import Client.Deck;
import Client.Player;
import Client.View.UI;
import Server.Card.*;
import Server.Chat.Chat;
import Server.Chat.Message;
import Server.Enums.*;
import Server.Exception.PlayerNotFoundByNameException;

import java.rmi.RemoteException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface ClientController {
    void main(UI ui) throws RemoteException;

    boolean isSavedGame();

    void setName(String name);

    List<AchievementCard> getPotentialSecretAchievements();

    void setChosenHandCard(Integer chosenHandCard);

    Integer getChosenHandCard();

    Player getPlayerByName(String name) throws PlayerNotFoundByNameException;

    Color getMyColor();

    String getMyId();

    void setMyColor(Color color);

    void setId(String id);

    void reconnect(String newId);

    void joinSavedGame(String name);

    void joinServer(String ip, int port);

    void setReady();

    void askSetColor(String color);

    void sendChatMessage(String message);

    void askSetName(String name);

    void setRMIMode(boolean rmi);

    void chooseStartingCardFace(Face face);

    void chooseSecretAchievement(int index);

    void askPlayCard(int cardNumber, Face face, int x, int y);

    void askDrawCard(Decks deck, DeckPosition deckPosition);

    int getIndexofSecretAchievement();

    String getMyName();

    List<Player> getPlayers();

    GameState getGameState();

    List<Color> getAvailableColors();

    List<Card> getHand();

    List<AchievementCard> getCommonAchievements();

    Map<String, Integer> getLeaderboard();

    int getDeckSize(Decks deck);

    List<Card> getBoardCards(Decks deck);

    Player getActivePlayer();

    int getTurn();

    List<String> getPlayerNames();

    void loadLobbyInfo(String id, List<String> playerNames, Map<String, Color> playerColors, Map<String, Boolean> playerReady, Boolean isSavedGame);

    void giveAchievementCards(List<AchievementCard> secretCards, List<AchievementCard> commonCards);

    void achievementDeckDrawInvalid();

    void alreadyDone(Actions action);

    void cardNotPlaceable();

    void addChatMessage(Message message);

    void chatMessageIsEmpty();

    void colorNotYetSet();

    void giveDrawnCard(Card drawnCard);

    void emptyDeck();

    void setEndGame();

    void gameAlreadyStarted();

    void gameNotYetStarted();

    void giveInitialHand(List<Card> hand);

    void invalidCard(Actions cardType);

    void displayLeaderboard(LinkedHashMap<String, Integer> playerPoints);

    void nameNotYetSet();

    void newPlayer(List<String> playerNames);

    void notYetGivenCard(Actions type);

    void notYourTurn();

    void newTurn(String activePlayerName, int turnNumber);

    void drawOtherPlayer(String name, Decks deckFrom, DeckPosition drawPosition, List<Card> newBoardCards);

    void giveOtherPlayerInitialHand(String name);

    void setColor(boolean confirmation, Color color);

    void updatePlayerColors(Color color, String name);

    void setName(Boolean confirmation);

    void updatePlayerReady(boolean ready, String name);

    void setSecretCard(String name);

    void setSecretCard(String name, int chosenCard);

    void startingCardChosen(String name, CornerCardFace startingFace);

    void startGame(List<GoldCard> goldBoardCards, List<ResourceCard> resourceBoardCards);

    void updatePlayerOrder(List<String> playerNames);

    void giveStartingCard(Card card);

    void toDoFirst(Actions actionToDo);

    void placeCard(String playerName, CornerCardFace placedCardFace, int x, int y, int points);

    void playerDisconnected(String playerName);

    void invalidName();

    void tooManyPlayers();

    void playerRemoved(String name);

    void otherPlayerReconnected(String name);

    void idNotInGame();

    void playerAlreadyPlaying();

    AchievementCard getSecretAchievement();

    List<Message> getChat();

    void setGameInfo(String id, List<AchievementCard> commonAchievements, Deck<GoldCard> goldDeck, Deck<ResourceCard> resourceDeck, String name, AchievementCard secretAchievement, List<Card> hand, int turn, List<Player> players, Chat chat, GameState gameState);

    void loadGame(List<AchievementCard> commonAchievements, Deck<GoldCard> goldDeck, Deck<ResourceCard> resourceDeck, AchievementCard secretAchievement, List<Card> hand, int turn, List<Player> players, Chat chat, GameState gameState, String name);

    void gameAlreadyFinished();
}
