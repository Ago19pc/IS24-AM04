package Server.Card;

import Server.Enums.CardCorners;

import Server.Enums.Face;
import Server.Enums.Symbol;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TestGoldCard {
    @Test
public void testGetFace() {
        GoldFrontFace frontFace = new GoldFrontFace("image1.jpg", Map.of(CardCorners.TOP_LEFT, Symbol.NONE, CardCorners.TOP_RIGHT, Symbol.NONE,
                CardCorners.BOTTOM_RIGHT, Symbol.NONE, CardCorners.BOTTOM_LEFT, Symbol.NONE),
                1, Map.of(Symbol.NONE, 1), Map.of(Symbol.NONE, 1), Symbol.FUNGUS);
        RegularBackFace backFace = new RegularBackFace("image2.jpg", List.of(Symbol.NONE, Symbol.NONE, Symbol.NONE, Symbol.NONE));
        GoldCard goldCard = new GoldCard(frontFace, backFace, "1.jpeg");
        assertEquals(frontFace, goldCard.getFace(Face.FRONT));
        assertEquals(backFace, goldCard.getFace(Face.BACK));
    }

}
