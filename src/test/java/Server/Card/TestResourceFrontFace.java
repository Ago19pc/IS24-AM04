package Server.Card;
import Server.Enums.Symbol;
import org.junit.jupiter.api.Test;

import java.util.HashMap;



import static org.junit.jupiter.api.Assertions.assertEquals;


public class TestResourceFrontFace {
    @Test
public void testGetScore() {
        ResourceFrontFace frontFace = new ResourceFrontFace("image1.jpg", new HashMap<>(), 0, Symbol.FUNGUS);
        assertEquals(0, frontFace.getScore());
    }

}
