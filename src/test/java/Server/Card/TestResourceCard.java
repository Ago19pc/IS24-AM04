package Server.Card;
import Server.Enums.Face;
import Server.Enums.Symbol;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestResourceCard {
    @Test
public void testGetFace() {
        ResourceFrontFace frontFace = new ResourceFrontFace("image1.jpg", new HashMap<>(), 1, Symbol.FUNGUS);
        RegularBackFace backFace = new RegularBackFace("image2.jpg", List.of(Symbol.NONE));
        ResourceCard resourceCard = new ResourceCard(frontFace, backFace);
        assertEquals(frontFace, resourceCard.getFace(Face.FRONT));
        assertEquals(backFace, resourceCard.getFace(Face.BACK));
    }

    @Test
    public void testGetCornerFace() {
        ResourceFrontFace frontFace = new ResourceFrontFace("image1.jpg", new HashMap<>(), 1, Symbol.FUNGUS);
        RegularBackFace backFace = new RegularBackFace("image2.jpg", List.of(Symbol.NONE));
        ResourceCard resourceCard = new ResourceCard(frontFace, backFace);
        assertEquals(frontFace, resourceCard.getCornerFace(Face.FRONT));
        assertEquals(backFace, resourceCard.getCornerFace(Face.BACK));
    }

}
