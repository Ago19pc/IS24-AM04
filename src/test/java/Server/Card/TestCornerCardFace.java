package Server.Card;
import Server.Enums.CardCorners;
import Server.Enums.Symbol;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestCornerCardFace {
    @Test
    public void testGetImageURI() {
        Map<CardCorners, Symbol> cornerSymbols = new HashMap<>();
        cornerSymbols.put(CardCorners.TOP_LEFT, Symbol.BOTTLE);
        cornerSymbols.put(CardCorners.TOP_RIGHT, Symbol.BUG);
        cornerSymbols.put(CardCorners.BOTTOM_LEFT, Symbol.ANIMAL);
        cornerSymbols.put(CardCorners.BOTTOM_RIGHT, Symbol.NONE);
        CornerCardFace cornerCardFace = new CornerCardFace("image1.jpg", cornerSymbols);
        assertEquals("image1.jpg", cornerCardFace.getImageURI());
    }


    @Test
    public void testGetCornerSymbols() {
        Map<CardCorners, Symbol> cornerSymbols = new HashMap<>();
        cornerSymbols.put(CardCorners.TOP_LEFT, Symbol.BUG);
        cornerSymbols.put(CardCorners.TOP_RIGHT, Symbol.FUNGUS);
        cornerSymbols.put(CardCorners.BOTTOM_LEFT, Symbol.PARCHMENT);
        cornerSymbols.put(CardCorners.BOTTOM_RIGHT, Symbol.EMPTY);
        CornerCardFace cornerCardFace = new CornerCardFace("image1.jpg", cornerSymbols);
        assertEquals(cornerSymbols, cornerCardFace.getCornerSymbols());
    }


    @Test
    public void testSetPlacementTurn() {
        Map<CardCorners, Symbol> cornerSymbols = new HashMap<>();
        cornerSymbols.put(CardCorners.TOP_LEFT, Symbol.BOTTLE);
        cornerSymbols.put(CardCorners.TOP_RIGHT, Symbol.QUILL);
        cornerSymbols.put(CardCorners.BOTTOM_LEFT, Symbol.NONE);
        cornerSymbols.put(CardCorners.BOTTOM_RIGHT, Symbol.EMPTY);
        CornerCardFace cornerCardFace = new CornerCardFace("image1.jpg", cornerSymbols);
        cornerCardFace.setPlacementTurn(1);
        assertEquals(1, cornerCardFace.getPlacementTurn());
    }


    @Test
    public void testGetXCoord() {
        Map<CardCorners, Symbol> cornerSymbols = new HashMap<>();
        cornerSymbols.put(CardCorners.TOP_LEFT, Symbol.ANIMAL);
        cornerSymbols.put(CardCorners.TOP_RIGHT, Symbol.PLANT);
        cornerSymbols.put(CardCorners.BOTTOM_LEFT, Symbol.EMPTY);
        cornerSymbols.put(CardCorners.BOTTOM_RIGHT, Symbol.EMPTY);
        CornerCardFace cornerCardFace = new CornerCardFace("image1.jpg", cornerSymbols);
        cornerCardFace.setXCoord(1);
        assertEquals(1, cornerCardFace.getXCoord());
    }


    @Test
    public void testGetYCoord() {
        Map<CardCorners, Symbol> cornerSymbols = new HashMap<>();
        cornerSymbols.put(CardCorners.TOP_LEFT, Symbol.BOTTLE);
        cornerSymbols.put(CardCorners.TOP_RIGHT, Symbol.BUG);
        cornerSymbols.put(CardCorners.BOTTOM_LEFT, Symbol.ANIMAL);
        cornerSymbols.put(CardCorners.BOTTOM_RIGHT, Symbol.NONE);
        CornerCardFace cornerCardFace = new CornerCardFace("image1.jpg", cornerSymbols);
        cornerCardFace.setYCoord(2);
        assertEquals(2, cornerCardFace.getYCoord());

    }
    @Test
    public void testSetXCoord() {
        Map<CardCorners, Symbol> cornerSymbols = new HashMap<>();
        cornerSymbols.put(CardCorners.TOP_LEFT, Symbol.BOTTLE);
        cornerSymbols.put(CardCorners.TOP_RIGHT, Symbol.BUG);
        cornerSymbols.put(CardCorners.BOTTOM_LEFT, Symbol.ANIMAL);
        cornerSymbols.put(CardCorners.BOTTOM_RIGHT, Symbol.NONE);
        CornerCardFace cornerCardFace = new CornerCardFace("image1.jpg", cornerSymbols);
        cornerCardFace.setXCoord(1);
        assertEquals(1, cornerCardFace.getXCoord());
    }
    @Test
    public void testSetYCoord() {
        Map<CardCorners, Symbol> cornerSymbols = new HashMap<>();
        cornerSymbols.put(CardCorners.TOP_LEFT, Symbol.BOTTLE);
        cornerSymbols.put(CardCorners.TOP_RIGHT, Symbol.BUG);
        cornerSymbols.put(CardCorners.BOTTOM_LEFT, Symbol.ANIMAL);
        cornerSymbols.put(CardCorners.BOTTOM_RIGHT, Symbol.NONE);
        CornerCardFace cornerCardFace = new CornerCardFace("image1.jpg", cornerSymbols);
        cornerCardFace.setYCoord(2);
        assertEquals(2, cornerCardFace.getYCoord());
    }
    @Test
    public void getPlacementTurn() {
        Map<CardCorners, Symbol> cornerSymbols = new HashMap<>();
        cornerSymbols.put(CardCorners.TOP_LEFT, Symbol.BOTTLE);
        cornerSymbols.put(CardCorners.TOP_RIGHT, Symbol.BUG);
        cornerSymbols.put(CardCorners.BOTTOM_LEFT, Symbol.ANIMAL);
        cornerSymbols.put(CardCorners.BOTTOM_RIGHT, Symbol.NONE);
        CornerCardFace cornerCardFace = new CornerCardFace("image1.jpg", cornerSymbols);
        cornerCardFace.setPlacementTurn(1);
        assertEquals(1, cornerCardFace.getPlacementTurn());
    }


}
