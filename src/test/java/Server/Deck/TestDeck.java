package Server.Deck;

import Server.Card.*;
import Server.Enums.CardCorners;
import Server.Enums.DeckPosition;
import Server.Enums.Face;
import Server.Enums.Symbol;
import Server.Exception.AlreadyFinishedException;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TestDeck {
    @Test
    public void TestAchievementDeck() throws AlreadyFinishedException {
        Boolean test = true;
        AchievementDeck achievementDeck = new AchievementDeck();
        assertEquals (14, achievementDeck.getNumberOfCards());
        Map<Symbol,Integer> scoreRequirements = new HashMap<>();
        Map<Symbol,Integer> scoreRequirements1 = new HashMap<>();
        scoreRequirements.put(Symbol.PATTERN1A, 1);
        Map<Symbol,Integer> scoreRequirementCard = achievementDeck.popCard(DeckPosition.DECK).getFace(Face.FRONT).getScoreRequirements();
        for (Symbol symbol : scoreRequirementCard.keySet()) {
            if(scoreRequirementCard.get(symbol) != 0){
                scoreRequirements1.put(symbol, 1);
            }
        }
        assertEquals(scoreRequirements1, scoreRequirements);
        for (int i = 0; i < 13; i++) {
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
        Boolean test = true;
        ResourceDeck resourceDeck = new ResourceDeck();
        assertEquals (38, resourceDeck.getNumberOfCards());
        Map<CardCorners, Symbol> cornerSymbols = new HashMap<>();
        cornerSymbols.put(CardCorners.TOP_LEFT, Symbol.EMPTY);
        cornerSymbols.put(CardCorners.TOP_RIGHT, Symbol.NONE);
        cornerSymbols.put(CardCorners.BOTTOM_LEFT, Symbol.FUNGUS);
        cornerSymbols.put(CardCorners.BOTTOM_RIGHT, Symbol.FUNGUS);
        assertEquals(cornerSymbols , resourceDeck.popCard(DeckPosition.DECK).getCornerFace(Face.FRONT).getCornerSymbols());
        for (int i = 0; i < 37; i++) {
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
        Boolean test = true;
        GoldDeck goldDeck = new GoldDeck();
        Map<CardCorners, Symbol> cornerSymbols = new HashMap<>();
        cornerSymbols.put(CardCorners.TOP_LEFT, Symbol.PARCHMENT);
        cornerSymbols.put(CardCorners.TOP_RIGHT, Symbol.EMPTY);
        cornerSymbols.put(CardCorners.BOTTOM_LEFT, Symbol.EMPTY);
        cornerSymbols.put(CardCorners.BOTTOM_RIGHT, Symbol.NONE);
        assertEquals (38, goldDeck.getNumberOfCards());
        assertEquals(cornerSymbols,goldDeck.popCard(DeckPosition.DECK).getCornerFace(Face.FRONT).getCornerSymbols());
        for (int i = 0; i < 37; i++) {
            Card drawnCard = goldDeck.popCard(DeckPosition.DECK);
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
}
