package Server.Card;
import Server.Enums.CardCorners;
import Server.Enums.Symbol;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestGoldFrontFace {
    @Test
public void testGetPlacementRequirements() {
        Map<CardCorners, Symbol> map = new HashMap<>();
        Map<Symbol, Integer> map2 = new HashMap<>();
        GoldFrontFace frontFace = new GoldFrontFace("image1.jpg", map, 0, map2, new HashMap<>(), Symbol.FUNGUS);
        assertEquals(map2, frontFace.getPlacementRequirements());
    }
    @Test
    public void testGetScoreRequirements() {
        Map<CardCorners, Symbol> map = new HashMap<>();
        Map<Symbol, Integer> map2 = new HashMap<>();

        GoldFrontFace frontFace = new GoldFrontFace("image1.jpg", map, 0, new HashMap<>(), map2, Symbol.FUNGUS);
        assertEquals(map2, frontFace.getScoreRequirements());
        //System.out.println(frontFace.getScoreRequirements().get(0));
    }


}
