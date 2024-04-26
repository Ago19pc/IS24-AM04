package Server.Deck;

import Server.Card.*;
import Server.Enums.DeckPosition;
import Server.Enums.Face;
import Server.Exception.AlreadyFinishedException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestDeck {
    @Test
    public void TestAchievementDeck() throws AlreadyFinishedException {
        AchievementDeck achievementDeck = new AchievementDeck();
        assertEquals (14, achievementDeck.getNumberOfCards());
        for (int i = 0; i < 14; i++) {
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
    public void TestResourceDeck() throws AlreadyFinishedException{
        ResourceDeck resourceDeck = new ResourceDeck();
        assertEquals (38, resourceDeck.getNumberOfCards());
        for (int i = 0; i < 38; i++) {
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
    public void TestGoldDeck() throws AlreadyFinishedException {
        GoldDeck goldDeck = new GoldDeck();
        assertEquals (38, goldDeck.getNumberOfCards());
        for (int i = 0; i < 38; i++) {
            Card drawnCard = goldDeck.popCard(DeckPosition.DECK);
            System.out.println(drawnCard.getCornerFace(Face.FRONT).getCornerSymbols());
            System.out.println(drawnCard.getCornerFace(Face.FRONT).getKingdom());
            System.out.println(drawnCard.getCornerFace(Face.FRONT).getPlacementRequirements());
            System.out.println(drawnCard.getCornerFace(Face.FRONT).getScoreRequirements());
            System.out.println(drawnCard.getCornerFace(Face.FRONT).getScore());
            System.out.println(" ");
            assertDoesNotThrow(() -> {
                GoldCard card = (GoldCard) drawnCard;
            });




        }

    }
}
