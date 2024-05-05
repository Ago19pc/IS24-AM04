package Server.Controller;

import Server.Card.*;
import Server.Connections.ServerConnectionHandler;
import Server.Deck.GoldDeck;
import Server.Deck.ResourceDeck;
import Server.Enums.*;
import Server.Exception.*;
import Server.Player.Player;
import Server.Player.PlayerInstance;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TestController {/*
    @Test
    public void testAddPlayer() throws Exception {
        ServerConnectionHandler connectionHandler = new ServerConnectionHandler(true);
        Controller controller = new ControllerInstance(connectionHandler);
        Player player = new PlayerInstance("player1");
        controller.addPlayer(player);
        List<Player> playerList = controller.getPlayerList();
        assertEquals(1, playerList.size());
        assertEquals(player, playerList.get(0));
        Player player2 = new PlayerInstance("player2");
        Player player3 = new PlayerInstance("player3");
        Player player4 = new PlayerInstance("player4");
        Player player5 = new PlayerInstance("player5");
        controller.addPlayer(player2);
        controller.addPlayer(player3);
        controller.addPlayer(player4);
        TooManyPlayersException exception = assertThrows(TooManyPlayersException.class, () -> {
            controller.addPlayer(player5);
        });
        playerList = controller.getPlayerList();
        assertEquals(4, playerList.size());
        assertFalse(playerList.contains(player5));
        assertEquals("Too many players", exception.getMessage());
    }
    @Test
    public void testRemovePlayer() throws Exception {
        ServerConnectionHandler connectionHandler = new ServerConnectionHandler(true);
        Controller controller = new ControllerInstance(connectionHandler);
        Player player = new PlayerInstance("player1");
        controller.addPlayer(player);
        List<Player> playerList = controller.getPlayerList();
        assertEquals(1, playerList.size());
        controller.removePlayer(player);
        playerList = controller.getPlayerList();
        assertEquals(0, playerList.size());
    }
    @Test
    public void testColor() throws Exception {
        ServerConnectionHandler connectionHandler = new ServerConnectionHandler(true);
        Controller controller = new ControllerInstance(connectionHandler);
        Player player = new PlayerInstance("player1");
        controller.addPlayer(player);
        controller.setPlayerColor(Color.RED, player);
        Player player2 = new PlayerInstance("player2");
        controller.addPlayer(player2);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            controller.setPlayerColor(Color.RED, player2);
        });
        assertEquals("Color already taken", exception.getMessage());
        assertEquals(Color.RED, player.getColor());
        assertNull(player2.getColor());
    }

    @Test
    public void testSetSecretAchievement() throws Exception {
        ServerConnectionHandler connectionHandler = new ServerConnectionHandler(true);
        Controller controller = new ControllerInstance(connectionHandler);
        Player player = new PlayerInstance("player1");
        controller.addPlayer(player);
        chosenCard
        controller.setSecretObjectiveCard(player, chosenCard);
        assertEquals(chosenCard, player.getSecretObjective());
    }
    @Test
    public void testInitializeManuscript() throws Exception
    {
        ServerConnectionHandler connectionHandler = new ServerConnectionHandler(true);
        Controller controller = new ControllerInstance(connectionHandler);
        Player player = new PlayerInstance("player1");
        Player player2 = new PlayerInstance("player2");
        controller.addPlayer(player);
        controller.addPlayer(player2);
        controller.setPlayerColor(Color.RED, player);
        controller.setPlayerColor(Color.BLUE, player2);
        controller.setReady(player);
        controller.setReady(player2);
        controller.start();
        StartingFrontFace startingFrontFace = new StartingFrontFace("image.jpg", new HashMap<>(), new ArrayList<>());
        StartingCard startingCard = new StartingCard(
                startingFrontFace,
                new CornerCardFace("image.jpg", new HashMap<>())
        );
        controller.setStartingCard(player, Face.FRONT);
        assertEquals(startingFrontFace,player.getManuscript().getCardByCoord(0,0));
    }
    @Test
    public void testGiveInitialHand() throws Exception
    {
        ServerConnectionHandler connectionHandler = new ServerConnectionHandler(true);
        Controller controller = new ControllerInstance(connectionHandler);
        Player player = new PlayerInstance("player1");
        Player player2 = new PlayerInstance("player2");
        controller.addPlayer(player);
        controller.addPlayer(player2);
        controller.setPlayerColor(Color.RED, player);
        controller.setPlayerColor(Color.BLUE, player2);
        controller.setReady(player);
        controller.setReady(player2);
        controller.start();
        controller.giveInitialHand();
        assertEquals(3, player.getHand().size());


    }
    @Test
    public void testNextTurn() throws Exception {
        ServerConnectionHandler connectionHandler = new ServerConnectionHandler(true);
        Controller controller = new ControllerInstance(connectionHandler);
        Player player = new PlayerInstance("player1");
        controller.addPlayer(player);
        controller.nextTurn();
        assertEquals(1, controller.getTurn());
    }
    @Test
    public void testPlayCard() throws Exception {
        ServerConnectionHandler connectionHandler = new ServerConnectionHandler(true);
        Controller controller = new ControllerInstance(connectionHandler);
        Player player = new PlayerInstance("player1");
        Player player2 = new PlayerInstance("player2");
        StartingCard startingCard = new StartingCard(new StartingFrontFace("image.jpg", new HashMap<>(), new ArrayList<>()), new CornerCardFace("image.jpg", new HashMap<>()));
        controller.addPlayer(player);
        controller.addPlayer(player2);
        controller.setPlayerColor(Color.RED, player);
        controller.setPlayerColor(Color.BLUE, player2);
        controller.setReady(player);
        controller.setReady(player2);
        //controller.start();
        controller.nextTurn();
        controller.setStartingCard(player,Face.FRONT);
        controller.setStartingCard(player2,Face.FRONT);
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
        player.removeCardFromHand(0);
        player.removeCardFromHand(0);
        player.removeCardFromHand(0);
        player2.removeCardFromHand(0);
        player2.removeCardFromHand(0);
        player2.removeCardFromHand(0);
        player.addCardToHand(resourceCard);
        player2.addCardToHand(resourceCard);
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
        player.addCardToHand(resourceCard2);
        player.addCardToHand(resourceCard2);
        player2.addCardToHand(resourceCard2);
        player2.addCardToHand(resourceCard2);
        Player player3 = controller.getPlayerList().get(0);
        controller.playCard(player3, 0,1,1,Face.BACK);
        assertEquals(resourceCard.getCornerFace(Face.BACK).getCornerSymbols(), player3.getManuscript().getCardByCoord(1,1).getCornerSymbols());
        assertEquals(resourceCard.getFace(Face.BACK).getPlacementTurn(), player3.getManuscript().getCardByCoord(1,1).getPlacementTurn());
        assertEquals(resourceCard.getFace(Face.BACK).getCenterSymbols(), player3.getManuscript().getCardByCoord(1,1).getCenterSymbols());
        player3.addCardToHand(resourceCard2);
        controller.playCard(player3, 0,-1,1,Face.FRONT);
        assertEquals(resourceCard2.getCornerFace(Face.FRONT).getCornerSymbols(), player3.getManuscript().getCardByCoord(-1,1).getCornerSymbols());
        assertEquals(resourceCard2.getFace(Face.FRONT).getPlacementTurn(), player3.getManuscript().getCardByCoord(-1,1).getPlacementTurn());
        assertEquals(resourceCard2.getCornerFace(Face.FRONT).getScore(), player3.getManuscript().getCardByCoord(-1,1).getScore());
        assertEquals(1, player3.getPoints());
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
        player3.addCardToHand(goldCard);
        controller.playCard(player3, 2,-1,-1,Face.FRONT);
        assertEquals(5, player3.getPoints());

    }
    @Test
    public void testDrawCard() throws Exception {
        int sentinel = 0,i;
        ServerConnectionHandler connectionHandler = new ServerConnectionHandler(true);
        Controller controller = new ControllerInstance(connectionHandler);
        Player player = new PlayerInstance("player1");
        Player player2 = new PlayerInstance("player2");
        controller.addPlayer(player);
        controller.addPlayer(player2);
        controller.setPlayerColor(Color.RED, player);
        controller.setPlayerColor(Color.BLUE, player2);
        controller.setReady(player);
        controller.setReady(player2);
        controller.start();
        controller.nextTurn();
        Card card ;
        Player player3 = controller.getPlayerList().get(0);
        controller.drawCard(player3, DeckPosition.DECK, Decks.RESOURCE);
        controller.drawCard(player3,DeckPosition.FIRST_CARD, Decks.GOLD);
        controller.drawCard(player3,DeckPosition.FIRST_CARD, Decks.GOLD);
        assertEquals(3, player3.getHand().size());
        ResourceDeck resourceDeck2 = new ResourceDeck();
        GoldDeck goldDeck = new GoldDeck();
        GoldDeck goldDeck1 = new GoldDeck();
        for (i = 0 ;  i < 38 ; i++){
            card = resourceDeck2.popCard(DeckPosition.DECK);
            if(card.getFace(Face.FRONT).getCornerSymbols().equals(player3.getHand().get(0).getFace(Face.FRONT).getCornerSymbols()) &&
                    card.getFace(Face.BACK).getCenterSymbols().equals(player3.getHand().get(0).getFace(Face.BACK).getCenterSymbols()) &&
                    card.getCornerFace(Face.FRONT).getScore() == player3.getHand().get(0).getCornerFace(Face.FRONT).getScore() &&
                    card.getFace(Face.FRONT).getKingdom().equals(player3.getHand().get(0).getFace(Face.BACK).getKingdom())){
                sentinel++;
            }
        }
        card = resourceDeck2.popCard(DeckPosition.FIRST_CARD);
        if(sentinel == 0 &&  card.getFace(Face.FRONT).getCornerSymbols().equals(player3.getHand().get(0).getFace(Face.FRONT).getCornerSymbols())
        && card.getFace(Face.BACK).getCenterSymbols().equals(player3.getHand().get(0).getFace(Face.BACK).getCenterSymbols()) &&
                card.getCornerFace(Face.FRONT).getScore() == player3.getHand().get(0).getCornerFace(Face.FRONT).getScore() &&
                card.getFace(Face.FRONT).getKingdom().equals(player3.getHand().get(0).getFace(Face.BACK).getKingdom())){
            sentinel++;
        }
        card = resourceDeck2.popCard(DeckPosition.SECOND_CARD);
        if(sentinel == 0 && card.getFace(Face.FRONT).getCornerSymbols().equals(player3.getHand().get(0).getFace(Face.FRONT).getCornerSymbols()) &&
                card.getFace(Face.BACK).getCenterSymbols().equals(player3.getHand().get(0).getFace(Face.BACK).getCenterSymbols()) &&
                card.getCornerFace(Face.FRONT).getScore() == player3.getHand().get(0).getCornerFace(Face.FRONT).getScore() &&
                card.getFace(Face.FRONT).getKingdom().equals(player3.getHand().get(0).getFace(Face.BACK).getKingdom()))
            {
            sentinel++;
            }
        assertEquals(1, sentinel);
        for (i = 0 ;  i < 38 ; i++){
            card = goldDeck.popCard(DeckPosition.DECK);
            if(card.getFace(Face.FRONT).getCornerSymbols().equals(player3.getHand().get(1).getFace(Face.FRONT).getCornerSymbols()) &&
                    card.getFace(Face.BACK).getCenterSymbols().equals(player3.getHand().get(1).getFace(Face.BACK).getCenterSymbols()) &&
                    card.getCornerFace(Face.FRONT).getScore() == player3.getHand().get(1).getCornerFace(Face.FRONT).getScore() &&
                    card.getFace(Face.FRONT).getKingdom().equals(player3.getHand().get(1).getFace(Face.BACK).getKingdom())){
                sentinel++;
            }
        }
        card = goldDeck.popCard(DeckPosition.FIRST_CARD);
        if(sentinel == 1 && card.getFace(Face.FRONT).getCornerSymbols().equals(player3.getHand().get(1).getFace(Face.FRONT).getCornerSymbols()) &&
                card.getFace(Face.BACK).getCenterSymbols().equals(player3.getHand().get(1).getFace(Face.BACK).getCenterSymbols()) &&
                card.getCornerFace(Face.FRONT).getScore() == player3.getHand().get(1).getCornerFace(Face.FRONT).getScore() &&
                card.getFace(Face.FRONT).getKingdom().equals(player3.getHand().get(1).getFace(Face.BACK).getKingdom())){
            sentinel++;
        }
        card = goldDeck.popCard(DeckPosition.SECOND_CARD);
        if(sentinel == 1 && card.getFace(Face.FRONT).getCornerSymbols().equals(player3.getHand().get(1).getFace(Face.FRONT).getCornerSymbols()) &&
                card.getFace(Face.BACK).getCenterSymbols().equals(player3.getHand().get(1).getFace(Face.BACK).getCenterSymbols()) &&
                card.getCornerFace(Face.FRONT).getScore() == player3.getHand().get(1).getCornerFace(Face.FRONT).getScore() &&
                card.getFace(Face.FRONT).getKingdom().equals(player3.getHand().get(1).getFace(Face.BACK).getKingdom()))
        {
            sentinel++;
        }
        assertEquals(2, sentinel);
        for (i = 0 ;  i < 38 ; i++){
            card = goldDeck1.popCard(DeckPosition.DECK);
            if(card.getFace(Face.FRONT).getCornerSymbols().equals(player3.getHand().get(2).getFace(Face.FRONT).getCornerSymbols()) &&
                    card.getFace(Face.BACK).getCenterSymbols().equals(player3.getHand().get(2).getFace(Face.BACK).getCenterSymbols()) &&
                    card.getCornerFace(Face.FRONT).getScore() == player3.getHand().get(2).getCornerFace(Face.FRONT).getScore() &&
                    card.getFace(Face.FRONT).getKingdom().equals(player3.getHand().get(2).getFace(Face.BACK).getKingdom())){
                sentinel++;
            }
        }
        card = goldDeck1.popCard(DeckPosition.FIRST_CARD);
        if(sentinel == 2 && card.getFace(Face.FRONT).getCornerSymbols().equals(player3.getHand().get(2).getFace(Face.FRONT).getCornerSymbols()) &&
                card.getFace(Face.BACK).getCenterSymbols().equals(player3.getHand().get(2).getFace(Face.BACK).getCenterSymbols()) &&
                card.getCornerFace(Face.FRONT).getScore() == player3.getHand().get(2).getCornerFace(Face.FRONT).getScore() &&
                card.getFace(Face.FRONT).getKingdom().equals(player3.getHand().get(2).getFace(Face.BACK).getKingdom())) {
            sentinel++;
        }
        card = goldDeck1.popCard(DeckPosition.SECOND_CARD);
        if(sentinel == 2 && card.getFace(Face.FRONT).getCornerSymbols().equals(player3.getHand().get(2).getFace(Face.FRONT).getCornerSymbols()) &&
                card.getFace(Face.BACK).getCenterSymbols().equals(player3.getHand().get(2).getFace(Face.BACK).getCenterSymbols()) &&
                card.getCornerFace(Face.FRONT).getScore() == player3.getHand().get(2).getCornerFace(Face.FRONT).getScore() &&
                card.getFace(Face.FRONT).getKingdom().equals(player3.getHand().get(2).getFace(Face.BACK).getKingdom()))
        {
            sentinel++;
        }
        assertEquals(3 , sentinel );
    }
    @Test
    public void testClearGame() throws IOException, TooManyPlayersException, MissingInfoException, AlreadyFinishedException {
        ServerConnectionHandler connectionHandler = new ServerConnectionHandler(true);
        Controller controller = new ControllerInstance(connectionHandler);
        Player player = new PlayerInstance("player1");
        controller.addPlayer(player);
        Player player2 = new PlayerInstance("player2");
        controller.addPlayer(player2);
        controller.nextTurn();
        controller.clear();
        assertEquals(0, controller.getPlayerList().size());
        assertEquals(0, controller.getTurn());
    }

    @Test
    public void testReady() throws IOException, TooManyPlayersException, MissingInfoException {
        ServerConnectionHandler connectionHandler = new ServerConnectionHandler(true);
        Controller controller = new ControllerInstance(connectionHandler);
        Player player = new PlayerInstance("player1");
        controller.addPlayer(player);
        assertFalse(player.isReady());
        controller.setPlayerColor(Color.RED, player);
        controller.setReady(player);
        assertTrue(player.isReady());
        controller.setNotReady(player);
        assertFalse(player.isReady());
    }

    @Test
    public void testChat() throws IOException, TooManyPlayersException {
        ServerConnectionHandler connectionHandler = new ServerConnectionHandler(true);
        Controller controller = new ControllerInstance(connectionHandler);
        Player player = new PlayerInstance("player1");
        controller.addPlayer(player);
        assertEquals(0, controller.getChatMessages().size());
        controller.addMessage("Hello", player);
        assertEquals(1, controller.getChatMessages().size());
        assertEquals("Hello", controller.getChatMessages().get(0).getMessage());
        assertEquals(player.getName(), controller.getChatMessages().get(0).getName());
    }

    @Test
    public void testSaveAndLoad() throws IOException, TooManyPlayersException, AlreadySetException, MissingInfoException, TooFewElementsException, AlreadyFinishedException {
        ServerConnectionHandler connectionHandler = new ServerConnectionHandler(true);
        Controller controller = new ControllerInstance(connectionHandler);
        Player player = new PlayerInstance("player1");
        controller.addPlayer(player);
        Player player2 = new PlayerInstance("player2");
        controller.addPlayer(player2);
        controller.setPlayerColor(Color.RED, player);
        controller.setPlayerColor(Color.BLUE, player2);
        controller.setReady(player);
        controller.setReady(player2);
        controller.start();
        controller.nextTurn();
        controller.giveInitialHand();
        controller.saveGame();
        controller.clear();
        controller.loadGame();
        assertEquals(2, controller.getPlayerList().size());
        assertEquals(3, controller.getPlayerList().get(0).getHand().size());
    }
    @Test
    public void testLeaderboard() throws TooManyPlayersException, MissingInfoException, TooFewElementsException, AlreadySetException, AlreadyFinishedException, TooManyElementsException, InvalidMoveException {
        Controller controller = new ControllerInstance(new ServerConnectionHandler(true));
        Player player = new PlayerInstance("player1");
        Player player2 = new PlayerInstance("player2");
        Player player3 = new PlayerInstance("player3");
        controller.addPlayer(player);
        controller.addPlayer(player2);
        controller.addPlayer(player3);
        controller.setPlayerColor(Color.RED, player);
        controller.setPlayerColor(Color.BLUE, player2);
        controller.setPlayerColor(Color.GREEN, player3);
        controller.setReady(player);
        controller.setReady(player2);
        controller.setReady(player3);
        controller.start();
        player = controller.getPlayerList().get(0);
        player2 = controller.getPlayerList().get(1);
        player3 = controller.getPlayerList().get(2);
        Map <CardCorners, Symbol> cornerSymbolsStarting = new HashMap<>();
        cornerSymbolsStarting.put(CardCorners.TOP_LEFT, Symbol.FUNGUS);
        cornerSymbolsStarting.put(CardCorners.TOP_RIGHT, Symbol.PLANT);
        cornerSymbolsStarting.put(CardCorners.BOTTOM_LEFT, Symbol.EMPTY);
        cornerSymbolsStarting.put(CardCorners.BOTTOM_RIGHT, Symbol.EMPTY);
        StartingFrontFace startingFrontFace = new StartingFrontFace("image.jpg", cornerSymbolsStarting, new ArrayList<>(List.of(Symbol.BUG)));
        StartingCard startingCard = new StartingCard(
                startingFrontFace,
                new CornerCardFace("image.jpg", new HashMap<>())
        );
        controller.setStartingCard(player, Face.FRONT);
        controller.setStartingCard(player2, Face.FRONT);
        controller.setStartingCard(player3, Face.FRONT);
        player.removeCardFromHand(0);
        player.removeCardFromHand(0);
        player.removeCardFromHand(0);
        player2.removeCardFromHand(0);
        player2.removeCardFromHand(0);
        player2.removeCardFromHand(0);
        player3.removeCardFromHand(0);
        player3.removeCardFromHand(0);
        player3.removeCardFromHand(0);
        ResourceFrontFace resourceFrontFace = new ResourceFrontFace("image.jpg", cornerSymbolsStarting, 0, Symbol.BUG);
        ResourceCard resourceCard = new ResourceCard(
                resourceFrontFace,
                new RegularBackFace("image.jpg", new ArrayList<>(List.of(Symbol.BUG)))
        );
        Map <CardCorners, Symbol> cornerSymbolsr2 = new HashMap<>();
        cornerSymbolsr2.put(CardCorners.TOP_LEFT, Symbol.EMPTY);
        cornerSymbolsr2.put(CardCorners.TOP_RIGHT, Symbol.PLANT);
        cornerSymbolsr2.put(CardCorners.BOTTOM_LEFT, Symbol.BUG);
        cornerSymbolsr2.put(CardCorners.BOTTOM_RIGHT, Symbol.EMPTY);
        ResourceFrontFace resourceFrontFace2 = new ResourceFrontFace("image.jpg", cornerSymbolsr2, 0, Symbol.BUG);
        ResourceCard resourceCard2 = new ResourceCard(
                resourceFrontFace2,
                new RegularBackFace("image.jpg", new ArrayList<>(List.of(Symbol.BUG)))
        );
        Map<CardCorners,Symbol> cornerSymbolsGold = new HashMap<>();
        cornerSymbolsGold.put(CardCorners.TOP_LEFT, Symbol.EMPTY);
        cornerSymbolsGold.put(CardCorners.TOP_RIGHT, Symbol.EMPTY);
        cornerSymbolsGold.put(CardCorners.BOTTOM_LEFT, Symbol.EMPTY);
        cornerSymbolsGold.put(CardCorners.BOTTOM_RIGHT, Symbol.EMPTY);
        Map<Symbol,Integer> scoreRequirements = new HashMap<>();
        scoreRequirements.put(Symbol.PLANT, 3);
        GoldFrontFace goldFrontFace = new GoldFrontFace("image.jpg", cornerSymbolsGold, 3, new HashMap<>(), scoreRequirements, Symbol.BUG);
        GoldCard goldCard = new GoldCard(
                goldFrontFace,
                new RegularBackFace("image.jpg", new ArrayList<>(List.of(Symbol.BUG)))
        );
        Map<Symbol,Integer> achievementRequirements = new HashMap<>();
        achievementRequirements.put(Symbol.PATTERN2B, 2);
        AchievementFrontFace achievementFrontFace = new AchievementFrontFace("image.jpg", achievementRequirements, 2);
        AchievementCard achievementCard = new AchievementCard(
                achievementFrontFace,
                new EmptyCardFace("image.jpg")
        );
        Map<CardCorners,Symbol> cornerSymbols3 = new HashMap<>();
        cornerSymbols3.put(CardCorners.TOP_LEFT, Symbol.EMPTY);
        cornerSymbols3.put(CardCorners.TOP_RIGHT, Symbol.ANIMAL);
        cornerSymbols3.put(CardCorners.BOTTOM_LEFT, Symbol.ANIMAL);
        cornerSymbols3.put(CardCorners.BOTTOM_RIGHT, Symbol.EMPTY);
        ResourceCard resourceCard3 = new ResourceCard(
                new ResourceFrontFace("image.jpg", cornerSymbols3 , 0, Symbol.ANIMAL),
                new RegularBackFace("image.jpg", new ArrayList<>(List.of(Symbol.ANIMAL)))
        );
        Map<CardCorners,Symbol> cornerSymbols4 = new HashMap<>();
        cornerSymbols4.put(CardCorners.TOP_LEFT, Symbol.QUILL);
        cornerSymbols4.put(CardCorners.TOP_RIGHT, Symbol.EMPTY);
        cornerSymbols4.put(CardCorners.BOTTOM_LEFT, Symbol.EMPTY);
        cornerSymbols4.put(CardCorners.BOTTOM_RIGHT, Symbol.EMPTY);
        ResourceFrontFace resourceFrontFace4 = new ResourceFrontFace("image.jpg", cornerSymbols4, 0, Symbol.ANIMAL);
        ResourceCard resourceCard4 = new ResourceCard(
                resourceFrontFace4,
                new RegularBackFace("image.jpg", new ArrayList<>(List.of(Symbol.ANIMAL)))
        );
        Map<CardCorners,Symbol> cornerSymbols5 = new HashMap<>();
        cornerSymbols5.put(CardCorners.TOP_LEFT, Symbol.EMPTY);
        cornerSymbols5.put(CardCorners.TOP_RIGHT, Symbol.EMPTY);
        cornerSymbols5.put(CardCorners.BOTTOM_LEFT, Symbol.EMPTY);
        cornerSymbols5.put(CardCorners.BOTTOM_RIGHT, Symbol.EMPTY);
        Map<Symbol,Integer> scoreRequirements2 = new HashMap<>();
        scoreRequirements2.put(Symbol.ANIMAL, 1);
        GoldFrontFace goldFrontFace2 = new GoldFrontFace("image.jpg", cornerSymbols5, 2, new HashMap<>(), scoreRequirements2, Symbol.ANIMAL);
        GoldCard goldCard2 = new GoldCard(
                goldFrontFace2,
                new RegularBackFace("image.jpg", new ArrayList<>(List.of(Symbol.ANIMAL)))
        );
        Map<Symbol,Integer> achievementRequirements2 = new HashMap<>();
        achievementRequirements2.put(Symbol.QUILL, 1);
        AchievementFrontFace achievementFrontFace2 = new AchievementFrontFace("image.jpg", achievementRequirements2, 1);
        AchievementCard achievementCard2 = new AchievementCard(
                achievementFrontFace2,
                new EmptyCardFace("image.jpg")
        );
        Map<CardCorners,Symbol> cornerSymbols6 = new HashMap<>();
        cornerSymbols6.put(CardCorners.TOP_LEFT, Symbol.FUNGUS);
        cornerSymbols6.put(CardCorners.TOP_RIGHT, Symbol.FUNGUS);
        cornerSymbols6.put(CardCorners.BOTTOM_LEFT, Symbol.FUNGUS);
        cornerSymbols6.put(CardCorners.BOTTOM_RIGHT, Symbol.FUNGUS);
        ResourceFrontFace resourceFrontFace6 = new ResourceFrontFace("image.jpg", cornerSymbols6, 0, Symbol.FUNGUS);
        ResourceCard resourceCard6 = new ResourceCard(
                resourceFrontFace6,
                new RegularBackFace("image.jpg", new ArrayList<>(List.of(Symbol.FUNGUS)))
        );
        Map<CardCorners,Symbol> cornerSymbols7 = new HashMap<>();
        cornerSymbols7.put(CardCorners.TOP_LEFT, Symbol.FUNGUS);
        cornerSymbols7.put(CardCorners.TOP_RIGHT, Symbol.FUNGUS);
        cornerSymbols7.put(CardCorners.BOTTOM_LEFT, Symbol.FUNGUS);
        cornerSymbols7.put(CardCorners.BOTTOM_RIGHT, Symbol.FUNGUS);
        ResourceFrontFace resourceFrontFace7 = new ResourceFrontFace("image.jpg", cornerSymbols7, 0, Symbol.FUNGUS);
        ResourceCard resourceCard7 = new ResourceCard(
                resourceFrontFace7,
                new RegularBackFace("image.jpg", new ArrayList<>(List.of(Symbol.FUNGUS)))
        );
        Map<Symbol,Integer> achievementRequirements3 = new HashMap<>();
        achievementRequirements3.put(Symbol.FUNGUS, 3);
        AchievementFrontFace achievementFrontFace3 = new AchievementFrontFace("image.jpg", achievementRequirements3, 3);
        AchievementCard achievementCard3 = new AchievementCard(
                achievementFrontFace3,
                new EmptyCardFace("image.jpg")
        );
        player.addCardToHand(resourceCard);
        player.addCardToHand(resourceCard2);
        player.addCardToHand(goldCard);
        player2.addCardToHand(resourceCard3);
        player2.addCardToHand(resourceCard4);
        player2.addCardToHand(goldCard2);
        player3.addCardToHand(resourceCard6);
        player3.addCardToHand(resourceCard7);
        player3.addCardToHand(goldCard2);
        controller.setSecretObjectiveCard(player, achievementCard);
        controller.setSecretObjectiveCard(player2, achievementCard2);
        controller.setSecretObjectiveCard(player3, achievementCard3);
        //qui fa next turn
        controller.playCard(player, 0,1,-1,Face.FRONT);
        controller.drawCard(player, DeckPosition.DECK, Decks.RESOURCE);
        controller.nextTurn();
        controller.playCard(player2, 0,-1,1,Face.FRONT);
        controller.drawCard(player2, DeckPosition.DECK, Decks.RESOURCE);
        controller.nextTurn();
        controller.playCard(player3, 0,-1,-1,Face.FRONT);
        controller.drawCard(player3, DeckPosition.DECK, Decks.RESOURCE);
        controller.nextTurn();
        controller.playCard(player, 0,2,-2,Face.FRONT);
        controller.drawCard(player, DeckPosition.DECK, Decks.RESOURCE);
        controller.nextTurn();
        controller.playCard(player2, 0,-1,-1,Face.FRONT);
        controller.drawCard(player2, DeckPosition.DECK, Decks.RESOURCE);
        controller.nextTurn();
        controller.playCard(player3, 0,1,-1,Face.FRONT);
        controller.drawCard(player3, DeckPosition.DECK, Decks.RESOURCE);
        controller.nextTurn();
        controller.playCard(player, 0,3,-3,Face.FRONT);
        controller.drawCard(player, DeckPosition.DECK, Decks.RESOURCE);
        controller.nextTurn();
        controller.playCard(player2, 0,1,-1,Face.FRONT);
        controller.nextTurn();
        List<Player> leaderboard = controller.computeLeaderboard();
        assertEquals(5, controller.getPlayerList().get(0).getPoints());
        assertEquals(6, controller.getPlayerList().get(1).getPoints());
        assertEquals(6, controller.getPlayerList().get(2).getPoints());
        assertEquals(controller.getPlayerList().get(2).getName(), leaderboard.get(0).getName());
        assertEquals(controller.getPlayerList().get(1).getName(), leaderboard.get(1).getName());
        assertEquals(controller.getPlayerList().get(0).getName(), leaderboard.get(2).getName());




    }*/
}
