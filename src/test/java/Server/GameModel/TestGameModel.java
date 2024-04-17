package Server.GameModel;

import Server.Card.*;
import org.junit.jupiter.api.Test;

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
    public void testEndGamePhase()
    {
        GameModelInstance gameModelInstance = new GameModelInstance();
        assertFalse(gameModelInstance.isEndGamePhase());
        gameModelInstance.setEndGamePhase();
        assertTrue(gameModelInstance.isEndGamePhase());
    }
}
