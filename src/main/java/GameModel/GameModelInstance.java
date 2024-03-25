package main.java.GameModel;

import main.java.Card.RegularBackFace;
import main.java.Card.StartingCard;
import main.java.Card.StartingFrontFace;
import main.java.Chat.Chat;
import main.java.Deck.AchievementDeck;
import main.java.Deck.GoldDeck;
import main.java.Deck.ResourceDeck;
import main.java.Enums.Symbol;
import main.java.Player.Player;

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
        System.out.println(this.toString() + "GameModelInstance");
        resourceDeck = new ResourceDeck();
        goldDeck = new GoldDeck();
        achievementDeck = new AchievementDeck();
        chat = new Chat();
        isEndGamePhase = false;
        turn = 0;
        generateStartingCards();

    }

    private void generateStartingCards() {
        try {
            File fileFRONT = new File("C:\\Users\\ago19\\IdeaProjects\\IS24-AM04\\images\\StartingCardsFRONT.txt");
            File fileBACK = new File("C:\\Users\\ago19\\IdeaProjects\\IS24-AM04\\images\\StartingCardsBACK.txt");

            BufferedReader readerFRONT = new BufferedReader(new FileReader(fileFRONT));
            String line;
            while ((line = readerFRONT.readLine()) != null) {
                String[] parts = line.split(" ");
                System.out.println(line);

                Map<Integer, Symbol> cornerSymbols = new HashMap<Integer, Symbol>();
                List<Symbol> centerSymbols = new ArrayList<Symbol>();
                for (int i = 0; i < parts.length; i++) {
                    if (i < 4) cornerSymbols.put(i, Symbol.valueOf(parts[i + 1]));
                    else centerSymbols.add(Symbol.valueOf(parts[i]));
                }

                StartingFrontFace frontFace = new StartingFrontFace(parts[0], cornerSymbols, centerSymbols);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        //Collection.shuffle(startingCards);
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
