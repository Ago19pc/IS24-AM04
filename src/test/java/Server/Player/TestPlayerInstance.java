package Server.Player;
import Server.Card.*;
import Server.Enums.Color;
import Server.Enums.Symbol;


import Server.Exception.AlreadySetException;
import Server.Exception.TooFewElementsException;
import Server.Exception.TooManyElementsException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestPlayerInstance {
    @Test
    public void testAddCardToHand() throws TooManyElementsException {
        PlayerInstance playerInstance = new PlayerInstance("pippo");
        GoldFrontFace goldFrontFace = new GoldFrontFace(
                "goldCardFront1.jpg",
                new HashMap<>(),
                5,
                new HashMap<>(),
                new HashMap<>(),
                Symbol.FUNGUS
        );
        RegularBackFace goldBackFace = new RegularBackFace(
                "goldCardBack1.jpg",
                new ArrayList<>(List.of(Symbol.BUG))
        );
        GoldCard goldCard = new GoldCard(
                goldFrontFace,
                goldBackFace,
                "1.jpeg"
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
                new ArrayList<>(List.of(Symbol.BUG))
        );
        ResourceCard resourceCard = new ResourceCard(
                resourceFrontFace,
                resourceBackFace,
                "1.jpeg"
        );
        playerInstance.addCardToHand(resourceCard);
        assertEquals(2, playerInstance.getHand().size());
        assertTrue(playerInstance.getHand().contains(resourceCard));
        assertTrue(playerInstance.getHand().contains(goldCard));
    }
    @Test
    public void testRemoveCardFromHand() throws TooFewElementsException, TooManyElementsException {
        PlayerInstance playerInstance = new PlayerInstance("pippo");
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
                new ArrayList<>(List.of(Symbol.BUG))
        );
        GoldCard goldCard = new GoldCard(
                goldFrontFace,
                goldBackFace,
                "1.jpeg"
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
                new ArrayList<>(List.of(Symbol.BUG))
        );
        ResourceCard resourceCard = new ResourceCard(
                resourceFrontFace,
                resourceBackFace,
                "1.jpeg"
        );
        playerInstance.addCardToHand(resourceCard);
        ResourceCard resourceCard2 = new ResourceCard(
                resourceFrontFace,
                resourceBackFace,
                "1.jpeg"
        );
        playerInstance.addCardToHand(resourceCard2);
        playerInstance.removeCardFromHand(0);
        assertEquals(2, playerInstance.getHand().size());
        assertTrue(playerInstance.getHand().contains(resourceCard));
        assertFalse(playerInstance.getHand().contains(goldCard));
        playerInstance.addCardToHand(goldCard);
        playerInstance.removeCardFromHand(0);
        assertEquals(2, playerInstance.getHand().size());
        assertFalse(playerInstance.getHand().contains(resourceCard));
        assertTrue(playerInstance.getHand().contains(goldCard));
    }
    @Test
    public void testName(){
        PlayerInstance playerInstance = new PlayerInstance("pippo");
        assertEquals("pippo", playerInstance.getName());
    }
    @Test
    public void testColor(){
        PlayerInstance playerInstance = new PlayerInstance("pippo");
        playerInstance.setColor(Color.RED);
        assertEquals(Color.RED, playerInstance.getColor());
    }
    @Test
    public void testSecretAchievement() throws AlreadySetException {
        PlayerInstance playerInstance = new PlayerInstance("pippo");
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
                achievementBackFace,
                "1.jpeg"
        );
        playerInstance.setSecretObjective(achievementCard);
        assertEquals(achievementCard, playerInstance.getSecretObjective());
    }
    @Test
    public void testPoints() {
        PlayerInstance playerInstance = new PlayerInstance("pippo");
        assertEquals(0, playerInstance.getPoints());
        playerInstance.addPoints(5);
        assertEquals(5, playerInstance.getPoints());
        playerInstance.addPoints(3);
        assertEquals(8, playerInstance.getPoints());
    }
}
