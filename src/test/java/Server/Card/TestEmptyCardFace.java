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

    @Test
    public void testGetCenterSymbols() {
        EmptyCardFace emptyCardFace = new EmptyCardFace("imageURI");
        UnsupportedOperationException exceptionThrown = assertThrows(
                UnsupportedOperationException.class, emptyCardFace::getCenterSymbols
        );
        assertEquals("Empty cards do not have center symbols", exceptionThrown.getMessage());
    }

    @Test
    public void testGetScoreRequirements() {
        EmptyCardFace emptyCardFace = new EmptyCardFace("imageURI");
        UnsupportedOperationException exceptionThrown = assertThrows(
                UnsupportedOperationException.class, emptyCardFace::getScoreRequirements
        );
        assertEquals("Empty cards do not have score requirements", exceptionThrown.getMessage());
    }


    @Test
    public void testGetPlacementRequirements() {
        EmptyCardFace emptyCardFace = new EmptyCardFace("imageURI");
        UnsupportedOperationException exceptionThrown = assertThrows(
                UnsupportedOperationException.class, emptyCardFace::getPlacementRequirements
        );
        assertEquals("Empty cards do not have placement requirements", exceptionThrown.getMessage());
    }


    @Test
    public void testGetXCoord() {
        EmptyCardFace emptyCardFace = new EmptyCardFace("imageURI");
        UnsupportedOperationException exceptionThrown = assertThrows(
                UnsupportedOperationException.class, emptyCardFace::getXCoord
        );
        assertEquals("Empty cards do not have coordinates", exceptionThrown.getMessage());
    }
    @Test
    public void testGetYCoord() {
        EmptyCardFace emptyCardFace = new EmptyCardFace("imageURI");
        UnsupportedOperationException exceptionThrown = assertThrows(
                UnsupportedOperationException.class, emptyCardFace::getYCoord
        );
        assertEquals("Empty cards do not have coordinates", exceptionThrown.getMessage());
    }
    @Test
    public void testSetPlacementTurn() {
        EmptyCardFace emptyCardFace = new EmptyCardFace("imageURI");
        UnsupportedOperationException exceptionThrown = assertThrows(
                UnsupportedOperationException.class, () -> emptyCardFace.setPlacementTurn(1)
        );
        assertEquals("Empty cards do not have placement turns", exceptionThrown.getMessage());
    }
    @Test
    public void testSetXCoord() {
        EmptyCardFace emptyCardFace = new EmptyCardFace("imageURI");
        UnsupportedOperationException exceptionThrown = assertThrows(
                UnsupportedOperationException.class, () -> emptyCardFace.setXCoord(1)
        );
        assertEquals("Empty cards do not have coordinates", exceptionThrown.getMessage());
    }
    @Test
    public void testSetYCoord() {
        EmptyCardFace emptyCardFace = new EmptyCardFace("imageURI");
        UnsupportedOperationException exceptionThrown = assertThrows(
                UnsupportedOperationException.class, () -> emptyCardFace.setYCoord(1)
        );
        assertEquals("Empty cards do not have coordinates", exceptionThrown.getMessage());
    }
    @Test
    public void testGetPlacementTurn() {
        EmptyCardFace emptyCardFace = new EmptyCardFace("imageURI");
        UnsupportedOperationException exceptionThrown = assertThrows(
                UnsupportedOperationException.class, emptyCardFace::getPlacementTurn
        );
        assertEquals("Empty cards do not have placement turns", exceptionThrown.getMessage());
    }
}
