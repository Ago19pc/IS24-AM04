package main.java.Server.GameModel;

import main.java.Server.Card.CornerCardFace;
import main.java.Server.Card.RegularBackFace;
import main.java.Server.Card.StartingCard;
import main.java.Server.Card.StartingFrontFace;
import main.java.Server.Chat.Chat;
import main.java.Server.Deck.AchievementDeck;
import main.java.Server.Deck.GoldDeck;
import main.java.Server.Deck.ResourceDeck;
import main.java.Server.Enums.Symbol;
import static main.java.Server.Enums.Face.BACK;
import static main.java.Server.Enums.Face.FRONT;
import main.java.Server.Player.Player;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;


public class GameModelInstance implements GameModel {
    private ResourceDeck resourceDeck;
    private GoldDeck goldDeck;
    private AchievementDeck achievementDeck;
    private List<StartingCard> startingCards;
    private int turn;
    private boolean isEndGamePhase;
    private Chat chat;

    private List<Player> playerList;

    public GameModelInstance() {
        System.out.println(this.toString() + " GameModelInstance");
        resourceDeck = new ResourceDeck();
        goldDeck = new GoldDeck();
        achievementDeck = new AchievementDeck();
        chat = new Chat();
        isEndGamePhase = false;
        turn = 0;
        startingCards = new ArrayList<StartingCard>();
        generateStartingCards();

    }

    private void generateStartingCards() {
        File fileFRONT = null;
        File fileBACK = null;
        BufferedReader readerFRONT = null;
        BufferedReader readerBACK = null;

        try {
            fileFRONT = new File("images\\StartingCardsFRONT.txt");
            fileBACK = new File("images\\StartingCardsBACK.txt");
            readerFRONT = new BufferedReader(new FileReader(fileFRONT));
            readerBACK = new BufferedReader(new FileReader(fileBACK));
        
        

        String lineF;
        while ((lineF = readerFRONT.readLine()) != null) {

            String[] partsF = lineF.split(" ");
            //System.out.println(lineF);

            String[] partsB = readerBACK.readLine().split(" ");
            //System.out.println(partsB);

            // La faccia davanti ha sia angoli che centrali

            Map<Integer, Symbol> cornerSymbolsF = new HashMap<Integer, Symbol>();
            List<Symbol> centerSymbols = new ArrayList<Symbol>();
            for (int i = 0; i < partsF.length; i++) {
                if (i < 4) cornerSymbolsF.put(i, Symbol.valueOf(partsF[i]));
                else centerSymbols.add(Symbol.valueOf(partsF[i]));
            }
            
            StartingFrontFace frontFace = new StartingFrontFace("STARTINGFRONT", cornerSymbolsF, centerSymbols);
            
            // La faccia dietro ha solo angoli
            Map<Integer, Symbol> cornerSymbolsB = new HashMap<Integer, Symbol>();
            for (int i = 0; i < partsB.length; i++) {
                cornerSymbolsB.put(i, Symbol.valueOf(partsB[i]));
            }

            CornerCardFace backFace = new CornerCardFace("STARTINGBACK", cornerSymbolsB);
            
            StartingCard card = new StartingCard(frontFace, backFace);
            startingCards.add(card);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
        /*
         * This is to print to check if the cards are generated correctly 
         for(StartingCard card : startingCards) {
             System.out.println("Front Center Symbols:");
             card.getFace(FRONT).getCenterSymbols().forEach(symbol -> System.out.println(symbol));
             System.out.println("Front Corner Symbols:");
             card.getFace(FRONT).getCornerSymbols().forEach((key, value) -> System.out.println(key + " " + value));
             
             System.out.println("Back Corner Symbols:");
             card.getFace(BACK).getCornerSymbols().forEach((key, value) -> System.out.println(key + " " + value));
             System.out.println();
            }
        */
            
        Collections.shuffle(startingCards);


        
    }

    public List<Player> getPlayerList() {
        return playerList;
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
