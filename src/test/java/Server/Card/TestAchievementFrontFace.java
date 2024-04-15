package Server.Card;
import Server.Enums.Symbol;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TestAchievementFrontFace {
    @Test
    public void testGetScoreRequirements() {
        Map<Symbol, Integer> map = new HashMap<>();
        AchievementFrontFace frontFace = new AchievementFrontFace("image1.jpg", map , 0);
        assertEquals(map, frontFace.getScoreRequirements());
    }
    @Test
    public void testGetScore() {
        AchievementFrontFace frontFace = new AchievementFrontFace("image1.jpg", new HashMap<>(), 0);
        assertEquals(0, frontFace.getScore());
    }
}
