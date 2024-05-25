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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;


public class GameModelInstance implements GameModel{
    private ResourceDeck resourceDeck;
    private GoldDeck goldDeck;
    private AchievementDeck achievementDeck;
    private List<StartingCard> startingCards;
    private int turn;
    private boolean isEndGamePhase;
    private final Chat chat;

    private final List<PlayerInstance> playerList;


    public GameModelInstance() {
        chat = new Chat();
        isEndGamePhase = false;
        turn = 0;
        playerList = new ArrayList<>();
    }


    /**
     * Generate the starting cards
     */
    private void generateStartingCards() {
        File fileFRONT;
        File fileBACK;
        BufferedReader readerFRONT;
        BufferedReader readerBACK;

        try {
            fileFRONT = new File(getClass().getResource("/images/StartingCardsFRONT.txt").toURI());
            fileBACK = new File(getClass().getResource("/images/StartingCardsBACK.txt").toURI());
            readerFRONT = new BufferedReader(new FileReader(fileFRONT));
            readerBACK = new BufferedReader(new FileReader(fileBACK));
        
        

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
                        //System.out.print(CardCorners.values()[i]);
                        //System.out.print(" ");
                        //System.out.println(Symbol.valueOf(partsF[i]));
                        cornerSymbolsF.put(CardCorners.values()[i], Symbol.valueOf(partsF[i]));
                    }
                    else centerSymbols.add(Symbol.valueOf(partsF[i]));
                }

                StartingFrontFace frontFace = new StartingFrontFace(counter + ".jpeg", cornerSymbolsF, centerSymbols);

                // La faccia dietro ha solo angoli
                Map<CardCorners, Symbol> cornerSymbolsB = new HashMap<>();
                for (int i = 0; i < partsB.length; i++) {
                    cornerSymbolsB.put(CardCorners.values()[i], Symbol.valueOf(partsB[i]));
                }

                CornerCardFace backFace = new CornerCardFace(counter + ".jpeg", cornerSymbolsB);

                StartingCard card = new StartingCard(frontFace, backFace, counter + ".jpeg");
                startingCards.add(card);
            }
        } catch (Exception e) {
            System.out.println("An error occurred while generating starting cards");
            e.printStackTrace();
        }
        Collections.shuffle(startingCards);
    
    }

    public List<Player> getPlayerList() {
        List<Player> playerListToReturn = new ArrayList<>();
        for (PlayerInstance player : playerList) {
            playerListToReturn.add((Player) player);
        }
        return playerListToReturn;
    }
    public void setPlayerList(List<Player> playerList) {
        this.playerList.clear();
        for (Player player : playerList) {
            this.playerList.add((PlayerInstance) player);
        }
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
