package Server.GameModel;

import Server.Card.CornerCardFace;
import Server.Card.StartingCard;
import Server.Card.StartingFrontFace;
import Server.Chat.Chat;
import Server.Deck.AchievementDeck;
import Server.Deck.GoldDeck;
import Server.Deck.ResourceDeck;
import Server.Enums.CardCorners;
import Server.Enums.Symbol;
import Server.EventManager.EventManager;
import Server.Player.Player;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;


public class GameModelInstance implements GameModel {
    private final ResourceDeck resourceDeck;
    private final GoldDeck goldDeck;
    private final AchievementDeck achievementDeck;
    private final List<StartingCard> startingCards;
    private int turn;
    private boolean isEndGamePhase;
    private final Chat chat;

    private List<Player> playerList;

    private final EventManager eventManager;

    public GameModelInstance(EventManager eventManager) {
        System.out.println("GameModelInstance");
        resourceDeck = new ResourceDeck();
        goldDeck = new GoldDeck();
        achievementDeck = new AchievementDeck();
        chat = new Chat();
        isEndGamePhase = false;
        turn = 0;
        startingCards = new ArrayList<>();
        this.eventManager = eventManager;
        generateStartingCards();

    }

    public EventManager getEventManager() {
        return eventManager;
    }

    private void generateStartingCards() {
        File fileFRONT;
        File fileBACK;
        BufferedReader readerFRONT;
        BufferedReader readerBACK;

        try {
            fileFRONT = new File("images\\StartingCardsFRONT.txt");
            fileBACK = new File("images\\StartingCardsBACK.txt");
            readerFRONT = new BufferedReader(new FileReader(fileFRONT));
            readerBACK = new BufferedReader(new FileReader(fileBACK));
        
        

        String lineF;
        while ((lineF = readerFRONT.readLine()) != null) {

            String[] partsF = lineF.split(" ");

            String[] partsB = readerBACK.readLine().split(" ");

            // La faccia davanti ha sia angoli che centrali

            Map<CardCorners, Symbol> cornerSymbolsF = new HashMap<>();
            List<Symbol> centerSymbols = new ArrayList<>();
            for (int i = 0; i < partsF.length; i++) {
                if (i < 4) cornerSymbolsF.put(CardCorners.values()[i], Symbol.valueOf(partsF[i]));
                else centerSymbols.add(Symbol.valueOf(partsF[i]));
            }
            
            StartingFrontFace frontFace = new StartingFrontFace("STARTINGFRONT", cornerSymbolsF, centerSymbols);
            
            // La faccia dietro ha solo angoli
            Map<CardCorners, Symbol> cornerSymbolsB = new HashMap<>();
            for (int i = 0; i < partsB.length; i++) {
                cornerSymbolsB.put(CardCorners.values()[i], Symbol.valueOf(partsB[i]));
            }

            CornerCardFace backFace = new CornerCardFace("STARTINGBACK", cornerSymbolsB);
            
            StartingCard card = new StartingCard(frontFace, backFace);
            startingCards.add(card);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }       
       Collections.shuffle(startingCards);
    
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }

    public void addPlayer(Player player) {
        playerList.add(player);
    }

    List<Player> getPlayers() {
        return this.playerList;
    }

    /**
     * Adds 1 to turn
     */
    @Override
    public void nextTurn() {
        this.turn++;
    }

    /**
     * @return ResourceDeck the resource deck
     */
    @Override
    public ResourceDeck getResourceDeck() {
        return this.resourceDeck;
    }

    /**
     * @return GoldDeck the gold deck
     */
    @Override
    public GoldDeck getGoldDeck() {
        return this.goldDeck;
    }

    /**
     * @return AchievementDeck the achievement deck
     */
    @Override
    public AchievementDeck getAchievementDeck() {
        return this.achievementDeck;
    }

    /**
     * @return List<StartingCard> the starting cards
     */
    @Override
    public List<StartingCard> getStartingCards() {
        return this.startingCards;
    }

    /**
     * @return int the turn
     */
    @Override
    public int getTurn() {
        return this.turn;
    }

    /**
     * @return boolean true if the game is in the end game phase
     */
    @Override
    public boolean isEndGamePhase() {
        return this.isEndGamePhase;
    }

    /**
     * Set the game to the end game phase
     */
    @Override
    public void setEndGamePhase() {
        this.isEndGamePhase = true;
    }

    /**
     * @return Chat the chat
     */
    @Override
    public Chat getChat() {
        return this.chat;
    }
}
