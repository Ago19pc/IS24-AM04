package Server.Card;
import Server.Enums.Symbol;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestGoldFrontFace {
    @Test
public void testGetPlacementRequirements() {
        Map map = new HashMap<Symbol, Integer>();
        GoldFrontFace frontFace = new GoldFrontFace("image1.jpg", map, 0, new HashMap<>(), new HashMap<>());
        assertEquals(map, frontFace.getPlacementRequirements());
    }
    @Test
    public void testGetScoreRequirements() {
        Map map = new HashMap<Symbol, Integer>();
        GoldFrontFace frontFace = new GoldFrontFace("image1.jpg", map, 0, new HashMap<>(), new HashMap<>());
        assertEquals(map, frontFace.getScoreRequirements());
        //System.out.println(frontFace.getScoreRequirements().get(0));
    }


}
