package Server.Controller;

import Server.Card.*;
import Server.Connections.ConnectionHandler;
import Server.Enums.Color;
import Server.Enums.Face;
import Server.EventManager.EventManager;
import Server.Player.Player;
import Server.Player.PlayerInstance;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestController {
    @Test
    public void testAddPlayer() throws Exception {
        ConnectionHandler connectionHandler = new ConnectionHandler(0);
        Controller controller = new ControllerInstance(connectionHandler);
        Player player = new PlayerInstance("player1",null);
        controller.addPlayer(player);
        List<Player> playerList = controller.getPlayerList();
        assertEquals(1, playerList.size());
        assertEquals(player, playerList.get(0));
        Player player2 = new PlayerInstance("player2",null);
        Player player3 = new PlayerInstance("player3",null);
        Player player4 = new PlayerInstance("player4",null);
        Player player5 = new PlayerInstance("player5",null);
        controller.addPlayer(player2);
        controller.addPlayer(player3);
        controller.addPlayer(player4);
        controller.addPlayer(player5);
        assertEquals(4, playerList.size());
        assertFalse(playerList.contains(player5));
    }
    @Test
    public void testRemovePlayer() throws Exception {
        ConnectionHandler connectionHandler = new ConnectionHandler(0);
        Controller controller = new ControllerInstance(connectionHandler);
        Player player = new PlayerInstance("player1",null);
        controller.addPlayer(player);
        List<Player> playerList = controller.getPlayerList();
        assertEquals(1, playerList.size());
        controller.removePlayer(player);
        assertEquals(0, playerList.size());
    }
    @Test
    public void testColor() throws Exception {
        ConnectionHandler connectionHandler = new ConnectionHandler(0);
        Controller controller = new ControllerInstance(connectionHandler);
        Player player = new PlayerInstance("player1",new EventManager());
        controller.addPlayer(player);
        controller.setPlayerColor(Color.RED, player);
        Player player2 = new PlayerInstance("player2",new EventManager());
        controller.addPlayer(player2);
        controller.setPlayerColor(Color.RED, player2);
        assertEquals(Color.RED, player.getColor());
        assertNull(player2.getColor());
    }

    @Test
    public void testSetSecretAchievement() throws Exception {
        ConnectionHandler connectionHandler = new ConnectionHandler(0);
        Controller controller = new ControllerInstance(connectionHandler);
        Player player = new PlayerInstance("player1",new EventManager());
        controller.addPlayer(player);
        AchievementCard chosenCard = new AchievementCard(
                new AchievementFrontFace("image.jpg", null, 0),
                new EmptyCardFace("image.jpg")
        );
        controller.setSecretObjectiveCard(player, chosenCard);
        assertEquals(chosenCard, player.getSecretObjective());
    }
    @Test
    public void testInitializeManuscript() throws Exception
    {
        ConnectionHandler connectionHandler = new ConnectionHandler(0);
        Controller controller = new ControllerInstance(connectionHandler);
        Player player = new PlayerInstance("player1",new EventManager());
        controller.addPlayer(player);
        StartingFrontFace startingFrontFace = new StartingFrontFace("image.jpg", new HashMap<>(), new ArrayList<>());
        StartingCard startingCard = new StartingCard(
                startingFrontFace,
                new CornerCardFace("image.jpg", new HashMap<>())
        );
        controller.setStartingCard(player, startingCard, Face.FRONT);
        assertEquals(startingFrontFace,player.getManuscript().getCardByCoord(0,0));
    }
    @Test
    public void testGiveInitialHand() throws Exception
    {
        ConnectionHandler connectionHandler = new ConnectionHandler(0);
        Controller controller = new ControllerInstance(connectionHandler);
        Player player = new PlayerInstance("player1",new EventManager());
        controller.addPlayer(player);
        controller.giveInitialHand();
        assertEquals(3, player.getHand().size());
        assertThrows(ClassCastException.class, () -> {
            GoldCard card = (GoldCard) player.getHand().get(0);
        });
        assertThrows(ClassCastException.class, () -> {
            StartingCard card = (StartingCard) player.getHand().get(0);
        });
        assertThrows(ClassCastException.class, () -> {
            AchievementCard card = (AchievementCard) player.getHand().get(0);
        });
        assertThrows(ClassCastException.class, () -> {
            GoldCard card = (GoldCard) player.getHand().get(1);
        });
        assertThrows(ClassCastException.class, () -> {
            StartingCard card = (StartingCard) player.getHand().get(1);
        });
        assertThrows(ClassCastException.class, () -> {
            AchievementCard card = (AchievementCard) player.getHand().get(1);
        });
        assertThrows(ClassCastException.class, () -> {
            ResourceCard card = (ResourceCard) player.getHand().get(2);
        });
        assertThrows(ClassCastException.class, () -> {
            StartingCard card = (StartingCard) player.getHand().get(2);
        });
        assertThrows(ClassCastException.class, () -> {
            AchievementCard card = (AchievementCard) player.getHand().get(2);
        });
    }
    @Test
    public void testNextTurn() throws Exception
    {
        ConnectionHandler connectionHandler = new ConnectionHandler(0);
        Controller controller = new ControllerInstance(connectionHandler);
        controller.nextTurn();
        assertEquals(1, controller.getTurn());
    }
}
