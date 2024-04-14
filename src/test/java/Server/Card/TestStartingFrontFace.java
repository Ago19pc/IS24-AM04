package Server.Card;

import Server.Enums.CardCorners;

import Server.Enums.Symbol;
import org.junit.jupiter.api.Test;


import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;





public class TestStartingFrontFace {
    @Test
    public void testGetCenterSymbols(){
        StartingFrontFace startingFrontFace = new StartingFrontFace("image1.jpg", Map.of(
                CardCorners.TOP_LEFT, Symbol.ANIMAL,
                CardCorners.TOP_RIGHT, Symbol.PLANT,
                CardCorners.BOTTOM_RIGHT, Symbol.BUG,
                CardCorners.BOTTOM_LEFT, Symbol.FUNGUS), List.of(Symbol.NONE));
        assertEquals(List.of(Symbol.NONE), startingFrontFace.getCenterSymbols());


    }

}
