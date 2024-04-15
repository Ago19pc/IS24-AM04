package Server.Deck;

import Server.Card.*;
import Server.Enums.DeckPosition;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestDeck {
    @Test
    public void TestAchievementDeck() {
        AchievementDeck achievementDeck = new AchievementDeck();
        assertEquals (16, achievementDeck.getNumberOfCards());
        for (int i = 0; i < 16; i++) {
            Card drawnCard = achievementDeck.popCard(DeckPosition.DECK);
            assertThrows(
                    ClassCastException.class, () -> {
                        GoldCard card = (GoldCard) drawnCard;
                    }
            );
            assertThrows(
                    ClassCastException.class, () -> {
                        ResourceCard card = (ResourceCard) drawnCard;
                    }
            );
            assertThrows(
                    ClassCastException.class, () -> {
                        StartingCard card = (StartingCard) drawnCard;
                    }
            );


        }

    }
    @Test
    public void TestResourceDeck() {
        ResourceDeck resourceDeck = new ResourceDeck();
        assertEquals (40, resourceDeck.getNumberOfCards());
        for (int i = 0; i < 40; i++) {
            Card drawnCard = resourceDeck.popCard(DeckPosition.DECK);
            ClassCastException exceptionThrown = assertThrows(
                    ClassCastException.class, () -> {
                        GoldCard card = (GoldCard) drawnCard;
                    }
            );
            ClassCastException exceptionThrown2 = assertThrows(
                    ClassCastException.class, () -> {
                        AchievementCard card = (AchievementCard) drawnCard;
                    }
            );
            ClassCastException exceptionThrown3 = assertThrows(
                    ClassCastException.class, () -> {
                        StartingCard card = (StartingCard) drawnCard;
                    }
            );


        }

    }
    @Test
    public void TestGoldDeck() {
        GoldDeck goldDeck = new GoldDeck();
        assertEquals (40, goldDeck.getNumberOfCards());
        for (int i = 0; i < 40; i++) {
            Card drawnCard = goldDeck.popCard(DeckPosition.DECK);
            assertDoesNotThrow(() -> {
                GoldCard card = (GoldCard) drawnCard;
            });
            ClassCastException exceptionThrown2 = assertThrows(
                    ClassCastException.class, () -> {
                        ResourceCard card = (ResourceCard) drawnCard;
                    }
            );
            ClassCastException exceptionThrown3 = assertThrows(
                    ClassCastException.class, () -> {
                        StartingCard card = (StartingCard) drawnCard;
                    }
            );


        }

    }
}
