package Server.Manuscript;

import Server.Card.*;
import Server.Enums.CardCorners;
import Server.Enums.Face;
import Server.Enums.Symbol;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TestManuscript {
    @Test
    public void testNewManuscript() {
        Map<CardCorners, Symbol> cornerSymbols = new HashMap<>();
        cornerSymbols.put(CardCorners.TOP_LEFT, Symbol.BOTTLE);
        cornerSymbols.put(CardCorners.TOP_RIGHT, Symbol.PLANT);
        cornerSymbols.put(CardCorners.BOTTOM_LEFT, Symbol.EMPTY);
        cornerSymbols.put(CardCorners.BOTTOM_RIGHT, Symbol.NONE);
        List<Symbol> symbols = new ArrayList<>();
        symbols.add(Symbol.ANIMAL);
        StartingFrontFace startingFrontFace = new StartingFrontFace("startingfront.jpeg", cornerSymbols, symbols);
        CornerCardFace startingBackFace = new CornerCardFace("startingback.jpeg", new HashMap<>());
        StartingCard startingCard = new StartingCard(startingFrontFace, startingBackFace);
        Manuscript manuscript = new Manuscript(startingCard, Face.FRONT);
        assertEquals(startingFrontFace, manuscript.getCardByCoord(0, 0));
        for (Symbol symbol : Symbol.values()) {
            if (symbol == Symbol.BOTTLE || symbol == Symbol.PLANT || symbol == Symbol.ANIMAL) {
                assertEquals(1, manuscript.getSymbolCount(symbol));
            } else if (symbol != Symbol.EMPTY && symbol != Symbol.NONE) {
                assertEquals(0, manuscript.getSymbolCount(symbol));
            }
        }
    }

    @Test
    public void testCoordinates(){
        StartingFrontFace startingFrontFace = new StartingFrontFace("startingfront.jpeg", new HashMap<>(), new ArrayList<>());
        CornerCardFace startingBackFace = new CornerCardFace("startingback.jpeg", new HashMap<>());
        ResourceFrontFace resourceFrontFace = new ResourceFrontFace("resourcefront.jpeg", new HashMap<>(), 0, Symbol.ANIMAL);
        RegularBackFace goldBackFace = new RegularBackFace("regularback2.jpeg", new ArrayList<>(List.of(Symbol.BUG)));
        ResourceFrontFace resourceFrontFace2 = new ResourceFrontFace("resourcefront2.jpeg", new HashMap<>(), 0, Symbol.FUNGUS);
        StartingCard startingCard = new StartingCard(startingFrontFace, startingBackFace);
        Manuscript manuscript = new Manuscript(startingCard, Face.FRONT);
        manuscript.addCard(1,1, resourceFrontFace,1);
        manuscript.addCard(0,2, goldBackFace,1);
        manuscript.addCard(-1,1, resourceFrontFace2,1);
        assertEquals(resourceFrontFace ,manuscript.getCardByCoord(1,1));
        assertEquals(goldBackFace ,manuscript.getCardByCoord(0,2));
        assertEquals(resourceFrontFace2 ,manuscript.getCardByCoord(-1,1));

    }
    @Test
    public void testSymbolCount() {
        Map<CardCorners, Symbol> cornerSymbols = new HashMap<>();
        cornerSymbols.put(CardCorners.TOP_LEFT, Symbol.PLANT);
        cornerSymbols.put(CardCorners.TOP_RIGHT, Symbol.FUNGUS);
        cornerSymbols.put(CardCorners.BOTTOM_LEFT, Symbol.EMPTY);
        cornerSymbols.put(CardCorners.BOTTOM_RIGHT, Symbol.BOTTLE);
        List<Symbol> symbols = new ArrayList<>();
        symbols.add(Symbol.FUNGUS);
        CornerCardFace backface = new CornerCardFace("backface.jpeg", new HashMap<>());
        StartingFrontFace frontface = new StartingFrontFace("frontface.jpeg", cornerSymbols, symbols);
        StartingCard card = new StartingCard(frontface, backface);
        Manuscript manuscript = new Manuscript(card, Face.FRONT);
        Map<CardCorners, Symbol> cornerSymbols2 = new HashMap<>();
        cornerSymbols2.put(CardCorners.TOP_LEFT, Symbol.BUG);
        cornerSymbols2.put(CardCorners.TOP_RIGHT, Symbol.NONE);
        cornerSymbols2.put(CardCorners.BOTTOM_LEFT, Symbol.NONE);
        //cornerSymbols2.put(CardCorners.BOTTOM_RIGHT, Symbol.NONE);
        ResourceFrontFace frontface2 = new ResourceFrontFace("frontface2.jpeg", cornerSymbols2, 0, Symbol.BUG);
        manuscript.addCard(-1, -1, frontface2, 1);
        Map<CardCorners, Symbol> cornerSymbols3 = new HashMap<>();
        cornerSymbols3.put(CardCorners.TOP_LEFT, Symbol.BUG);
        cornerSymbols3.put(CardCorners.TOP_RIGHT, Symbol.ANIMAL);
        cornerSymbols3.put(CardCorners.BOTTOM_LEFT, Symbol.NONE);
        cornerSymbols3.put(CardCorners.BOTTOM_RIGHT, Symbol.FUNGUS);
        GoldFrontFace frontface3 = new GoldFrontFace("frontface3.jpeg", cornerSymbols3, 0, new HashMap<>(), new HashMap<>(), Symbol.BUG);
        manuscript.addCard(1, -1, frontface3, 2);
        Map<CardCorners, Symbol> cornerSymbols4 = new HashMap<>();
        cornerSymbols4.put(CardCorners.TOP_LEFT, Symbol.FUNGUS);
        cornerSymbols4.put(CardCorners.TOP_RIGHT, Symbol.BOTTLE);
        cornerSymbols4.put(CardCorners.BOTTOM_LEFT, Symbol.PLANT);
        cornerSymbols4.put(CardCorners.BOTTOM_RIGHT, Symbol.ANIMAL);
        ResourceFrontFace frontface4 = new ResourceFrontFace("frontface4.jpeg", cornerSymbols4, 0, Symbol.FUNGUS);
        manuscript.addCard(2, 0, frontface4, 3);
        for (Symbol symbol : Symbol.values()) {
            if (symbol == Symbol.FUNGUS) {
                assertEquals(4, manuscript.getSymbolCount(symbol));
            } else if (symbol == Symbol.BUG) {
                assertEquals(2, manuscript.getSymbolCount(symbol));
            } else if (symbol == Symbol.ANIMAL) {
                assertEquals(1, manuscript.getSymbolCount(symbol));
            } else if (symbol == Symbol.PLANT) {
                assertEquals(2, manuscript.getSymbolCount(symbol));
            } else if (symbol == Symbol.BOTTLE) {
                assertEquals(1, manuscript.getSymbolCount(symbol));
            } else if (symbol != Symbol.NONE && symbol != Symbol.EMPTY) {
                assertEquals(0, manuscript.getSymbolCount(symbol));
            }
        }
    }
    @Test
    public void testPoints()
    {
        Map<CardCorners, Symbol> cornerSymbols = new HashMap<>();
        List<Symbol> symbols = new ArrayList<>();
        symbols.add(Symbol.ANIMAL);
        symbols.add(Symbol.BUG);
        StartingFrontFace startingFrontFace = new StartingFrontFace("startingfront.jpeg", cornerSymbols, symbols);
        CornerCardFace startingBackFace = new CornerCardFace("startingback.jpeg", new HashMap<>());
        StartingCard startingCard = new StartingCard(startingFrontFace, startingBackFace);
        Manuscript manuscript = new Manuscript(startingCard, Face.FRONT);
        Map<CardCorners, Symbol> cornerSymbols2 = new HashMap<>();
        cornerSymbols2.put(CardCorners.TOP_LEFT, Symbol.FUNGUS);
        cornerSymbols2.put(CardCorners.TOP_RIGHT, Symbol.NONE);
        cornerSymbols2.put(CardCorners.BOTTOM_LEFT, Symbol.FUNGUS);
        cornerSymbols2.put(CardCorners.BOTTOM_RIGHT, Symbol.EMPTY);
        ResourceFrontFace frontface2 = new ResourceFrontFace("frontface2.jpeg", cornerSymbols2, 0, Symbol.BUG);
        manuscript.addCard(1, 1, frontface2, 1);
        Map<CardCorners, Symbol> cornerSymbols3 = new HashMap<>();
        cornerSymbols3.put(CardCorners.TOP_LEFT, Symbol.EMPTY);
        cornerSymbols3.put(CardCorners.TOP_RIGHT, Symbol.NONE);
        cornerSymbols3.put(CardCorners.BOTTOM_LEFT, Symbol.PLANT);
        cornerSymbols3.put(CardCorners.BOTTOM_RIGHT, Symbol.EMPTY);
        ResourceFrontFace frontface3 = new ResourceFrontFace("frontface3.jpeg", cornerSymbols3, 0, Symbol.FUNGUS);
        manuscript.addCard(2, 0, frontface3, 2);
        Map<CardCorners, Symbol> cornerSymbols4 = new HashMap<>();
        cornerSymbols4.put(CardCorners.TOP_LEFT, Symbol.PLANT);
        cornerSymbols4.put(CardCorners.TOP_RIGHT, Symbol.EMPTY);
        cornerSymbols4.put(CardCorners.BOTTOM_LEFT, Symbol.EMPTY);
        cornerSymbols4.put(CardCorners.BOTTOM_RIGHT, Symbol.EMPTY);
        ResourceFrontFace frontface4 = new ResourceFrontFace("frontface4.jpeg", cornerSymbols4, 0, Symbol.FUNGUS);
        manuscript.addCard(1, -1, frontface4, 3);
        Map<CardCorners, Symbol> cornerSymbols5 = new HashMap<>();
        cornerSymbols5.put(CardCorners.TOP_LEFT, Symbol.FUNGUS);
        cornerSymbols5.put(CardCorners.TOP_RIGHT, Symbol.NONE);
        cornerSymbols5.put(CardCorners.BOTTOM_LEFT, Symbol.EMPTY);
        cornerSymbols5.put(CardCorners.BOTTOM_RIGHT, Symbol.EMPTY);
        ResourceFrontFace frontface5 = new ResourceFrontFace("frontface5.jpeg", cornerSymbols5, 0, Symbol.PLANT);
        manuscript.addCard(3, -1, frontface5, 4);
        Map<CardCorners, Symbol> cornerSymbols6 = new HashMap<>();
        cornerSymbols6.put(CardCorners.TOP_LEFT, Symbol.PARCHMENT);
        cornerSymbols6.put(CardCorners.TOP_RIGHT, Symbol.BUG);
        cornerSymbols6.put(CardCorners.BOTTOM_LEFT, Symbol.EMPTY);
        cornerSymbols6.put(CardCorners.BOTTOM_RIGHT, Symbol.FUNGUS);
        GoldFrontFace frontface6 = new GoldFrontFace("frontface6.jpeg", cornerSymbols6, 0, new HashMap<>(), new HashMap<>(), Symbol.PLANT);
        manuscript.addCard(4, -2, frontface6, 5);
        Map<CardCorners, Symbol> cornerSymbols7 = new HashMap<>();
        cornerSymbols7.put(CardCorners.TOP_LEFT, Symbol.EMPTY);
        cornerSymbols7.put(CardCorners.TOP_RIGHT, Symbol.BUG);
        cornerSymbols7.put(CardCorners.BOTTOM_LEFT, Symbol.NONE);
        cornerSymbols7.put(CardCorners.BOTTOM_RIGHT, Symbol.EMPTY);
        GoldFrontFace frontface7 = new GoldFrontFace("frontface7.jpeg", cornerSymbols7, 0, new HashMap<>(), new HashMap<>(), Symbol.PLANT);
        manuscript.addCard(-1, -1, frontface7, 6);
        Map<CardCorners, Symbol> cornerSymbols8 = new HashMap<>();
        cornerSymbols8.put(CardCorners.TOP_LEFT, Symbol.FUNGUS);
        cornerSymbols8.put(CardCorners.TOP_RIGHT, Symbol.NONE);
        cornerSymbols8.put(CardCorners.BOTTOM_LEFT, Symbol.FUNGUS);
        cornerSymbols8.put(CardCorners.BOTTOM_RIGHT, Symbol.EMPTY);
        ResourceFrontFace frontface8 = new ResourceFrontFace("frontface8.jpeg", cornerSymbols8, 0, Symbol.FUNGUS);
        manuscript.addCard(0, -2, frontface8, 7);
        Map<CardCorners, Symbol> cornerSymbols9 = new HashMap<>();
        cornerSymbols9.put(CardCorners.TOP_LEFT, Symbol.EMPTY);
        cornerSymbols9.put(CardCorners.TOP_RIGHT, Symbol.ANIMAL);
        cornerSymbols9.put(CardCorners.BOTTOM_LEFT, Symbol.EMPTY);
        cornerSymbols9.put(CardCorners.BOTTOM_RIGHT, Symbol.NONE);
        ResourceFrontFace frontface9 = new ResourceFrontFace("frontface9.jpeg", cornerSymbols9, 0, Symbol.FUNGUS);
        manuscript.addCard(-2, 0, frontface9, 8);
        Map<CardCorners, Symbol> cornerSymbols10 = new HashMap<>();
        cornerSymbols10.put(CardCorners.TOP_LEFT, Symbol.EMPTY);
        cornerSymbols10.put(CardCorners.TOP_RIGHT, Symbol.NONE);
        cornerSymbols10.put(CardCorners.BOTTOM_LEFT, Symbol.EMPTY);
        cornerSymbols10.put(CardCorners.BOTTOM_RIGHT, Symbol.ANIMAL);
        ResourceFrontFace frontface10 = new ResourceFrontFace("frontface10.jpeg", cornerSymbols10, 0, Symbol.BUG);
        manuscript.addCard(-1, 1, frontface10, 9);
        Map<CardCorners, Symbol> cornerSymbols11 = new HashMap<>();
        cornerSymbols11.put(CardCorners.TOP_LEFT, Symbol.NONE);
        cornerSymbols11.put(CardCorners.TOP_RIGHT, Symbol.EMPTY);
        cornerSymbols11.put(CardCorners.BOTTOM_LEFT, Symbol.EMPTY);
        cornerSymbols11.put(CardCorners.BOTTOM_RIGHT, Symbol.FUNGUS);
        ResourceFrontFace frontface11 = new ResourceFrontFace("frontface11.jpeg", cornerSymbols11, 0, Symbol.FUNGUS);
        manuscript.addCard(-2, 2, frontface11, 10);
        Map<CardCorners, Symbol> cornerSymbols12 = new HashMap<>();
        cornerSymbols12.put(CardCorners.TOP_LEFT, Symbol.EMPTY);
        cornerSymbols12.put(CardCorners.TOP_RIGHT, Symbol.NONE);
        cornerSymbols12.put(CardCorners.BOTTOM_LEFT, Symbol.ANIMAL);
        cornerSymbols12.put(CardCorners.BOTTOM_RIGHT, Symbol.EMPTY);
        ResourceFrontFace frontface12 = new ResourceFrontFace("frontface12.jpeg", cornerSymbols12, 0, Symbol.ANIMAL);
        manuscript.addCard(-1, 3, frontface12, 11);
        Map<Symbol, Integer> scoreRequirements = new HashMap<>();
        scoreRequirements.put(Symbol.PATTERN1F, 2);
        AchievementFrontFace achievementFrontFace1 = new AchievementFrontFace("achievementfront1.jpeg", scoreRequirements, 0);
        EmptyCardFace achievementBackFace1 = new EmptyCardFace("achievementback1.jpeg");
        AchievementCard achievementCard1 = new AchievementCard(achievementFrontFace1, achievementBackFace1);
        Map<Symbol, Integer> scoreRequirements2 = new HashMap<>();
        scoreRequirements2.put(Symbol.PATTERN2P, 2);
        AchievementFrontFace achievementFrontFace2 = new AchievementFrontFace("achievementfront2.jpeg", scoreRequirements2, 0);
        AchievementCard achievementCard2 = new AchievementCard(achievementFrontFace2, achievementBackFace1);
        Map<Symbol, Integer> scoreRequirements3 = new HashMap<>();
        scoreRequirements3.put(Symbol.PATTERN1A, 2);
        AchievementFrontFace achievementFrontFace3 = new AchievementFrontFace("achievementfront3.jpeg", scoreRequirements3, 0);
        AchievementCard achievementCard3 = new AchievementCard(achievementFrontFace3, achievementBackFace1);
        Map<Symbol, Integer> scoreRequirements4 = new HashMap<>();
        scoreRequirements4.put(Symbol.PATTERN2B, 2);
        AchievementFrontFace achievementFrontFace4 = new AchievementFrontFace("achievementfront4.jpeg", scoreRequirements4, 0);
        AchievementCard achievementCard4 = new AchievementCard(achievementFrontFace4, achievementBackFace1);
        Map<Symbol, Integer> scoreRequirements5 = new HashMap<>();
        scoreRequirements5.put(Symbol.PATTERN3, 3);
        AchievementFrontFace achievementFrontFace5 = new AchievementFrontFace("achievementfront5.jpeg", scoreRequirements5, 0);
        AchievementCard achievementCard5 = new AchievementCard(achievementFrontFace5, achievementBackFace1);
        Map<Symbol, Integer> scoreRequirements6 = new HashMap<>();
        scoreRequirements6.put(Symbol.PATTERN4, 3);
        AchievementFrontFace achievementFrontFace6 = new AchievementFrontFace("achievementfront6.jpeg", scoreRequirements6, 0);
        AchievementCard achievementCard6 = new AchievementCard(achievementFrontFace6, achievementBackFace1);
        Map<Symbol, Integer> scoreRequirements7 = new HashMap<>();
        scoreRequirements7.put(Symbol.PATTERN5, 3);
        AchievementFrontFace achievementFrontFace7 = new AchievementFrontFace("achievementfront7.jpeg", scoreRequirements7, 0);
        AchievementCard achievementCard7 = new AchievementCard(achievementFrontFace7, achievementBackFace1);
        Map<Symbol, Integer> scoreRequirements8 = new HashMap<>();
        scoreRequirements8.put(Symbol.PATTERN6, 3);
        AchievementFrontFace achievementFrontFace8 = new AchievementFrontFace("achievementfront8.jpeg", scoreRequirements8, 0);
        AchievementCard achievementCard8 = new AchievementCard(achievementFrontFace8, achievementBackFace1);
        Map<Symbol, Integer> scoreRequirements9 = new HashMap<>();
        scoreRequirements9.put(Symbol.FUNGUS, 3);
        AchievementFrontFace achievementFrontFace9 = new AchievementFrontFace("achievementfront9.jpeg", scoreRequirements9, 0);
        AchievementCard achievementCard9 = new AchievementCard(achievementFrontFace9, achievementBackFace1);
        Map<Symbol, Integer> scoreRequirements10 = new HashMap<>();
        scoreRequirements10.put(Symbol.PLANT, 3);
        AchievementFrontFace achievementFrontFace10 = new AchievementFrontFace("achievementfront10.jpeg", scoreRequirements10, 0);
        AchievementCard achievementCard10 = new AchievementCard(achievementFrontFace10, achievementBackFace1);
        Map<Symbol, Integer> scoreRequirements11 = new HashMap<>();
        scoreRequirements11.put(Symbol.ANIMAL, 3);
        AchievementFrontFace achievementFrontFace11 = new AchievementFrontFace("achievementfront11.jpeg", scoreRequirements11, 0);
        AchievementCard achievementCard11 = new AchievementCard(achievementFrontFace11, achievementBackFace1);
        Map<Symbol, Integer> scoreRequirements12 = new HashMap<>();
        scoreRequirements12.put(Symbol.BUG, 3);
        AchievementFrontFace achievementFrontFace12 = new AchievementFrontFace("achievementfront12.jpeg", scoreRequirements12, 0);
        AchievementCard achievementCard12 = new AchievementCard(achievementFrontFace12, achievementBackFace1);
        Map<Symbol, Integer> scoreRequirements13 = new HashMap<>();
        scoreRequirements13.put(Symbol.PARCHMENT, 1);
        scoreRequirements13.put(Symbol.BOTTLE, 1);
        scoreRequirements13.put(Symbol.QUILL, 1);
        AchievementFrontFace achievementFrontFace13 = new AchievementFrontFace("achievementfront13.jpeg", scoreRequirements13, 0);
        AchievementCard achievementCard13 = new AchievementCard(achievementFrontFace13, achievementBackFace1);
        Map<Symbol, Integer> scoreRequirements14 = new HashMap<>();
        scoreRequirements14.put(Symbol.PARCHMENT, 2);
        AchievementFrontFace achievementFrontFace14 = new AchievementFrontFace("achievementfront14.jpeg", scoreRequirements14, 0);
        AchievementCard achievementCard14 = new AchievementCard(achievementFrontFace14, achievementBackFace1);
        Map<Symbol, Integer> scoreRequirements15 = new HashMap<>();
        scoreRequirements15.put(Symbol.BOTTLE, 2);
        AchievementFrontFace achievementFrontFace15 = new AchievementFrontFace("achievementfront15.jpeg", scoreRequirements15, 0);
        AchievementCard achievementCard15 = new AchievementCard(achievementFrontFace15, achievementBackFace1);
        Map<Symbol, Integer> scoreRequirements16 = new HashMap<>();
        scoreRequirements16.put(Symbol.QUILL, 2);
        AchievementFrontFace achievementFrontFace16 = new AchievementFrontFace("achievementfront16.jpeg", scoreRequirements16, 0);
        AchievementCard achievementCard16 = new AchievementCard(achievementFrontFace16, achievementBackFace1);
        assertEquals(2, manuscript.calculatePoints(achievementCard1));
        assertEquals(0, manuscript.calculatePoints(achievementCard2));
        assertEquals(0, manuscript.calculatePoints(achievementCard3));
        assertEquals(0, manuscript.calculatePoints(achievementCard4));
        assertEquals(3, manuscript.calculatePoints(achievementCard5));
        assertEquals(0, manuscript.calculatePoints(achievementCard6));
        assertEquals(0, manuscript.calculatePoints(achievementCard7));
        assertEquals(0, manuscript.calculatePoints(achievementCard8));
        assertEquals(4, manuscript.calculatePoints(achievementCard9));
        assertEquals(0, manuscript.calculatePoints(achievementCard10));
        assertEquals(2, manuscript.calculatePoints(achievementCard11));
        assertEquals(2, manuscript.calculatePoints(achievementCard12));
        assertEquals(0, manuscript.calculatePoints(achievementCard13));
        assertEquals(0, manuscript.calculatePoints(achievementCard14));
        assertEquals(0, manuscript.calculatePoints(achievementCard15));
        assertEquals(0, manuscript.calculatePoints(achievementCard16));
    }

    @Test
    public void testPlayability(){
        Map<CardCorners, Symbol> startingCornerSymbols = new HashMap<>();
        startingCornerSymbols.put(CardCorners.TOP_LEFT, Symbol.BOTTLE);
        startingCornerSymbols.put(CardCorners.TOP_RIGHT, Symbol.PLANT);
        startingCornerSymbols.put(CardCorners.BOTTOM_LEFT, Symbol.EMPTY);
        startingCornerSymbols.put(CardCorners.BOTTOM_RIGHT, Symbol.FUNGUS);
        StartingFrontFace startingFrontFace = new StartingFrontFace("startingfront.jpeg", startingCornerSymbols, new ArrayList<>());
        CornerCardFace startingBackFace = new CornerCardFace("startingback.jpeg", new HashMap<>());
        StartingCard startingCard = new StartingCard(startingFrontFace, startingBackFace);
        Manuscript manuscript = new Manuscript(startingCard, Face.FRONT);
        Map<CardCorners, Symbol> cornerSymbols = new HashMap<>();
        cornerSymbols.put(CardCorners.TOP_LEFT, Symbol.BUG);
        cornerSymbols.put(CardCorners.TOP_RIGHT, Symbol.NONE);
        cornerSymbols.put(CardCorners.BOTTOM_LEFT, Symbol.NONE);
        cornerSymbols.put(CardCorners.BOTTOM_RIGHT, Symbol.NONE);
        ResourceFrontFace frontface = new ResourceFrontFace("frontface.jpeg", cornerSymbols, 0, Symbol.BUG);
        manuscript.addCard(1, 1, frontface, 1);
        Map<CardCorners, Symbol> cornerSymbols2 = new HashMap<>();
        cornerSymbols2.put(CardCorners.TOP_LEFT, Symbol.BUG);
        cornerSymbols2.put(CardCorners.TOP_RIGHT, Symbol.ANIMAL);
        cornerSymbols2.put(CardCorners.BOTTOM_LEFT, Symbol.NONE);
        cornerSymbols2.put(CardCorners.BOTTOM_RIGHT, Symbol.FUNGUS);
        GoldFrontFace frontface2 = new GoldFrontFace("frontface2.jpeg", cornerSymbols2, 0, new HashMap<>(), new HashMap<>(), Symbol.BUG);
        //you cannot have more than one corner on the same card
        assertFalse(manuscript.isPlaceable(-1, 0, frontface2));
        assertFalse(manuscript.isPlaceable(0, 1, frontface2));
        //you cannot place a card on another one
        assertFalse(manuscript.isPlaceable(0, 0, frontface2));
        //a card cannot be placed on NONE
        assertFalse(manuscript.isPlaceable(2, 2, frontface2));
        //you can, however, place a card that has a NONE corner on another one
        Map<CardCorners, Symbol> cornerSymbols3 = new HashMap<>();
        cornerSymbols3.put(CardCorners.TOP_LEFT, Symbol.NONE);
        cornerSymbols3.put(CardCorners.TOP_RIGHT, Symbol.NONE);
        cornerSymbols3.put(CardCorners.BOTTOM_LEFT, Symbol.NONE);
        cornerSymbols3.put(CardCorners.BOTTOM_RIGHT, Symbol.NONE);
        ResourceFrontFace frontface3 = new ResourceFrontFace("frontface3.jpeg", cornerSymbols3, 0, Symbol.BUG);
        assertTrue(manuscript.isPlaceable(-1, -1, frontface3));
        //you need to meet the placement requirements
        Map<CardCorners, Symbol> cornerSymbols4 = new HashMap<>();
        cornerSymbols4.put(CardCorners.TOP_LEFT, Symbol.BUG);
        cornerSymbols4.put(CardCorners.TOP_RIGHT, Symbol.NONE);
        cornerSymbols4.put(CardCorners.BOTTOM_LEFT, Symbol.NONE);
        cornerSymbols4.put(CardCorners.BOTTOM_RIGHT, Symbol.NONE);
        Map<Symbol, Integer> placementRequirements = new HashMap<>();
        placementRequirements.put(Symbol.FUNGUS, 3);
        GoldFrontFace frontface4 = new GoldFrontFace("frontface4.jpeg", cornerSymbols4, 0, placementRequirements, new HashMap<>(), Symbol.BUG);
        assertFalse(manuscript.isPlaceable(-1, -1, frontface4));
    }
}