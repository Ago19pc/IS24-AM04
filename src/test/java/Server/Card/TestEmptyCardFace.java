package Server.Card;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
public class TestEmptyCardFace {
    @Test
    public void testGetImageURI() {
        EmptyCardFace emptyCardFace = new EmptyCardFace("image1.jpg");
        assertEquals("image1.jpg", emptyCardFace.getImageURI());
    }
    @Test
    public void testGetScore() {
        EmptyCardFace emptyCardFace = new EmptyCardFace("imageURI");
        UnsupportedOperationException exceptionThrown = assertThrows(
                UnsupportedOperationException.class, emptyCardFace::getScore
        );
        assertEquals("Empty cards do not have scores", exceptionThrown.getMessage());
    }
    @Test
    public void testGetCornerSymbols() {
        EmptyCardFace emptyCardFace = new EmptyCardFace("imageURI");
        UnsupportedOperationException exceptionThrown = assertThrows(
                UnsupportedOperationException.class, emptyCardFace::getCornerSymbols
        );
        assertEquals("Empty cards do not have corner symbols", exceptionThrown.getMessage());
    }
    /*
    @Test
    public void testGetCenterSymbols() {
        EmptyCardFace emptyCardFace = new EmptyCardFace("imageURI");
        try {
            emptyCardFace.getCenterSymbols();
        } catch (UnsupportedOperationException e) {
            assertEquals("Empty cards do not have center symbols", e.getMessage());
        }
    }
    @Test
    public void testGetScoreRequirements() {
        EmptyCardFace emptyCardFace = new EmptyCardFace("imageURI");
        try {
            emptyCardFace.getScoreRequirements();
        } catch (UnsupportedOperationException e) {
            assertEquals("Empty cards do not have score requirements", e.getMessage());
        }
    }
    @Test
    public void testGetPlacementRequirements() {
        EmptyCardFace emptyCardFace = new EmptyCardFace("imageURI");
        try {
            emptyCardFace.getPlacementRequirements();
        } catch (UnsupportedOperationException e) {
            assertEquals("Empty cards do not have placement requirements", e.getMessage());
        }
    }
    @Test
    public void testGetXCoord() {
        EmptyCardFace emptyCardFace = new EmptyCardFace("imageURI");
        try {
            emptyCardFace.getXCoord();
        } catch (UnsupportedOperationException e) {
            assertEquals("Empty cards do not have coordinates", e.getMessage());
        }
    }*/
}
