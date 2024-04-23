package Server.Controller;

import Server.Card.*;
import Server.Connections.ServerConnectionHandler;
import Server.Deck.GoldDeck;
import Server.Deck.ResourceDeck;
import Server.Enums.*;
import Server.Exception.AlreadySetException;
import Server.Exception.MissingInfoException;
import Server.Exception.TooManyPlayersException;
import Server.Player.Player;
import Server.Player.PlayerInstance;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TestController {
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
        ServerConnectionHandler connectionHandler = new ServerConnectionHandler(true);
        Controller controller = new ControllerInstance(connectionHandler);
        Player player = new PlayerInstance("player1");
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
        ServerConnectionHandler connectionHandler = new ServerConnectionHandler(true);
        Controller controller = new ControllerInstance(connectionHandler);
        Player player = new PlayerInstance("player1");
        controller.addPlayer(player);
        controller.giveInitialHand();
        assertEquals(3, player.getHand().size());


    }
    @Test
    public void testNextTurn() throws Exception {
        ServerConnectionHandler connectionHandler = new ServerConnectionHandler(true);
        Controller controller = new ControllerInstance(connectionHandler);
        controller.nextTurn();
        assertEquals(1, controller.getTurn());
    }
    @Test
    public void testPlayCard() throws Exception {
        ServerConnectionHandler connectionHandler = new ServerConnectionHandler(true);
        Controller controller = new ControllerInstance(connectionHandler);
        Player player = new PlayerInstance("player1");
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
        player.addCardToHand(resourceCard);
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
        controller.playCard(player, 0,1,1,Face.BACK);
        assertEquals(resourceCard.getCornerFace(Face.BACK).getCornerSymbols(), player.getManuscript().getCardByCoord(1,1).getCornerSymbols());
        assertEquals(resourceCard.getFace(Face.BACK).getPlacementTurn(), player.getManuscript().getCardByCoord(1,1).getPlacementTurn());
        assertEquals(resourceCard.getFace(Face.BACK).getCenterSymbols(), player.getManuscript().getCardByCoord(1,1).getCenterSymbols());
        player.addCardToHand(resourceCard2);
        controller.playCard(player, 0,-1,1,Face.FRONT);
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
        player.addCardToHand(goldCard);
        controller.playCard(player, 2,-1,-1,Face.FRONT);
        assertEquals(5, player.getPoints());
        //System.out.println(resourceCard.getCornerFace(Face.BACK)==player.getManuscript().getCardByCoord(1,1));
        //riga 169 da false perché non è stato fatto l'equals delle cardFace

    }
    @Test
    public void testDrawCard() throws Exception {
        int sentinel = 0, i = 0;
        ServerConnectionHandler connectionHandler = new ServerConnectionHandler(true);
        Controller controller = new ControllerInstance(connectionHandler);
        Player player = new PlayerInstance("player1");
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
    //todo: redo testleaderboard
    @Test
    public void testClearGame() throws IOException, TooManyPlayersException {
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
        assertEquals(player, controller.getChatMessages().get(0).getSender());
    }

    @Test
    public void testSaveAndLoad() throws IOException, TooManyPlayersException, AlreadySetException {
        ServerConnectionHandler connectionHandler = new ServerConnectionHandler(true);
        Controller controller = new ControllerInstance(connectionHandler);
        Player player = new PlayerInstance("player1");
        controller.addPlayer(player);
        Player player2 = new PlayerInstance("player2");
        controller.addPlayer(player2);
        controller.nextTurn();
        controller.giveInitialHand();
        controller.saveGame();
        controller.clear();
        controller.loadGame();
        assertEquals(2, controller.getPlayerList().size());
        assertEquals(3, controller.getPlayerList().get(0).getHand().size());
    }
}
