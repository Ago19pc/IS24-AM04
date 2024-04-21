package Server.Controller;

import Server.Card.*;
import Server.Connections.ConnectionHandler;
import Server.Deck.GoldDeck;
import Server.Deck.ResourceDeck;
import Server.Enums.*;
import Server.EventManager.EventManager;
import Server.Manuscript.Manuscript;
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

        Map<CardCorners, Symbol> cornerSymbols = new HashMap<>();
        cornerSymbols.put(CardCorners.TOP_LEFT, Symbol.FUNGUS);
        cornerSymbols.put(CardCorners.TOP_RIGHT, Symbol.FUNGUS);
        cornerSymbols.put(CardCorners.BOTTOM_LEFT, Symbol.FUNGUS);
        cornerSymbols.put(CardCorners.BOTTOM_RIGHT, Symbol.FUNGUS);
        List<Symbol> centerSymbols = new ArrayList<>();
        centerSymbols.add(Symbol.FUNGUS);
        ResourceCard resourceCard = new ResourceCard(
                new ResourceFrontFace("image1.jpg", cornerSymbols , 0, Symbol.FUNGUS),
                new RegularBackFace("image2.jpg", centerSymbols)
        );
        Map<CardCorners,Symbol> cornerSymbols2 = new HashMap<>();
        cornerSymbols2.put(CardCorners.TOP_LEFT, Symbol.BUG);
        cornerSymbols2.put(CardCorners.TOP_RIGHT, Symbol.BUG);
        cornerSymbols2.put(CardCorners.BOTTOM_LEFT, Symbol.BUG);
        cornerSymbols2.put(CardCorners.BOTTOM_RIGHT, Symbol.BUG);
        List<Symbol> centerSymbols2 = new ArrayList<>();
        centerSymbols2.add(Symbol.BUG);
        ResourceCard resourceCard2 = new ResourceCard(
                new ResourceFrontFace("image3.jpg", cornerSymbols2 , 1, Symbol.BUG),
                new RegularBackFace("image4.jpg", centerSymbols2)
        );
        controller.playCard(player, resourceCard,1,1,Face.BACK);
        assertEquals(resourceCard.getCornerFace(Face.BACK).getCornerSymbols(), player.getManuscript().getCardByCoord(1,1).getCornerSymbols());
        assertEquals(resourceCard.getFace(Face.BACK).getPlacementTurn(), player.getManuscript().getCardByCoord(1,1).getPlacementTurn());
        assertEquals(resourceCard.getFace(Face.BACK).getCenterSymbols(), player.getManuscript().getCardByCoord(1,1).getCenterSymbols());
        controller.playCard(player, resourceCard2,-1,1,Face.FRONT);
        assertEquals(resourceCard2.getCornerFace(Face.FRONT).getCornerSymbols(), player.getManuscript().getCardByCoord(-1,1).getCornerSymbols());
        assertEquals(resourceCard2.getFace(Face.FRONT).getPlacementTurn(), player.getManuscript().getCardByCoord(-1,1).getPlacementTurn());
        assertEquals(resourceCard2.getCornerFace(Face.FRONT).getScore(), player.getManuscript().getCardByCoord(-1,1).getScore());
        assertEquals(1, player.getPoints());
        Map<CardCorners,Symbol> cornerSymbols3 = new HashMap<>();
        cornerSymbols3.put(CardCorners.TOP_LEFT, Symbol.BUG);
        cornerSymbols3.put(CardCorners.TOP_RIGHT, Symbol.QUILL);
        cornerSymbols3.put(CardCorners.BOTTOM_LEFT, Symbol.QUILL);
        cornerSymbols3.put(CardCorners.BOTTOM_RIGHT, Symbol.QUILL);
        Map<Symbol,Integer> scoreRequirements = new HashMap<>();
        scoreRequirements.put(Symbol.BUG, 2);
        GoldCard goldCard = new GoldCard(
                new GoldFrontFace("image5.jpg", cornerSymbols3, 2,new HashMap<>(), scoreRequirements, Symbol.BUG),
                new RegularBackFace("image6.jpg", centerSymbols2)
        );
        controller.playCard(player, goldCard,-1,-1,Face.FRONT);
        assertEquals(5, player.getPoints());
        //System.out.println(resourceCard.getCornerFace(Face.BACK)==player.getManuscript().getCardByCoord(1,1));
        //riga 169 da false perché non è stato fatto l'equals delle cardFace

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
        controller.drawCard(player,DeckPosition.FIRST_CARD, Decks.GOLD);
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
        for (i = 0 ;  i < 36 ; i++){
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
        for (i = 0 ;  i < 36 ; i++){
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
        for (i = 0 ;  i < 36 ; i++){
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
    @Test
    public void testComputeLeaderboard() throws IOException {
        ConnectionHandler connectionHandler = new ConnectionHandler(0);
        Controller controller = new ControllerInstance(connectionHandler);
        Player player = new PlayerInstance("player1",new EventManager());
        controller.addPlayer(player);
        Player player2 = new PlayerInstance("player2",new EventManager());
        controller.addPlayer(player2);
        Player player3 = new PlayerInstance("player3",new EventManager());
        controller.addPlayer(player3);
        Player player4 = new PlayerInstance("player4",new EventManager());
        controller.addPlayer(player4);
        Map<CardCorners, Symbol> cornerSymbols = new HashMap<>();
        List<Symbol> symbols = new ArrayList<>();
        symbols.add(Symbol.ANIMAL);
        symbols.add(Symbol.BUG);
        StartingFrontFace startingFrontFace = new StartingFrontFace("startingfront.jpeg", cornerSymbols, symbols);
        CornerCardFace startingBackFace = new CornerCardFace("startingback.jpeg", new HashMap<>());
        StartingCard startingCard = new StartingCard(startingFrontFace, startingBackFace);
        controller.setStartingCard(player, startingCard, Face.FRONT);
        controller.setStartingCard(player2, startingCard, Face.FRONT);
        controller.setStartingCard(player3, startingCard, Face.FRONT);
        controller.setStartingCard(player4, startingCard, Face.FRONT);
        Map<CardCorners, Symbol> cornerSymbols2 = new HashMap<>();
        cornerSymbols2.put(CardCorners.TOP_LEFT, Symbol.FUNGUS);
        cornerSymbols2.put(CardCorners.TOP_RIGHT, Symbol.NONE);
        cornerSymbols2.put(CardCorners.BOTTOM_LEFT, Symbol.FUNGUS);
        cornerSymbols2.put(CardCorners.BOTTOM_RIGHT, Symbol.EMPTY);
        ResourceFrontFace frontface2 = new ResourceFrontFace("frontface2.jpeg", cornerSymbols2, 0, Symbol.BUG);
        controller.nextTurn();
        controller.playCard(player,
                new ResourceCard(frontface2, new RegularBackFace("backface2.jpeg", new ArrayList<>(List.of(Symbol.BUG)))),
                1, 1, Face.FRONT);
        controller.playCard(player2,
                new ResourceCard(frontface2, new RegularBackFace("backface2.jpeg", new ArrayList<>(List.of(Symbol.BUG)))),
                1, 1, Face.FRONT);
        controller.playCard(player3,
                new ResourceCard(frontface2, new RegularBackFace("backface2.jpeg", new ArrayList<>(List.of(Symbol.BUG)))),
                1, 1, Face.FRONT);
        controller.playCard(player4,
                new ResourceCard(frontface2, new RegularBackFace("backface2.jpeg", new ArrayList<>(List.of(Symbol.BUG)))),
                1, 1, Face.FRONT);
        Map<CardCorners, Symbol> cornerSymbols3 = new HashMap<>();
        cornerSymbols3.put(CardCorners.TOP_LEFT, Symbol.EMPTY);
        cornerSymbols3.put(CardCorners.TOP_RIGHT, Symbol.NONE);
        cornerSymbols3.put(CardCorners.BOTTOM_LEFT, Symbol.PLANT);
        cornerSymbols3.put(CardCorners.BOTTOM_RIGHT, Symbol.EMPTY);
        ResourceFrontFace frontface3 = new ResourceFrontFace("frontface3.jpeg", cornerSymbols3, 0, Symbol.FUNGUS);
        controller.nextTurn();
        controller.playCard(player,
                new ResourceCard(frontface3, new RegularBackFace("backface3.jpeg", new ArrayList<>(List.of(Symbol.BUG)))),
                2, 0, Face.FRONT);
        controller.playCard(player2,
                new ResourceCard(frontface3, new RegularBackFace("backface3.jpeg", new ArrayList<>(List.of(Symbol.BUG)))),
                2, 0, Face.FRONT);
        controller.playCard(player3,
                new ResourceCard(frontface3, new RegularBackFace("backface3.jpeg", new ArrayList<>(List.of(Symbol.BUG)))),
                2, 0, Face.FRONT);
        controller.playCard(player4,
                new ResourceCard(frontface3, new RegularBackFace("backface3.jpeg", new ArrayList<>(List.of(Symbol.BUG)))),
                2, 0, Face.FRONT);
        Map<CardCorners, Symbol> cornerSymbols4 = new HashMap<>();
        cornerSymbols4.put(CardCorners.TOP_LEFT, Symbol.PLANT);
        cornerSymbols4.put(CardCorners.TOP_RIGHT, Symbol.EMPTY);
        cornerSymbols4.put(CardCorners.BOTTOM_LEFT, Symbol.EMPTY);
        cornerSymbols4.put(CardCorners.BOTTOM_RIGHT, Symbol.EMPTY);
        ResourceFrontFace frontface4 = new ResourceFrontFace("frontface4.jpeg", cornerSymbols4, 0, Symbol.FUNGUS);
        controller.nextTurn();
        controller.playCard(player,
                new ResourceCard(frontface4, new RegularBackFace("backface4.jpeg", new ArrayList<>(List.of(Symbol.BUG)))),
                1, -1, Face.FRONT);
        controller.playCard(player2,
                new ResourceCard(frontface4, new RegularBackFace("backface4.jpeg", new ArrayList<>(List.of(Symbol.BUG)))),
                1, -1, Face.FRONT);
        controller.playCard(player3,
                new ResourceCard(frontface4, new RegularBackFace("backface4.jpeg", new ArrayList<>(List.of(Symbol.BUG)))),
                1, -1, Face.FRONT);
        controller.playCard(player4,
                new ResourceCard(frontface4, new RegularBackFace("backface4.jpeg", new ArrayList<>(List.of(Symbol.BUG)))),
                1, -1, Face.FRONT);
        Map<CardCorners, Symbol> cornerSymbols5 = new HashMap<>();
        cornerSymbols5.put(CardCorners.TOP_LEFT, Symbol.FUNGUS);
        cornerSymbols5.put(CardCorners.TOP_RIGHT, Symbol.NONE);
        cornerSymbols5.put(CardCorners.BOTTOM_LEFT, Symbol.EMPTY);
        cornerSymbols5.put(CardCorners.BOTTOM_RIGHT, Symbol.EMPTY);
        ResourceFrontFace frontface5 = new ResourceFrontFace("frontface5.jpeg", cornerSymbols5, 0, Symbol.PLANT);
        controller.nextTurn();
        controller.playCard(player,
                new ResourceCard(frontface5, new RegularBackFace("backface5.jpeg", new ArrayList<>(List.of(Symbol.BUG)))),
                3, -1, Face.FRONT);
        controller.playCard(player2,
                new ResourceCard(frontface5, new RegularBackFace("backface5.jpeg", new ArrayList<>(List.of(Symbol.BUG)))),
                3, -1, Face.FRONT);
        controller.playCard(player3,
                new ResourceCard(frontface5, new RegularBackFace("backface5.jpeg", new ArrayList<>(List.of(Symbol.BUG)))),
                3, -1, Face.FRONT);
        controller.playCard(player4,
                new ResourceCard(frontface5, new RegularBackFace("backface5.jpeg", new ArrayList<>(List.of(Symbol.BUG)))),
                3, -1, Face.FRONT);
        Map<CardCorners, Symbol> cornerSymbols6 = new HashMap<>();
        cornerSymbols6.put(CardCorners.TOP_LEFT, Symbol.PARCHMENT);
        cornerSymbols6.put(CardCorners.TOP_RIGHT, Symbol.BUG);
        cornerSymbols6.put(CardCorners.BOTTOM_LEFT, Symbol.EMPTY);
        cornerSymbols6.put(CardCorners.BOTTOM_RIGHT, Symbol.FUNGUS);
        GoldFrontFace frontface6 = new GoldFrontFace("frontface6.jpeg", cornerSymbols6, 0, new HashMap<>(), new HashMap<>(Map.of(Symbol.BUG, 100)), Symbol.PLANT);
        controller.nextTurn();
        controller.playCard(player,
                new GoldCard(frontface6, new RegularBackFace("backface6.jpeg", new ArrayList<>(List.of(Symbol.BUG)))),
                4, -2, Face.FRONT);
        controller.playCard(player2,
                new GoldCard(frontface6, new RegularBackFace("backface6.jpeg", new ArrayList<>(List.of(Symbol.BUG)))),
                4, -2, Face.FRONT);
        controller.playCard(player3,
                new GoldCard(frontface6, new RegularBackFace("backface6.jpeg", new ArrayList<>(List.of(Symbol.BUG)))),
                4, -2, Face.FRONT);
        controller.playCard(player4,
                new GoldCard(frontface6, new RegularBackFace("backface6.jpeg", new ArrayList<>(List.of(Symbol.BUG)))),
                4, -2, Face.FRONT);
        Map<CardCorners, Symbol> cornerSymbols7 = new HashMap<>();
        cornerSymbols7.put(CardCorners.TOP_LEFT, Symbol.EMPTY);
        cornerSymbols7.put(CardCorners.TOP_RIGHT, Symbol.BUG);
        cornerSymbols7.put(CardCorners.BOTTOM_LEFT, Symbol.NONE);
        cornerSymbols7.put(CardCorners.BOTTOM_RIGHT, Symbol.EMPTY);
        GoldFrontFace frontface7 = new GoldFrontFace("frontface7.jpeg", cornerSymbols7, 0, new HashMap<>(), new HashMap<>(Map.of(Symbol.BUG, 100)), Symbol.PLANT);
        controller.nextTurn();
        controller.playCard(player,
                new GoldCard(frontface7, new RegularBackFace("backface7.jpeg", new ArrayList<>(List.of(Symbol.BUG)))),
                -1, -1, Face.FRONT);
        controller.playCard(player2,
                new GoldCard(frontface7, new RegularBackFace("backface7.jpeg", new ArrayList<>(List.of(Symbol.BUG)))),
                -1, -1, Face.FRONT);
        controller.playCard(player3,
                new GoldCard(frontface7, new RegularBackFace("backface7.jpeg", new ArrayList<>(List.of(Symbol.BUG)))),
                -1, -1, Face.FRONT);
        controller.playCard(player4,
                new GoldCard(frontface7, new RegularBackFace("backface7.jpeg", new ArrayList<>(List.of(Symbol.BUG)))),
                -1, -1, Face.FRONT);
        Map<CardCorners, Symbol> cornerSymbols8 = new HashMap<>();
        cornerSymbols8.put(CardCorners.TOP_LEFT, Symbol.FUNGUS);
        cornerSymbols8.put(CardCorners.TOP_RIGHT, Symbol.NONE);
        cornerSymbols8.put(CardCorners.BOTTOM_LEFT, Symbol.FUNGUS);
        cornerSymbols8.put(CardCorners.BOTTOM_RIGHT, Symbol.EMPTY);
        ResourceFrontFace frontface8 = new ResourceFrontFace("frontface8.jpeg", cornerSymbols8, 0, Symbol.FUNGUS);
        controller.nextTurn();
        controller.playCard(player,
                new ResourceCard(frontface8, new RegularBackFace("backface8.jpeg", new ArrayList<>(List.of(Symbol.BUG)))),
                0, -2, Face.FRONT);
        controller.playCard(player2,
                new ResourceCard(frontface8, new RegularBackFace("backface8.jpeg", new ArrayList<>(List.of(Symbol.BUG)))),
                0, -2, Face.FRONT);
        controller.playCard(player3,
                new ResourceCard(frontface8, new RegularBackFace("backface8.jpeg", new ArrayList<>(List.of(Symbol.BUG)))),
                0, -2, Face.FRONT);
        controller.playCard(player4,
                new ResourceCard(frontface8, new RegularBackFace("backface8.jpeg", new ArrayList<>(List.of(Symbol.BUG)))),
                0, -2, Face.FRONT);
        Map<CardCorners, Symbol> cornerSymbols9 = new HashMap<>();
        cornerSymbols9.put(CardCorners.TOP_LEFT, Symbol.EMPTY);
        cornerSymbols9.put(CardCorners.TOP_RIGHT, Symbol.ANIMAL);
        cornerSymbols9.put(CardCorners.BOTTOM_LEFT, Symbol.EMPTY);
        cornerSymbols9.put(CardCorners.BOTTOM_RIGHT, Symbol.NONE);
        ResourceFrontFace frontface9 = new ResourceFrontFace("frontface9.jpeg", cornerSymbols9, 0, Symbol.FUNGUS);
        controller.nextTurn();
        controller.playCard(player,
                new ResourceCard(frontface9, new RegularBackFace("backface9.jpeg", new ArrayList<>(List.of(Symbol.BUG)))),
                -2, 0, Face.FRONT);
        controller.playCard(player2,
                new ResourceCard(frontface9, new RegularBackFace("backface9.jpeg", new ArrayList<>(List.of(Symbol.BUG)))),
                -2, 0, Face.FRONT);
        controller.playCard(player3,
                new ResourceCard(frontface9, new RegularBackFace("backface9.jpeg", new ArrayList<>(List.of(Symbol.BUG)))),
                -2, 0, Face.FRONT);
        controller.playCard(player4,
                new ResourceCard(frontface9, new RegularBackFace("backface9.jpeg", new ArrayList<>(List.of(Symbol.BUG)))),
                -2, 0, Face.FRONT);
        Map<CardCorners, Symbol> cornerSymbols10 = new HashMap<>();
        cornerSymbols10.put(CardCorners.TOP_LEFT, Symbol.EMPTY);
        cornerSymbols10.put(CardCorners.TOP_RIGHT, Symbol.NONE);
        cornerSymbols10.put(CardCorners.BOTTOM_LEFT, Symbol.EMPTY);
        cornerSymbols10.put(CardCorners.BOTTOM_RIGHT, Symbol.ANIMAL);
        ResourceFrontFace frontface10 = new ResourceFrontFace("frontface10.jpeg", cornerSymbols10, 0, Symbol.BUG);
        controller.nextTurn();
        controller.playCard(player,
                new ResourceCard(frontface10, new RegularBackFace("backface10.jpeg", new ArrayList<>(List.of(Symbol.BUG)))),
                -1, 1, Face.FRONT);
        controller.playCard(player2,
                new ResourceCard(frontface10, new RegularBackFace("backface10.jpeg", new ArrayList<>(List.of(Symbol.BUG)))),
                -1, 1, Face.FRONT);
        controller.playCard(player3,
                new ResourceCard(frontface10, new RegularBackFace("backface10.jpeg", new ArrayList<>(List.of(Symbol.BUG)))),
                -1, 1, Face.FRONT);
        controller.playCard(player4,
                new ResourceCard(frontface10, new RegularBackFace("backface10.jpeg", new ArrayList<>(List.of(Symbol.BUG)))),
                -1, 1, Face.FRONT);
        Map<CardCorners, Symbol> cornerSymbols11 = new HashMap<>();
        cornerSymbols11.put(CardCorners.TOP_LEFT, Symbol.NONE);
        cornerSymbols11.put(CardCorners.TOP_RIGHT, Symbol.EMPTY);
        cornerSymbols11.put(CardCorners.BOTTOM_LEFT, Symbol.EMPTY);
        cornerSymbols11.put(CardCorners.BOTTOM_RIGHT, Symbol.FUNGUS);
        ResourceFrontFace frontface11 = new ResourceFrontFace("frontface11.jpeg", cornerSymbols11, 0, Symbol.FUNGUS);
        controller.nextTurn();
        controller.playCard(player,
                new ResourceCard(frontface11, new RegularBackFace("backface11.jpeg", new ArrayList<>(List.of(Symbol.BUG)))),
                -2, 2, Face.FRONT);
        controller.playCard(player2,
                new ResourceCard(frontface11, new RegularBackFace("backface11.jpeg", new ArrayList<>(List.of(Symbol.BUG)))),
                -2, 2, Face.FRONT);
        controller.playCard(player3,
                new ResourceCard(frontface11, new RegularBackFace("backface11.jpeg", new ArrayList<>(List.of(Symbol.BUG)))),
                -2, 2, Face.FRONT);
        controller.playCard(player4,
                new ResourceCard(frontface11, new RegularBackFace("backface11.jpeg", new ArrayList<>(List.of(Symbol.BUG)))),
                -2, 2, Face.FRONT);
        Map<CardCorners, Symbol> cornerSymbols12 = new HashMap<>();
        cornerSymbols12.put(CardCorners.TOP_LEFT, Symbol.EMPTY);
        cornerSymbols12.put(CardCorners.TOP_RIGHT, Symbol.NONE);
        cornerSymbols12.put(CardCorners.BOTTOM_LEFT, Symbol.ANIMAL);
        cornerSymbols12.put(CardCorners.BOTTOM_RIGHT, Symbol.EMPTY);
        ResourceFrontFace frontface12 = new ResourceFrontFace("frontface12.jpeg", cornerSymbols12, 0, Symbol.ANIMAL);
        controller.nextTurn();
        controller.playCard(player,
                new ResourceCard(frontface12, new RegularBackFace("backface12.jpeg", new ArrayList<>(List.of(Symbol.BUG)))),
                -1, 3, Face.FRONT);
        controller.playCard(player2,
                new ResourceCard(frontface12, new RegularBackFace("backface12.jpeg", new ArrayList<>(List.of(Symbol.BUG)))),
                -1, 3, Face.FRONT);
        controller.playCard(player3,
                new ResourceCard(frontface12, new RegularBackFace("backface12.jpeg", new ArrayList<>(List.of(Symbol.BUG)))),
                -1, 3, Face.FRONT);
        controller.playCard(player4,
                new ResourceCard(frontface12, new RegularBackFace("backface12.jpeg", new ArrayList<>(List.of(Symbol.BUG)))),
                -1, 3, Face.FRONT);
        Map<Symbol, Integer> scoreRequirements = new HashMap<>();
        scoreRequirements.put(Symbol.PATTERN1F, 2);
        AchievementFrontFace achievementFrontFace1 = new AchievementFrontFace("achievementfront1.jpeg", scoreRequirements, 0);
        EmptyCardFace achievementBackFace1 = new EmptyCardFace("achievementback1.jpeg");
        AchievementCard achievementCard1 = new AchievementCard(achievementFrontFace1, achievementBackFace1);
        player.setSecretObjective(achievementCard1);
        Map<Symbol, Integer> scoreRequirements5 = new HashMap<>();
        scoreRequirements5.put(Symbol.PATTERN3, 3);
        AchievementFrontFace achievementFrontFace5 = new AchievementFrontFace("achievementfront5.jpeg", scoreRequirements5, 0);
        AchievementCard achievementCard5 = new AchievementCard(achievementFrontFace5, achievementBackFace1);
        player2.setSecretObjective(achievementCard5);
        Map<Symbol, Integer> scoreRequirements9 = new HashMap<>();
        scoreRequirements9.put(Symbol.FUNGUS, 3);
        AchievementFrontFace achievementFrontFace9 = new AchievementFrontFace("achievementfront9.jpeg", scoreRequirements9, 0);
        AchievementCard achievementCard9 = new AchievementCard(achievementFrontFace9, achievementBackFace1);
        player3.setSecretObjective(achievementCard9);
        Map<Symbol, Integer> scoreRequirements16 = new HashMap<>();
        scoreRequirements16.put(Symbol.QUILL, 2);
        AchievementFrontFace achievementFrontFace16 = new AchievementFrontFace("achievementfront16.jpeg", scoreRequirements16, 0);
        AchievementCard achievementCard16 = new AchievementCard(achievementFrontFace16, achievementBackFace1);
        player4.setSecretObjective(achievementCard16);
        List<Player> leaderboard = controller.computeLeaderboard();
        assertEquals(player, leaderboard.get(2));
        assertEquals(player2, leaderboard.get(1));
        assertEquals(player3, leaderboard.get(0));
        assertEquals(player4, leaderboard.get(3));
        int commonPoints = player4.getPoints();
        assertEquals(commonPoints, player.getPoints() - 2);
        assertEquals(commonPoints, player2.getPoints() - 3);
        assertEquals(commonPoints, player3.getPoints() - 4);
    }
}
