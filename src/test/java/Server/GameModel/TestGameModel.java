package Server.GameModel;

import Server.Card.*;
import Server.Player.Player;
import Server.Player.PlayerInstance;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestGameModel {
    @Test
    public void testNewGameModelInstance() {
        GameModelInstance gameModelInstance = new GameModelInstance();
        assertEquals(0, gameModelInstance.getTurn());
        assertFalse(gameModelInstance.isEndGamePhase());
        List<StartingCard> startingCards = gameModelInstance.getStartingCards();
        assertEquals(6, startingCards.size());
        for(Card card : startingCards) {
            ClassCastException exceptionThrown = assertThrows(
                    ClassCastException.class, () -> {
                        GoldCard goldCard = (GoldCard) card;
                    }
            );
            ClassCastException exceptionThrown2 = assertThrows(
                    ClassCastException.class, () -> {
                        AchievementCard achievementCard = (AchievementCard) card;
                    }
            );
            ClassCastException exceptionThrown3 = assertThrows(
                    ClassCastException.class, () -> {
                        ResourceCard resourceCard = (ResourceCard) card;
                    }
            );
        }
    }
    @Test
    public void testTurn()
    {
        GameModelInstance gameModelInstance = new GameModelInstance();
        assertEquals(0, gameModelInstance.getTurn());
        gameModelInstance.nextTurn();
        assertEquals(1, gameModelInstance.getTurn());
        gameModelInstance.nextTurn();
        assertEquals(2, gameModelInstance.getTurn());
    }
    @Test
    public void testEndGamePhase() {
        GameModelInstance gameModelInstance = new GameModelInstance();
        assertFalse(gameModelInstance.isEndGamePhase());
        gameModelInstance.setEndGamePhase();
        assertTrue(gameModelInstance.isEndGamePhase());
    }
    @Test
    public void testNewPlayer() {
        GameModelInstance gameModelInstance = new GameModelInstance();
        Player player = new PlayerInstance("player1",null);
        gameModelInstance.addPlayer(player);
        List<Player> playerList = gameModelInstance.getPlayerList();
        assertEquals(1, playerList.size());
        assertEquals(player, playerList.get(0));
    }
    @Test
    public void testRemovePlayer() {
        GameModelInstance gameModelInstance = new GameModelInstance();
        Player player = new PlayerInstance("player1",null);
        gameModelInstance.addPlayer(player);
        List<Player> playerList = gameModelInstance.getPlayerList();
        assertEquals(1, playerList.size());
        gameModelInstance.removePlayer(player);
        assertEquals(0, playerList.size());
    }
    @Test
    public void testShufflePlayers()
    {
        GameModelInstance gameModelInstance = new GameModelInstance();
        Player player1 = new PlayerInstance("player1",null);
        Player player2 = new PlayerInstance("player2",null);
        Player player3 = new PlayerInstance("player3",null);
        Player player4 = new PlayerInstance("player4",null);
        gameModelInstance.addPlayer(player1);
        gameModelInstance.addPlayer(player2);
        gameModelInstance.addPlayer(player3);
        gameModelInstance.addPlayer(player4);
        List<Player> playerList = new ArrayList<>(gameModelInstance.getPlayerList());
        boolean isShuffled = false;
        for(int j = 0; j < 10 ; j++) {


            gameModelInstance.shufflePlayerList();
            List<Player> shuffledPlayerList = gameModelInstance.getPlayerList();
            assertEquals(4, shuffledPlayerList.size());
            for (int i = 0; i < 4; i++) {
                if (playerList.get(i) != shuffledPlayerList.get(i)) {
                    isShuffled = true;
                    break;
                }
            }
        }
        assertTrue(isShuffled);
    }
}
