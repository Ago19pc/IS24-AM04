package Server.Manuscript;

import Server.Card.CornerCardFace;
import Server.Card.StartingFrontFace;
import Server.Enums.CardCorners;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

public class TestGraph {
    @Test
    public void testNewGraph() {
        CornerCardFace startingCard = new StartingFrontFace("startingFront1.jpeg", new HashMap<>(), new LinkedList<>());
        Graph graph = new Graph(startingCard);
        assertEquals(startingCard, graph.getRoot());
        assertTrue(graph.getAllCards().contains(startingCard));
        assertEquals(1, graph.getAllCards().size());
        assertNull(graph.getNeighbors(startingCard).get(CardCorners.TOP_LEFT));
        assertNull(graph.getNeighbors(startingCard).get(CardCorners.TOP_RIGHT));
        assertNull(graph.getNeighbors(startingCard).get(CardCorners.BOTTOM_LEFT));
        assertNull(graph.getNeighbors(startingCard).get(CardCorners.BOTTOM_RIGHT));
    }
    @Test
    public void testAddCard(){
        CornerCardFace startingCard = new StartingFrontFace("startingFront1.jpeg", new HashMap<>(), new LinkedList<>());
        Graph graph = new Graph(startingCard);
        CornerCardFace newCard = new CornerCardFace("newCard1.jpeg", new HashMap<>());
        HashMap<CardCorners, CornerCardFace> placementPosition = new HashMap<>();
        placementPosition.put(CardCorners.BOTTOM_LEFT, startingCard);
        graph.addCard(newCard, placementPosition, 1);
        assertEquals(newCard, graph.getNeighbors(startingCard).get(CardCorners.TOP_RIGHT));
        assertEquals(startingCard, graph.getNeighbors(newCard).get(CardCorners.BOTTOM_LEFT));
        assertTrue(graph.getAllCards().contains(newCard));
        assertEquals(2, graph.getAllCards().size());
    }
    @Test
    public void testCardsUnderAndOver(){
        CornerCardFace startingCard = new StartingFrontFace("startingFront1.jpeg", new HashMap<>(), new LinkedList<>());
        Graph graph = new Graph(startingCard);
        CornerCardFace newCard1 = new CornerCardFace("newCard1.jpeg", new HashMap<>());
        CornerCardFace newCard2 = new CornerCardFace("newCard2.jpeg", new HashMap<>());
        CornerCardFace newCard3 = new CornerCardFace("newCard3.jpeg", new HashMap<>());
        HashMap<CardCorners, CornerCardFace> placementPosition1 = new HashMap<>();
        placementPosition1.put(CardCorners.BOTTOM_LEFT, startingCard);
        HashMap<CardCorners, CornerCardFace> placementPosition2 = new HashMap<>();
        placementPosition2.put(CardCorners.TOP_LEFT, newCard1);
        HashMap<CardCorners, CornerCardFace> placementPosition3 = new HashMap<>();
        placementPosition3.put(CardCorners.TOP_LEFT, startingCard);
        placementPosition3.put(CardCorners.TOP_RIGHT, newCard2);
        graph.addCard(newCard1, placementPosition1, 1);
        graph.addCard(newCard2, placementPosition2, 2);
        graph.addCard(newCard3, placementPosition3, 3);
        assertEquals(0, graph.getCardsUnder(startingCard).size());
        assertEquals(1, graph.getCardsUnder(newCard1).size());
        assertTrue(graph.getCardsUnder(newCard1).containsValue(startingCard));
        assertEquals(1, graph.getCardsUnder(newCard2).size());
        assertTrue(graph.getCardsUnder(newCard2).containsValue(newCard1));
        assertEquals(2, graph.getCardsUnder(newCard3).size());
        assertTrue(graph.getCardsUnder(newCard3).containsValue(startingCard));
        assertTrue(graph.getCardsUnder(newCard3).containsValue(newCard2));
        assertEquals(2, graph.getCardsOver(startingCard).size());
        assertTrue(graph.getCardsOver(startingCard).containsValue(newCard1));
        assertTrue(graph.getCardsOver(startingCard).containsValue(newCard3));
        assertEquals(1, graph.getCardsOver(newCard1).size());
        assertTrue(graph.getCardsOver(newCard1).containsValue(newCard2));
        assertEquals(1, graph.getCardsOver(newCard2).size());
        assertTrue(graph.getCardsOver(newCard2).containsValue(newCard3));
        assertEquals(0, graph.getCardsOver(newCard3).size());
    }
}
