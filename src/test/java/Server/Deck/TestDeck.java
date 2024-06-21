package Server.Deck;

import Server.Card.*;
import Server.Enums.CardCorners;
import Server.Enums.DeckPosition;
import Server.Enums.Face;
import Server.Enums.Symbol;
import Server.Exception.AlreadyFinishedException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestDeck {
    @Test
    public void TestAchievementDeck() throws AlreadyFinishedException {
        Boolean test = true;
        AchievementDeck achievementDeck = new AchievementDeck(true);
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
        ResourceDeck resourceDeck = new ResourceDeck(true);
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
        GoldDeck goldDeck = new GoldDeck(true);
        Map<CardCorners, Symbol> cornerSymbols = new HashMap<>();
        cornerSymbols.put(CardCorners.TOP_LEFT, Symbol.PARCHMENT);
        cornerSymbols.put(CardCorners.TOP_RIGHT, Symbol.EMPTY);
        cornerSymbols.put(CardCorners.BOTTOM_LEFT, Symbol.EMPTY);
        cornerSymbols.put(CardCorners.BOTTOM_RIGHT, Symbol.NONE);
        assertEquals (38, goldDeck.getNumberOfCards());
        assertEquals(cornerSymbols,goldDeck.getTopCardNoPop().getFace(Face.FRONT).getCornerSymbols());
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
    @Test
    public void testgetGoldBoardCard()
    {
        Boolean test = true;
        GoldDeck goldDeck = new GoldDeck(true);
        Map<CardCorners,Symbol> cornerSymbols = new HashMap<>();
        cornerSymbols.put(CardCorners.TOP_LEFT, Symbol.NONE);
        cornerSymbols.put(CardCorners.TOP_RIGHT, Symbol.EMPTY);
        cornerSymbols.put(CardCorners.BOTTOM_LEFT, Symbol.EMPTY);
        cornerSymbols.put(CardCorners.BOTTOM_RIGHT, Symbol.QUILL);
        Map<Symbol,Integer> PlacementRequirements = new HashMap<>();
        PlacementRequirements.put(Symbol.FUNGUS, 2);
        PlacementRequirements.put(Symbol.ANIMAL, 1);
        Map<Symbol,Integer> scoreRequirements = new HashMap<>();
        scoreRequirements.put(Symbol.QUILL, 1);
        List<Symbol> symbols = new ArrayList<>();
        symbols.add(Symbol.FUNGUS);
        GoldCard goldCard = new GoldCard(new GoldFrontFace("",cornerSymbols,1, PlacementRequirements,scoreRequirements,Symbol.FUNGUS), new RegularBackFace("",symbols ),"");
        assertEquals(goldCard.toString(), goldDeck.getBoardCard().get(DeckPosition.FIRST_CARD).toString());
        cornerSymbols = new HashMap<>();
        cornerSymbols.put(CardCorners.TOP_LEFT, Symbol.EMPTY);
        cornerSymbols.put(CardCorners.TOP_RIGHT, Symbol.BOTTLE);
        cornerSymbols.put(CardCorners.BOTTOM_LEFT, Symbol.NONE);
        cornerSymbols.put(CardCorners.BOTTOM_RIGHT, Symbol.EMPTY);
        PlacementRequirements = new HashMap<>();
        PlacementRequirements.put(Symbol.FUNGUS, 2);
        PlacementRequirements.put(Symbol.PLANT, 1);
        scoreRequirements = new HashMap<>();
        scoreRequirements.put(Symbol.BOTTLE, 1);
        goldCard = new GoldCard(new GoldFrontFace("",cornerSymbols,1, PlacementRequirements,scoreRequirements,Symbol.FUNGUS), new RegularBackFace("",symbols ),"");
        assertEquals(goldCard.toString(), goldDeck.getBoardCard().get(DeckPosition.SECOND_CARD).toString());

    }
    @Test
    public void getResourcesBoardCard()
    {
        ResourceDeck resourceDeck = new ResourceDeck(true);
        Map<CardCorners,Symbol> cornerSymbols = new HashMap<>();
        cornerSymbols.put(CardCorners.TOP_LEFT, Symbol.FUNGUS);
        cornerSymbols.put(CardCorners.TOP_RIGHT, Symbol.EMPTY);
        cornerSymbols.put(CardCorners.BOTTOM_LEFT, Symbol.FUNGUS);
        cornerSymbols.put(CardCorners.BOTTOM_RIGHT, Symbol.NONE);
        List<Symbol> symbols = new ArrayList<>();
        symbols.add(Symbol.FUNGUS);
        ResourceCard resourceCard = new ResourceCard(new ResourceFrontFace("",cornerSymbols,0,Symbol.FUNGUS), new RegularBackFace("",symbols ),"");
        assertEquals(resourceCard.toString(), resourceDeck.getBoardCard().get(DeckPosition.FIRST_CARD).toString());
        cornerSymbols = new HashMap<>();
        cornerSymbols.put(CardCorners.TOP_LEFT, Symbol.FUNGUS);
        cornerSymbols.put(CardCorners.TOP_RIGHT, Symbol.FUNGUS);
        cornerSymbols.put(CardCorners.BOTTOM_LEFT, Symbol.NONE);
        cornerSymbols.put(CardCorners.BOTTOM_RIGHT, Symbol.EMPTY);
        resourceCard = new ResourceCard(new ResourceFrontFace("",cornerSymbols,0,Symbol.FUNGUS), new RegularBackFace("",symbols ),"");
        assertEquals(resourceCard.toString(), resourceDeck.getBoardCard().get(DeckPosition.SECOND_CARD).toString());
    }
    @Test
    public void getAchievementBoardCard()
    {
        Boolean test = true;
        AchievementDeck achievementDeck = new AchievementDeck(test);
        Map<Symbol,Integer> scoreRequirements = new HashMap<>();
        for (Symbol symbol : Symbol.values()) {
            if(symbol != Symbol.PATTERN1F)
                scoreRequirements.put(symbol, 0);
        }
        scoreRequirements.put(Symbol.PATTERN1F, 1);
        AchievementCard achievementCard = new AchievementCard(new AchievementFrontFace("",scoreRequirements,2), new EmptyCardFace(""),"");
        assertEquals(achievementCard.toString(), achievementDeck.getBoardCard().get(DeckPosition.FIRST_CARD).toString());
        scoreRequirements = new HashMap<>();
        for (Symbol symbol : Symbol.values()) {
            if(symbol != Symbol.PATTERN2P)
                scoreRequirements.put(symbol, 0);
        }
        scoreRequirements.put(Symbol.PATTERN2P, 1);
        achievementCard = new AchievementCard(new AchievementFrontFace("",scoreRequirements,2), new EmptyCardFace(""),"");
        assertEquals(achievementCard.toString(), achievementDeck.getBoardCard().get(DeckPosition.SECOND_CARD).toString());
    }

}
