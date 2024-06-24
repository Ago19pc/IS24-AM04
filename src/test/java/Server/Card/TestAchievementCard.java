package Server.Card;
import Server.Enums.Face;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestAchievementCard {


    @Test
    public void testGetFace() {
        AchievementFrontFace frontFace = new AchievementFrontFace("front-1.jpeg", new HashMap<>(), 1);
        EmptyCardFace backFace = new EmptyCardFace("back-1.jpeg");
        AchievementCard achievementCard = new AchievementCard(frontFace, backFace);
        assertEquals(frontFace, achievementCard.getFace(Face.FRONT));
        assertEquals(backFace, achievementCard.getFace(Face.BACK));

    }


    @Test
    public void testGetCornerFace() {
        AchievementFrontFace frontFace = new AchievementFrontFace("front-1.jpeg", new HashMap<>(), 0);
        EmptyCardFace backFace = new EmptyCardFace("back-1.jpeg");
        AchievementCard achievementCard = new AchievementCard(frontFace, backFace);
        UnsupportedOperationException exceptionThrown = assertThrows(
                UnsupportedOperationException.class, () -> achievementCard.getCornerFace(Face.FRONT)
        );
        assertEquals("Achievement cards do not have corner faces", exceptionThrown.getMessage());
    }





}
