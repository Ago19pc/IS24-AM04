package Server.Card;
import Server.Deck.AchievementDeck;
import Server.Enums.CardCorners;
import Server.Enums.DeckPosition;
import Server.Enums.Face;
import Server.Enums.Symbol;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestAchievementCard {


    @Test
    public void testGetFace() {
        Map map = new HashMap<Symbol, Integer>();
        AchievementFrontFace frontFace = new AchievementFrontFace("image1.jpg", map, 1);
        EmptyCardFace backFace = new EmptyCardFace("image2.jpg");
        AchievementCard achievementCard = new AchievementCard(frontFace, backFace);
        assertEquals(frontFace, achievementCard.getFace(Face.FRONT));
        assertEquals(backFace, achievementCard.getFace(Face.BACK));

    }

    /*
    @Test
    public void testGetCornerFace() {
        AchievementFrontFace frontFace = new AchievementFrontFace("image1.jpg", "name1", "description1");
        EmptyCardFace backFace = new EmptyCardFace("image2.jpg");
        AchievementCard achievementCard = new AchievementCard(frontFace, backFace);
        assertThrows(UnsupportedOperationException.class, () -> achievementCard.getCornerFace(Face.FRONT));
    }

     */



}
