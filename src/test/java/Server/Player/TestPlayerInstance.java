package Server.Player;
import Server.Card.*;
import Server.Enums.Color;
import Server.Enums.Symbol;
import Server.EventManager.EventManager;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class TestPlayerInstance {
    @Test
    public void testAddCardToHand() {
        PlayerInstance playerInstance = new PlayerInstance("pippo", null);
        GoldFrontFace goldFrontFace = new GoldFrontFace(
                "goldCardFront1.jpg",
                new HashMap<>(),
                5,
                null,
                null,
                Symbol.FUNGUS
        );
        RegularBackFace goldBackFace = new RegularBackFace(
                "goldCardBack1.jpg",
                null
        );
        GoldCard goldCard = new GoldCard(
                goldFrontFace,
                goldBackFace
        );
        playerInstance.addCardToHand(goldCard);
        assertEquals(1, playerInstance.getHand().size());
        assertTrue(playerInstance.getHand().contains(goldCard));
        ResourceFrontFace resourceFrontFace = new ResourceFrontFace(
                "resourceCardFront1.jpg",
                new HashMap<>(),
                0,
                Symbol.FUNGUS
        );
        RegularBackFace resourceBackFace = new RegularBackFace(
                "resourceCardBack1.jpg",
                null
        );
        ResourceCard resourceCard = new ResourceCard(
                resourceFrontFace,
                resourceBackFace
        );
        playerInstance.addCardToHand(resourceCard);
        assertEquals(2, playerInstance.getHand().size());
        assertTrue(playerInstance.getHand().contains(resourceCard));
        assertTrue(playerInstance.getHand().contains(goldCard));
    }
    @Test
    public void testRemoveCardFromHand(){
        PlayerInstance playerInstance = new PlayerInstance("pippo", null);
        GoldFrontFace goldFrontFace = new GoldFrontFace(
                "goldCardFront1.jpg",
                new HashMap<>(),
                5,
                null,
                null,
                Symbol.FUNGUS
        );
        RegularBackFace goldBackFace = new RegularBackFace(
                "goldCardBack1.jpg",
                null
        );
        GoldCard goldCard = new GoldCard(
                goldFrontFace,
                goldBackFace
        );
        playerInstance.addCardToHand(goldCard);
        ResourceFrontFace resourceFrontFace = new ResourceFrontFace(
                "resourceCardFront1.jpg",
                new HashMap<>(),
                0,
                Symbol.FUNGUS
        );
        RegularBackFace resourceBackFace = new RegularBackFace(
                "resourceCardBack1.jpg",
                null
        );
        ResourceCard resourceCard = new ResourceCard(
                resourceFrontFace,
                resourceBackFace
        );
        playerInstance.addCardToHand(resourceCard);
        playerInstance.removeCardFromHand(goldCard);
        assertEquals(1, playerInstance.getHand().size());
        assertTrue(playerInstance.getHand().contains(resourceCard));
        assertFalse(playerInstance.getHand().contains(goldCard));
        playerInstance.removeCardFromHand(resourceCard);
        assertEquals(0, playerInstance.getHand().size());
        assertFalse(playerInstance.getHand().contains(resourceCard));
        assertFalse(playerInstance.getHand().contains(goldCard));
    }
    @Test
    public void testName(){
        PlayerInstance playerInstance = new PlayerInstance("pippo", null);
        assertEquals("pippo", playerInstance.getName());
    }
    @Test
    public void testColor(){
        PlayerInstance playerInstance = new PlayerInstance("pippo", new EventManager());
        playerInstance.setColor(Color.RED);
        assertEquals(Color.RED, playerInstance.getColor());
    }
    @Test
    public void testSecretAchievement() {
        PlayerInstance playerInstance = new PlayerInstance("pippo", new EventManager());
        AchievementFrontFace achievementFrontFace = new AchievementFrontFace(
                "achievementCardFront1.jpg",
                new HashMap<>(),
                3
        );
        EmptyCardFace achievementBackFace = new EmptyCardFace(
                "achievementCardBack1.jpg"
        );
        AchievementCard achievementCard = new AchievementCard(
                achievementFrontFace,
                achievementBackFace
        );
        playerInstance.setSecretObjective(achievementCard);
        assertEquals(achievementCard, playerInstance.getSecretObjective());
    }
    @Test
    public void testPoints(){
        PlayerInstance playerInstance = new PlayerInstance("pippo", new EventManager());
        assertEquals(0, playerInstance.getPoints());
        playerInstance.addPoints(5);
        assertEquals(5, playerInstance.getPoints());
        playerInstance.addPoints(3);
        assertEquals(8, playerInstance.getPoints());
    }
}
