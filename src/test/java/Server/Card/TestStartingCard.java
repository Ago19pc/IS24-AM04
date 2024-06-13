/*package Server.Card;

import Server.Enums.Face;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
public class TestStartingCard {
    @Test
    public void testGetFace() {
        StartingFrontFace frontFace = new StartingFrontFace("image1.jpg", new HashMap<>(), new ArrayList<>());
        CornerCardFace backFace = new CornerCardFace("image2.jpg", new HashMap<>());
        StartingCard card = new StartingCard(frontFace, backFace);
        assertEquals(frontFace, card.getFace(Face.FRONT));
        assertEquals(backFace, card.getFace(Face.BACK));
    }

    @Test
    public void testGetCornerFace() {
        StartingFrontFace frontFace = new StartingFrontFace("image1.jpg", new HashMap<>(), new ArrayList<>());
        CornerCardFace backFace = new CornerCardFace("image2.jpg", new HashMap<>());
        StartingCard card = new StartingCard(frontFace, backFace);
        assertEquals(frontFace, card.getCornerFace(Face.FRONT));
        assertEquals(backFace, card.getCornerFace(Face.BACK));
    }
}
*/