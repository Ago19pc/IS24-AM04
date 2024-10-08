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
import Server.Exception.AlreadySetException;
import Server.Player.Player;
import Server.Player.PlayerInstance;

import java.io.*;
import java.util.*;

/**
 * Implementation of the GameModel interface
 */
public class GameModelInstance implements GameModel{
    private ResourceDeck resourceDeck;
    private GoldDeck goldDeck;
    private AchievementDeck achievementDeck;
    private List<StartingCard> startingCards;
    private int turn;
    private boolean isEndGamePhase;
    private final Chat chat;
    private int activePlayerIndex = -1;
    private boolean lastRound = false;
    private final List<PlayerInstance> playerList;

    /**
     * Constructor
     * Initializes data
     */
    public GameModelInstance() {
        chat = new Chat();
        isEndGamePhase = false;
        turn = 0;
        playerList = new ArrayList<>();
    }

    public int getActivePlayerIndex() {
        return activePlayerIndex;
    }

    public boolean isLastRound() {
        return lastRound;
    }

    public void setLastRound(boolean lastRound) {
        this.lastRound = lastRound;
    }

    public void setActivePlayerIndex(int activePlayerIndex) {
        this.activePlayerIndex = activePlayerIndex;
    }


    /**
     * Generate the starting cards
     */
    private void generateStartingCards() {
        InputStream fileFRONT;
        InputStream fileBACK;
        BufferedReader readerFRONT;
        BufferedReader readerBACK;

        try {
            fileFRONT = getClass().getResourceAsStream("/images/StartingCardsFRONT.txt");
            fileBACK = getClass().getResourceAsStream("/images/StartingCardsBACK.txt");
            assert fileFRONT != null;
            readerFRONT = new BufferedReader(new InputStreamReader(fileFRONT));
            assert fileBACK != null;
            readerBACK = new BufferedReader(new InputStreamReader(fileBACK));
        
        

            String lineF;
            int counter = 79;
            while ((lineF = readerFRONT.readLine()) != null) {
                counter++;

                String[] partsF = lineF.split(" ");

                String[] partsB = readerBACK.readLine().split(" ");

                // La faccia davanti ha sia angoli che centrali

                Map<CardCorners, Symbol> cornerSymbolsF = new HashMap<>();
                List<Symbol> centerSymbols = new ArrayList<>();
                for (int i = 0; i < partsF.length; i++) {
                    if (i < 4) {
                        cornerSymbolsF.put(CardCorners.values()[i], Symbol.valueOf(partsF[i]));
                    }
                    else centerSymbols.add(Symbol.valueOf(partsF[i]));
                }

                StartingFrontFace frontFace = new StartingFrontFace("front-" + counter + ".jpeg", cornerSymbolsF, centerSymbols);

                // La faccia dietro ha solo angoli
                Map<CardCorners, Symbol> cornerSymbolsB = new HashMap<>();
                for (int i = 0; i < partsB.length; i++) {
                    cornerSymbolsB.put(CardCorners.values()[i], Symbol.valueOf(partsB[i]));
                }

                CornerCardFace backFace = new CornerCardFace("back-" + counter + ".jpeg", cornerSymbolsB);

                StartingCard card = new StartingCard(frontFace, backFace);
                startingCards.add(card);
            }
        } catch (Exception e) {
            System.err.println("An error occurred while generating starting cards");
        }
        Collections.shuffle(startingCards);
    
    }

    public List<Player> getPlayerList() {
        return new LinkedList<>(playerList);
    }

    public void addPlayer(Player player) throws IllegalArgumentException{
        for (PlayerInstance playerInstance : playerList) {
            if (playerInstance.getName().equals(player.getName())) {
                throw new IllegalArgumentException("Player with same name already exists");
            }
        }
        PlayerInstance playerInstance = (PlayerInstance) player;
        playerList.add(playerInstance);
    }


    @Override
    public void nextTurn() {
        this.turn++;
    }


    @Override
    public ResourceDeck getResourceDeck() {
        return this.resourceDeck;
    }


    @Override
    public GoldDeck getGoldDeck() {
        return this.goldDeck;
    }


    @Override
    public AchievementDeck getAchievementDeck() {
        return this.achievementDeck;
    }


    @Override
    public List<StartingCard> getStartingCards() {
        return this.startingCards;
    }


    @Override
    public int getTurn() {
        return this.turn;
    }


    @Override
    public boolean isEndGamePhase() {
        return this.isEndGamePhase;
    }


    @Override
    public void setEndGamePhase() throws AlreadySetException {
        if(isEndGamePhase){
            throw new AlreadySetException("The game is already in the end game phase");
        }
        this.isEndGamePhase = true;
    }

    /**
     * @return Chat the chat
     */
    @Override
    public Chat getChat() {
        return this.chat;
    }

    public void removePlayer(Player player) throws IllegalArgumentException{
        boolean removed = false;
        for (PlayerInstance playerInstance : playerList) {
            if (playerInstance.getName().equals(player.getName())) {
                playerList.remove(playerInstance);
                removed = true;
                break;
            }
        }
        if (!removed) {
            throw new IllegalArgumentException("Player not found");
        }
    }
    public void shufflePlayerList() {
        Collections.shuffle(playerList);
    }

    @Override
    public void createGoldResourceDecks() throws AlreadySetException {
        if(goldDeck != null || resourceDeck != null){
            throw new AlreadySetException("Gold and resource decks already created");
        }
        resourceDeck = new ResourceDeck();
        goldDeck = new GoldDeck();
    }

    @Override
    public void createStartingCards() throws AlreadySetException {
        if(startingCards != null){
            throw new AlreadySetException("Starting cards already created");
        }
        startingCards = new ArrayList<>();
        generateStartingCards();
    }

    @Override
    public void createAchievementDeck() throws AlreadySetException {
        if(achievementDeck != null){
            throw new AlreadySetException("Achievement deck already created");
        }
        achievementDeck = new AchievementDeck();
    }
}
