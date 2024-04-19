package Server.Controller;

import Server.Card.*;
import Server.Connections.ConnectionHandler;
import Server.Deck.GoldDeck;
import Server.Deck.ResourceDeck;
import Server.Enums.*;
import Server.EventManager.EventManager;
import Server.Player.Player;
import Server.Player.PlayerInstance;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public void testNextTurn() throws Exception {
        ConnectionHandler connectionHandler = new ConnectionHandler(0);
        Controller controller = new ControllerInstance(connectionHandler);
        controller.nextTurn();
        assertEquals(1, controller.getTurn());
    }
    @Test
    public void testPlayCard() throws Exception {
        ConnectionHandler connectionHandler = new ConnectionHandler(0);
        Controller controller = new ControllerInstance(connectionHandler);
        Player player = new PlayerInstance("player1",new EventManager());
        StartingCard startingCard = new StartingCard(new StartingFrontFace("image.jpg", new HashMap<>(), new ArrayList<>()), new CornerCardFace("image.jpg", new HashMap<>()));
        controller.addPlayer(player);
        controller.setStartingCard(player,startingCard,Face.FRONT);
        controller.giveInitialHand();
        Map<CardCorners, Symbol> cornerSymbols = new HashMap<>(player.getHand().get(0).getCornerFace(Face.FRONT).getCornerSymbols());
        List<Symbol> centerSymbols = new ArrayList<>(player.getHand().get(0).getFace(Face.BACK).getCenterSymbols());
        ResourceCard resourceCard = new ResourceCard(
                new ResourceFrontFace("image1.jpg", cornerSymbols , 0, Symbol.FUNGUS),
                new RegularBackFace("image2.jpg", centerSymbols)
        );
        Map<CardCorners,Symbol> cornerSymbols2 = new HashMap<>(player.getHand().get(1).getCornerFace(Face.FRONT).getCornerSymbols());
        List<Symbol> centerSymbols2 = new ArrayList<>(player.getHand().get(1).getFace(Face.BACK).getCenterSymbols());
        ResourceCard resourceCard2 = new ResourceCard(
                new ResourceFrontFace("image3.jpg", cornerSymbols2 , 0, Symbol.FUNGUS),
                new RegularBackFace("image4.jpg", centerSymbols2)
        );
        controller.playCard(player, player.getHand().get(0),1,1,Face.BACK);
        assertEquals(resourceCard.getCornerFace(Face.BACK).getCornerSymbols(), player.getManuscript().getCardByCoord(1,1).getCornerSymbols());
        assertEquals(resourceCard.getFace(Face.BACK).getPlacementTurn(), player.getManuscript().getCardByCoord(1,1).getPlacementTurn());
        assertEquals(resourceCard.getFace(Face.BACK).getCenterSymbols(), player.getManuscript().getCardByCoord(1,1).getCenterSymbols());
        controller.playCard(player, player.getHand().get(0),-1,1,Face.FRONT);
        assertEquals(resourceCard2.getCornerFace(Face.FRONT).getCornerSymbols(), player.getManuscript().getCardByCoord(-1,1).getCornerSymbols());
        assertEquals(resourceCard2.getFace(Face.FRONT).getPlacementTurn(), player.getManuscript().getCardByCoord(-1,1).getPlacementTurn());
        assertEquals(resourceCard2.getCornerFace(Face.FRONT).getScore(), player.getManuscript().getCardByCoord(-1,1).getScore());

        //System.out.println(resourceCard.getCornerFace(Face.BACK)==player.getManuscript().getCardByCoord(1,1));
        //riga 169 da false

    }
    @Test
    public void testDrawCard() throws Exception {
        int sentinel = 0, i = 0;
        ConnectionHandler connectionHandler = new ConnectionHandler(0);
        Controller controller = new ControllerInstance(connectionHandler);
        Player player = new PlayerInstance("player1",new EventManager());
        controller.addPlayer(player);
        Map <CardCorners, Symbol> cornerSymbols = new HashMap<>();
        List <Symbol> centerSymbols = new ArrayList<>();
        centerSymbols.add(Symbol.NONE);
        Card card = new ResourceCard(
                new ResourceFrontFace("image.jpg", cornerSymbols, 0, Symbol.FUNGUS),
                new RegularBackFace("image.jpg", centerSymbols)
        );
        controller.drawCard(player, DeckPosition.DECK, Decks.RESOURCE);
        controller.drawCard(player,DeckPosition.FIRST_CARD, Decks.GOLD);
        controller.drawCard(player,DeckPosition.SECOND_CARD, Decks.GOLD);
        assertEquals(3, player.getHand().size());
        assertThrows(ClassCastException.class, () -> {
            GoldCard card1 = (GoldCard) player.getHand().get(0);
        });
        assertThrows(ClassCastException.class, () -> {
            StartingCard card1 = (StartingCard) player.getHand().get(0);
        });
        assertThrows(ClassCastException.class, () -> {
            AchievementCard card1 = (AchievementCard) player.getHand().get(0);
        });
        assertThrows(ClassCastException.class, () -> {
            ResourceCard card1 = (ResourceCard) player.getHand().get(1);
        });
        assertThrows(ClassCastException.class, () -> {
            StartingCard card1 = (StartingCard) player.getHand().get(1);
        });
        assertThrows(ClassCastException.class, () -> {
            AchievementCard card1 = (AchievementCard) player.getHand().get(1);
        });
        assertThrows(ClassCastException.class, () -> {
            ResourceCard card1 = (ResourceCard) player.getHand().get(2);
        });
        assertThrows(ClassCastException.class, () -> {
            StartingCard card1 = (StartingCard) player.getHand().get(2);
        });
        assertThrows(ClassCastException.class, () -> {
            AchievementCard card1 = (AchievementCard) player.getHand().get(2);
        });
        ResourceDeck resourceDeck2 = new ResourceDeck();
        GoldDeck goldDeck = new GoldDeck();
        GoldDeck goldDeck1 = new GoldDeck();
        List<ResourceCard> cards = new ArrayList<>();
        for (i = 0 ;  i < 38 ; i++){
            card = (ResourceCard) resourceDeck2.popCard(DeckPosition.DECK);
            if(card.getFace(Face.FRONT).getCornerSymbols().equals(player.getHand().get(0).getFace(Face.FRONT).getCornerSymbols()) &&
                    card.getFace(Face.BACK).getCenterSymbols().equals(player.getHand().get(0).getFace(Face.BACK).getCenterSymbols()) &&
                    card.getCornerFace(Face.FRONT).getScore() == player.getHand().get(0).getCornerFace(Face.FRONT).getScore() &&
                    card.getFace(Face.FRONT).getKingdom().equals(player.getHand().get(0).getFace(Face.BACK).getKingdom())){
                sentinel++;
            }
        }
        card = (ResourceCard) resourceDeck2.popCard(DeckPosition.FIRST_CARD);
        if(sentinel == 0 && card.getFace(Face.FRONT).getCornerSymbols().equals(player.getHand().get(0).getFace(Face.FRONT).getCornerSymbols())
        && card.getFace(Face.BACK).getCenterSymbols().equals(player.getHand().get(0).getFace(Face.BACK).getCenterSymbols()) &&
                card.getCornerFace(Face.FRONT).getScore() == player.getHand().get(0).getCornerFace(Face.FRONT).getScore() &&
                card.getFace(Face.FRONT).getKingdom().equals(player.getHand().get(0).getFace(Face.BACK).getKingdom())){
            sentinel++;
        }
        card = (ResourceCard) resourceDeck2.popCard(DeckPosition.SECOND_CARD);
        if(sentinel == 0 && card.getFace(Face.FRONT).getCornerSymbols().equals(player.getHand().get(0).getFace(Face.FRONT).getCornerSymbols()) &&
                card.getFace(Face.BACK).getCenterSymbols().equals(player.getHand().get(0).getFace(Face.BACK).getCenterSymbols()) &&
                card.getCornerFace(Face.FRONT).getScore() == player.getHand().get(0).getCornerFace(Face.FRONT).getScore() &&
                card.getFace(Face.FRONT).getKingdom().equals(player.getHand().get(0).getFace(Face.BACK).getKingdom()))
            {
            sentinel++;
            }
        assertEquals(1, sentinel);
        for (i = 0 ;  i < 38 ; i++){
            card = (GoldCard) goldDeck.popCard(DeckPosition.DECK);
            if(card.getFace(Face.FRONT).getCornerSymbols().equals(player.getHand().get(1).getFace(Face.FRONT).getCornerSymbols()) &&
                    card.getFace(Face.BACK).getCenterSymbols().equals(player.getHand().get(1).getFace(Face.BACK).getCenterSymbols()) &&
                    card.getCornerFace(Face.FRONT).getScore() == player.getHand().get(1).getCornerFace(Face.FRONT).getScore() &&
                    card.getFace(Face.FRONT).getKingdom().equals(player.getHand().get(1).getFace(Face.BACK).getKingdom())){
                sentinel++;
            }
        }
        card = (GoldCard) goldDeck.popCard(DeckPosition.FIRST_CARD);
        if(sentinel == 1 && card.getFace(Face.FRONT).getCornerSymbols().equals(player.getHand().get(1).getFace(Face.FRONT).getCornerSymbols()) &&
                card.getFace(Face.BACK).getCenterSymbols().equals(player.getHand().get(1).getFace(Face.BACK).getCenterSymbols()) &&
                card.getCornerFace(Face.FRONT).getScore() == player.getHand().get(1).getCornerFace(Face.FRONT).getScore() &&
                card.getFace(Face.FRONT).getKingdom().equals(player.getHand().get(1).getFace(Face.BACK).getKingdom())){
            sentinel++;
        }
        card = (GoldCard) goldDeck.popCard(DeckPosition.SECOND_CARD);
        if(sentinel == 1 && card.getFace(Face.FRONT).getCornerSymbols().equals(player.getHand().get(1).getFace(Face.FRONT).getCornerSymbols()) &&
                card.getFace(Face.BACK).getCenterSymbols().equals(player.getHand().get(1).getFace(Face.BACK).getCenterSymbols()) &&
                card.getCornerFace(Face.FRONT).getScore() == player.getHand().get(1).getCornerFace(Face.FRONT).getScore() &&
                card.getFace(Face.FRONT).getKingdom().equals(player.getHand().get(1).getFace(Face.BACK).getKingdom()))
        {
            sentinel++;
        }
        assertEquals(2, sentinel);
        for (i = 0 ;  i < 38 ; i++){
            card = (GoldCard) goldDeck1.popCard(DeckPosition.DECK);
            if(card.getFace(Face.FRONT).getCornerSymbols().equals(player.getHand().get(2).getFace(Face.FRONT).getCornerSymbols()) &&
                    card.getFace(Face.BACK).getCenterSymbols().equals(player.getHand().get(2).getFace(Face.BACK).getCenterSymbols()) &&
                    card.getCornerFace(Face.FRONT).getScore() == player.getHand().get(2).getCornerFace(Face.FRONT).getScore() &&
                    card.getFace(Face.FRONT).getKingdom().equals(player.getHand().get(2).getFace(Face.BACK).getKingdom())){
                sentinel++;
            }
        }
        card = (GoldCard) goldDeck1.popCard(DeckPosition.FIRST_CARD);
        if(sentinel == 2 && card.getFace(Face.FRONT).getCornerSymbols().equals(player.getHand().get(2).getFace(Face.FRONT).getCornerSymbols()) &&
                card.getFace(Face.BACK).getCenterSymbols().equals(player.getHand().get(2).getFace(Face.BACK).getCenterSymbols()) &&
                card.getCornerFace(Face.FRONT).getScore() == player.getHand().get(2).getCornerFace(Face.FRONT).getScore() &&
                card.getFace(Face.FRONT).getKingdom().equals(player.getHand().get(2).getFace(Face.BACK).getKingdom())) {
            sentinel++;
        }
        card = (GoldCard) goldDeck1.popCard(DeckPosition.SECOND_CARD);
        if(sentinel == 2 && card.getFace(Face.FRONT).getCornerSymbols().equals(player.getHand().get(2).getFace(Face.FRONT).getCornerSymbols()) &&
                card.getFace(Face.BACK).getCenterSymbols().equals(player.getHand().get(2).getFace(Face.BACK).getCenterSymbols()) &&
                card.getCornerFace(Face.FRONT).getScore() == player.getHand().get(2).getCornerFace(Face.FRONT).getScore() &&
                card.getFace(Face.FRONT).getKingdom().equals(player.getHand().get(2).getFace(Face.BACK).getKingdom()))
        {
            sentinel++;
        }
        assertEquals(3 , sentinel );


    }
}
