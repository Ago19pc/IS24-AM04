package Server.Controller;

import Client.Deck;
import Server.Card.*;
import Server.Chat.Message;
import Server.Connections.GeneralServerConnectionHandler;
import Server.Deck.AchievementDeck;
import Server.Enums.*;
import Server.Exception.*;
import Server.GameModel.GameModel;
import Server.GameModel.GameModelInstance;
import Server.Manuscript.Manuscript;
import Server.Messages.*;
import Server.Player.Player;
import Server.Player.PlayerInstance;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

/**
 * This is the implementation of the controller interface. It manages the game flow and the game model.
 */
public class ControllerInstance implements Controller{
    private GameModel gameModel;
    private final GeneralServerConnectionHandler connectionHandler;
    private Map<Player, StartingCard> givenStartingCards = new HashMap<>();
    private Map<Player, List<AchievementCard>> givenSecretObjectiveCards = new HashMap<>();
    private GameState gameState = GameState.LOBBY;

    /**
     * Asks whether the user wants to start a new game or load a saved game and initializes the game model
     * @param connectionHandler the connection handler
     */
    public ControllerInstance(GeneralServerConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
        this.gameModel = new GameModelInstance();
        Scanner inputReader = new Scanner(System.in);
        System.out.println("Vuoi iniziare una nuova partita (n) o caricare una partita salvata (l)?");
        String input = inputReader.nextLine();
        if(input.equals("l")){
            loadGame();
            gameState = GameState.LOAD_GAME_LOBBY;
            System.out.println("Partita caricata. I giocatori sono:");
            for (Player p : gameModel.getPlayerList()){
                System.out.println(p.getName());
            }
        }
    }
    @Override
    public void addPlayer(String name, String clientID) throws TooManyPlayersException, AlreadyStartedException, IllegalArgumentException {
    if(connectionHandler.isIdConnectedToName(clientID)) throw new IllegalArgumentException("Player already connected");
    if(gameState != GameState.LOBBY) throw new AlreadyStartedException("Game already started");
    Player player = new PlayerInstance(name);
    for (Player p : gameModel.getPlayerList()){
        if (p.getName().equals(player.getName())) throw new IllegalArgumentException("Player with same name already exists");
    }
    if( name.isBlank() ) throw new IllegalArgumentException("Name cannot be empty");
    if(gameModel.getPlayerList().size()<4) {
        gameModel.addPlayer(player);
        connectionHandler.addPlayerByID(name, clientID);
        PlayerNameMessage playerNameMessage = new PlayerNameMessage(player.getName(), true);
        connectionHandler.sendMessage(playerNameMessage, player.getName());
        NewPlayerMessage playerMessage = new NewPlayerMessage(gameModel.getPlayerList());
        connectionHandler.sendAllMessage(playerMessage);
    } else {
        throw new TooManyPlayersException("Too many players");
    }
    }
    @Override
    public void addSavedPlayer(String clientId, String name) throws AlreadyStartedException, IllegalArgumentException, PlayerNotFoundByNameException {
        if(gameState != GameState.LOAD_GAME_LOBBY) throw new AlreadyStartedException("Game already started");
        if(connectionHandler.isNameConnectedToId(name)) throw new IllegalArgumentException("Player has already connected");
        Player player = null;
        for (Player p : gameModel.getPlayerList()){
            if (p.getName().equals(name)){
                player = p;
            }
        }
        if(player == null) throw new PlayerNotFoundByNameException("Player not found");
        connectionHandler.addPlayerByID(name, clientId);
        PlayerNameMessage playerNameMessage = new PlayerNameMessage(name, true);
        connectionHandler.sendMessage(playerNameMessage, name);
        NewPlayerMessage playerMessage = new NewPlayerMessage(gameModel.getPlayerList());
        connectionHandler.sendAllMessage(playerMessage);
        boolean allSet = true;
        for (Player p : gameModel.getPlayerList()){
            if (!connectionHandler.isNameConnectedToId(p.getName())){
                allSet = false;
                break;
            }
        }
        if (allSet) {
            gameState = GameState.PLACE_CARD;
            List<AchievementCard> commonAchievementCards = new ArrayList<>();
            commonAchievementCards.add(gameModel.getAchievementDeck().getBoardCard().get(DeckPosition.FIRST_CARD));
            commonAchievementCards.add(gameModel.getAchievementDeck().getBoardCard().get(DeckPosition.SECOND_CARD));
            Deck<GoldCard> goldDeck = new Deck<>(
                    gameModel.getGoldDeck().getNumberOfCards(),
                    new ArrayList<>(List.of(gameModel.getGoldDeck().getTopCardNoPop(), gameModel.getGoldDeck().getBoardCard().get(DeckPosition.FIRST_CARD), gameModel.getGoldDeck().getBoardCard().get(DeckPosition.SECOND_CARD)))
            );
            Deck<ResourceCard> resourceDeck = new Deck<>(
                    gameModel.getResourceDeck().getNumberOfCards(),
                    new ArrayList<>(List.of(gameModel.getResourceDeck().getTopCardNoPop(), gameModel.getResourceDeck().getBoardCard().get(DeckPosition.FIRST_CARD), gameModel.getResourceDeck().getBoardCard().get(DeckPosition.SECOND_CARD)))
            );
            List<Client.Player> playerList = new ArrayList<>();
            System.out.println("Il giocatore attivo è il giocatore numero " + gameModel.getActivePlayerIndex());
            System.out.println("E' il turno di " + gameModel.getPlayerList().get(gameModel.getActivePlayerIndex()).getName());
            for (Player p : gameModel.getPlayerList()){
                playerList.add(new Client.Player(
                        p.getName(),
                        p.getPoints(),
                        p.getHand().size(),
                        gameModel.getActivePlayerIndex() == getPlayerList().indexOf(p),
                        p.getColor(),
                        p.getManuscript()
                ));
            }
            for(Player p : gameModel.getPlayerList()){
                SavedGameMessage message = new SavedGameMessage(
                        p.getName(),
                        commonAchievementCards,
                        goldDeck,
                        resourceDeck,
                        p.getSecretObjective(),
                        p.getHand(),
                        gameModel.getTurn(),
                        playerList,
                        gameModel.getChat(),
                        gameState
                );
                connectionHandler.sendMessage(message, p.getName());
            }
        }
    }
    @Override
    public void removePlayer(Player player) {
        System.out.println("Removing player object" + player.getName());
        gameModel.removePlayer(player);
        System.out.println("Removing player name");
        connectionHandler.removePlayerByName(player.getName());
        System.out.println("Player removed");
        switch(gameState){
            case CHOOSE_SECRET_ACHIEVEMENT:
                boolean allSet = getPlayerList().stream().allMatch(p -> p.getSecretObjective() != null);
                if(allSet){
                    shufflePlayerList();
                    nextTurn();
                }
                break;
            case CHOOSE_STARTING_CARD:
                boolean allset = getPlayerList().stream().allMatch(p -> p.getManuscript() != null);
                if(allset){
                    try {
                        giveInitialHand();
                    } catch (AlreadySetException | AlreadyFinishedException e) {
                        //do nothing as it's normal that it's already set
                    }
                }
                break;
            case PLACE_CARD, DRAW_CARD:
                if(getPlayerList().size() == 1){
                    try{
                        computeLeaderboard();
                    } catch (AlreadyFinishedException e) {
                        System.err.println("Error while computing leaderboard");
                    }
                }
                break;
            default:
                break;
        }
    }

    public void setOffline(String id){
        connectionHandler.setOffline(id);
        PlayerDisconnectedMessage message = new PlayerDisconnectedMessage(connectionHandler.getPlayerNameByID(id));
        reactToDisconnection(id);
        if(connectionHandler.isInDisconnectedList(id)){
            connectionHandler.sendAllMessage(message);
        }
    }

    public List<Player> getPlayerList() {
        return gameModel.getPlayerList();
    }

    public GameState getGameState() {
        return gameState;
    }

    /**
     * Shuffles the player list and sends the new order to all players
     */
    private void shufflePlayerList() {
        gameModel.shufflePlayerList();
        PlayerOrderMessage playerOrderMessage = new PlayerOrderMessage(gameModel.getPlayerList().stream().map(Player::getName).toList());
        connectionHandler.sendAllMessage(playerOrderMessage);
    }

    /**
     * Starts the game by creating the decks and giving the starting cards to the players
     * @throws TooFewElementsException if there are not enough players or not all players are ready
     * @throws AlreadySetException if the decks are already set
     */
    private void start() throws TooFewElementsException, AlreadySetException {
        if(gameModel.getPlayerList().size() < 2){
            throw new TooFewElementsException("Not enough players");
        }
        for(Player player : gameModel.getPlayerList()) {
            if(!player.isReady()){
                throw new TooFewElementsException("Not all players are ready");
            }
        }
        createDecks();
        giveStartingCards();
        StartGameMessage startGameMessage = new StartGameMessage(
                List.of(
                        gameModel.getGoldDeck().getTopCardNoPop(),
                        gameModel.getGoldDeck().getBoardCard().get(DeckPosition.FIRST_CARD),
                        gameModel.getGoldDeck().getBoardCard().get(DeckPosition.SECOND_CARD)
                ),
                List.of(
                        gameModel.getResourceDeck().getTopCardNoPop(),
                        gameModel.getResourceDeck().getBoardCard().get(DeckPosition.FIRST_CARD),
                        gameModel.getResourceDeck().getBoardCard().get(DeckPosition.SECOND_CARD)
                )
        );
        connectionHandler.sendAllMessage(startGameMessage);
    }

    /**
     * Creates the decks
     * @throws AlreadySetException if the decks are already set
     */
    private void createDecks() throws AlreadySetException {
        gameModel.createGoldResourceDecks();
    }

    public void setPlayerColor(Color color, Player player) throws IllegalArgumentException, AlreadyStartedException {
        if(gameModel.getTurn() > 0){
            throw new AlreadyStartedException("Game already started");
        }
        List<Color> colors = getPlayerList().stream().map(Player::getColor).toList();
        if(!colors.contains(color)){
            player.setColor(color);
            PlayerColorMessage message = new PlayerColorMessage(true, player.getName(), color);
            connectionHandler.sendAllMessage(message);
        } else {
            throw new IllegalArgumentException("Color not available");
        }

    }

    /**
     * Gives the secret objective cards to the players
     * @throws AlreadySetException if the secret objective cards are already set
     */
    private void giveSecretObjectiveCards() throws AlreadySetException {
        gameState = GameState.CHOOSE_SECRET_ACHIEVEMENT;
        gameModel.createAchievementDeck();
        getPlayerList().forEach(player -> {
            givenSecretObjectiveCards.put(player, new ArrayList<>());
            List<AchievementCard> secretObjectiveCardsToPlayer = givenSecretObjectiveCards.get(player);
            try {
                AchievementCard card1 = gameModel.getAchievementDeck().popCard(DeckPosition.DECK);
                AchievementCard card2 = gameModel.getAchievementDeck().popCard(DeckPosition.DECK);
                secretObjectiveCardsToPlayer.add(card1);
                secretObjectiveCardsToPlayer.add(card2);
                List<AchievementCard> commonAchievements = new ArrayList<>();
                commonAchievements.add(gameModel.getAchievementDeck().popCard(DeckPosition.FIRST_CARD));
                commonAchievements.add(gameModel.getAchievementDeck().popCard(DeckPosition.SECOND_CARD));
                AchievementCardsMessage achievementCardsMessage = new AchievementCardsMessage(secretObjectiveCardsToPlayer, commonAchievements);
                connectionHandler.sendMessage(achievementCardsMessage, player.getName());
            } catch (AlreadyFinishedException e) {
                throw new RuntimeException(e);
            }

        });
    }

    public void setSecretObjectiveCard(Player player, int cardNumber) throws AlreadySetException, MissingInfoException, AlreadyStartedException {
        if(givenSecretObjectiveCards.get(player) == null || givenSecretObjectiveCards == null){
            throw new MissingInfoException("Secret Objective not given");
        }
        if(gameModel.getTurn() > 1){
            throw new AlreadyStartedException("Game already started");
        }
        AchievementCard card = givenSecretObjectiveCards.get(player).get(cardNumber);
        player.setSecretObjective(card);
        SetSecretCardMessage setSecretCardMessage = new SetSecretCardMessage(player.getName());
        connectionHandler.sendAllMessage(setSecretCardMessage);
        boolean allSet = getPlayerList().stream().allMatch(p -> p.getSecretObjective() != null);
        if(allSet){
            shufflePlayerList();
            nextTurn();
        }
    }

    /**
     * Gives the starting cards to the players
     * @throws AlreadySetException if the starting cards are already set
     */
    private void giveStartingCards() throws AlreadySetException {
        gameState = GameState.CHOOSE_STARTING_CARD;
        gameModel.createStartingCards();
        List<StartingCard> startingCards = gameModel.getStartingCards();
        getPlayerList().forEach(player -> {
            StartingCard card = startingCards.removeFirst();
            givenStartingCards.put(player, card);
            StartingCardsMessage startingCardsMessage = new StartingCardsMessage(card);
            connectionHandler.sendMessage(startingCardsMessage, player.getName());
        });
    }
    public void setStartingCard(Player player, Face face) throws AlreadySetException, AlreadyStartedException, MissingInfoException {
        if(gameModel.getTurn() > 1){
            throw new AlreadyStartedException("Game already started");
        }
        if(givenStartingCards.get(player) == null || givenStartingCards == null){
            throw new MissingInfoException("Starting Card not given");
        }
        Card card = givenStartingCards.get(player);
        player.initializeManuscript(card, face);
        SetStartingCardMessage setStartingCardMessage = new SetStartingCardMessage(player.getName(), card.getCornerFace(face));
        connectionHandler.sendAllMessage(setStartingCardMessage);
        boolean allSet = getPlayerList().stream().allMatch(p -> p.getManuscript() != null);
        if(allSet){
            try {
                giveInitialHand();
                StartGameMessage startGameMessage = new StartGameMessage(
                        List.of(
                                gameModel.getGoldDeck().getTopCardNoPop(),
                                gameModel.getGoldDeck().getBoardCard().get(DeckPosition.FIRST_CARD),
                                gameModel.getGoldDeck().getBoardCard().get(DeckPosition.SECOND_CARD)
                        ),
                        List.of(
                                gameModel.getResourceDeck().getTopCardNoPop(),
                                gameModel.getResourceDeck().getBoardCard().get(DeckPosition.FIRST_CARD),
                                gameModel.getResourceDeck().getBoardCard().get(DeckPosition.SECOND_CARD)
                        )
                );
                connectionHandler.sendAllMessage(startGameMessage);

            } catch (AlreadySetException | AlreadyFinishedException e) {
                //do nothing as it's normal that it's already set
            }
        }
    }

    /**
     * Gives the initial hand to the players
     * @throws AlreadySetException if the initial hand is already given
     * @throws AlreadyFinishedException if the deck is empty
     */
    private void giveInitialHand() throws AlreadySetException, AlreadyFinishedException{
        for(Player player : getPlayerList()){
            Card card1 = gameModel.getResourceDeck().popCard(DeckPosition.DECK);
            Card card2 = gameModel.getResourceDeck().popCard(DeckPosition.DECK);
            Card card3 = gameModel.getGoldDeck().popCard(DeckPosition.DECK);
            try{
                player.addCardToHand(card1);
                player.addCardToHand(card2);
                player.addCardToHand(card3);
                InitialHandMessage initialHandMessage = new InitialHandMessage(List.of(card1, card2, card3));
                connectionHandler.sendMessage(initialHandMessage, player.getName());
                OtherPlayerInitialHandMessage otherPlayerInitialHandMessage = new OtherPlayerInitialHandMessage(player.getName());
                connectionHandler.sendAllMessage(otherPlayerInitialHandMessage);
            } catch (TooManyElementsException e) {
                throw new AlreadySetException("Initial Hand already given");
            }

        }
        //si potrebbero accorpare i messaggi della mano con quelli degli obiettivi segreti in un unico messaggio

        //Dopo aver dato la mano iniziale si prosegue con la seconda parte dell'inizializzazione: si scoprono gli obiettivi segreti e non
        giveSecretObjectiveCards();

    }

    /**
     * Sets the next turn. Sets the end game phase or ends the game if necessary
     */
    private void nextTurn(){
        if(gameModel.getActivePlayerIndex() == -1){ //sets active player as the first player. If it's not the first turn this is not valid
            if(gameModel.getTurn() != 0){
                System.out.println("Active player not set");
                return;
            }
        }
        //if it is end game and all players have played also an extra round, calculate leaderboard
        if(gameModel.isEndGamePhase() && gameModel.getActivePlayerIndex() == getPlayerList().size() - 1 ){
            if(gameModel.isLastRound()){
                try{
                    computeLeaderboard();
                    return;
                } catch (AlreadyFinishedException e) {
                    System.err.println("Error while computing leaderboard");
                }
            } else {
                gameModel.setLastRound(true);
            }
        } else {
            //sets end game if necessary
            if (gameModel.getActivePlayerIndex() != -1 && getPlayerList().get(gameModel.getActivePlayerIndex()).getPoints() >= 20) {
                try {
                    endGame();
                } catch (AlreadySetException e) {
                    //do nothing as it's normal that it's already set
                }
            }
        }
        //sets new active player
        do {
            if (gameModel.getActivePlayerIndex() == gameModel.getPlayerList().size() - 1) {
                gameModel.setActivePlayerIndex(0);
            } else {
                gameModel.setActivePlayerIndex(gameModel.getActivePlayerIndex() + 1);
            }
            gameState = GameState.PLACE_CARD;
        } while (!isOnline(getPlayerList().get(gameModel.getActivePlayerIndex())));//if the player is not online skips to the next one
        gameModel.nextTurn();
        saveGame();
        NewTurnMessage newTurnMessage = new NewTurnMessage(getPlayerList().get(gameModel.getActivePlayerIndex()).getName(), gameModel.getTurn());
        connectionHandler.sendAllMessage(newTurnMessage);
    }
    public int getTurn() {
        return gameModel.getTurn();
    }

    public boolean isOnline(Player player) {
        String id = connectionHandler.getIdByName(player.getName());
        return !getConnectionHandler().isInDisconnectedList(id);
    }

    public void playCard(Player player, int position, int xCoord, int yCoord, Face face) throws TooFewElementsException, InvalidMoveException {
        //if it's not the player's turn, throw exception
        if(getPlayerList().indexOf(player) != gameModel.getActivePlayerIndex()){
            System.out.println("Not player's turn");
            throw new InvalidMoveException("Not player's turn");
        }
        CornerCardFace cardFace = player.getHand().get(position).getCornerFace(face);
        if(!player.getManuscript().isPlaceable(xCoord, yCoord, cardFace)) {
            System.out.println("Card not placeable because of position or requirements");
            throw new InvalidMoveException("Card not placeable. Check the position and the placement requirements");
        }
        if(!gameState.equals(GameState.PLACE_CARD)){
            System.out.println("Player has already played");
            throw new TooFewElementsException("Already played");
        }
        player.removeCardFromHand(position);
        int cardPoints = 0;
        int obtainedPoints = 0;
        try {
            cardPoints = cardFace.getScore();
        } catch (UnsupportedOperationException e) {
            if (Objects.equals(e.getMessage(), "Regular cards do not have scores")) {
                cardPoints = 0;
            }
        }
        Map<Symbol, Integer> scoreRequirements = null;
        try {
            scoreRequirements = cardFace.getScoreRequirements();
        } catch (UnsupportedOperationException e) {
            // Already handled by initializing scoreRequirements to null
        }
        if (scoreRequirements != null) {
            Symbol requiredSymbol = (Symbol) scoreRequirements.keySet().toArray()[0];
            int i = 0;
            for (Symbol symbol : scoreRequirements.keySet()) {
                if (scoreRequirements.get(symbol) != 0) {
                    requiredSymbol = (Symbol) scoreRequirements.keySet().toArray()[i];
                }
                i++;
            }
            int requiredQuantity = scoreRequirements.get(requiredSymbol);
            int actualQuantity;
            if (requiredSymbol == Symbol.COVERED_CORNER) {
                actualQuantity = player.getManuscript().getNeighbors(xCoord, yCoord).size();
            } else {
                actualQuantity = player.getManuscript().getSymbolCount(requiredSymbol);
                Symbol finalRequiredSymbol = requiredSymbol;
                int quantityOnCard = cardFace.getCornerSymbols().entrySet().stream()
                        .filter(entry -> entry.getValue() == finalRequiredSymbol).toList().size();
                actualQuantity += quantityOnCard;
            }
            //System.out.println("RequiredSymbols " + requiredSymbol + " ScoreRequirements" + scoreRequirements);
            if(requiredQuantity != 0){
                obtainedPoints = actualQuantity / requiredQuantity * cardPoints;
                player.addPoints(obtainedPoints);
            } else {
                player.addPoints(cardPoints);
                obtainedPoints = cardPoints;
            }
        } else {
            player.addPoints(cardPoints);
            obtainedPoints = cardPoints;
        }
        player.getManuscript().addCard(xCoord, yCoord, cardFace, getTurn());
        OtherPlayerPlayCardMessage otherPlayerPlayCardMessage = new OtherPlayerPlayCardMessage(
                player.getName(),
                cardFace,
                xCoord,
                yCoord,
                obtainedPoints
        );
        gameState = GameState.DRAW_CARD;
        connectionHandler.sendAllMessage(otherPlayerPlayCardMessage);
    }
    public void drawCard(Player player, DeckPosition deckPosition, Decks deck) throws TooManyElementsException, InvalidMoveException, AlreadyFinishedException, NotYetStartedException {
        if(gameModel.getTurn() < 1) {
            throw new NotYetStartedException("Game not started");
        }
        if(getPlayerList().indexOf(player) != gameModel.getActivePlayerIndex()){
            throw new InvalidMoveException("Not player's turn");
        }
        if(!gameState.equals(GameState.DRAW_CARD)){
            throw new TooManyElementsException("Play a card first");
        }
        Card drawnCard;
        switch (deck) {
            case RESOURCE -> {
                drawnCard = gameModel.getResourceDeck().popCard(deckPosition);
                player.addCardToHand(drawnCard);

            }
            case GOLD -> {
                drawnCard = gameModel.getGoldDeck().popCard(deckPosition);
                player.addCardToHand(drawnCard);
            }
            default -> throw new IllegalArgumentException("Invalid deck");
        }
        DrawCardMessage drawCardMessage = new DrawCardMessage(drawnCard);
        connectionHandler.sendMessage(drawCardMessage, player.getName());
        if(gameModel.getResourceDeck().isEmpty() && gameModel.getGoldDeck().isEmpty()){
            try {
                endGame();
            } catch (AlreadySetException e) {
                //do nothing as it's normal that it's already set
            }
        }
        OtherPlayerDrawCardMessage otherPlayerDrawCardMessage = new OtherPlayerDrawCardMessage(
                player.getName(),
                deck,
                deckPosition,
                switch (deck) {
                    //todo: aggiungere un metodo ai deck che ritorni le boardcards in una lista per evitare sto macello
                    case RESOURCE -> List.of(gameModel.getResourceDeck().getBoardCard().get(DeckPosition.DECK),  gameModel.getResourceDeck().getBoardCard().get(DeckPosition.FIRST_CARD), gameModel.getResourceDeck().getBoardCard().get(DeckPosition.SECOND_CARD));
                    case GOLD -> List.of(gameModel.getGoldDeck().getBoardCard().get(DeckPosition.DECK), gameModel.getGoldDeck().getBoardCard().get(DeckPosition.FIRST_CARD), gameModel.getGoldDeck().getBoardCard().get(DeckPosition.SECOND_CARD));
                    default -> null;
                }
        );
        connectionHandler.sendAllMessage(otherPlayerDrawCardMessage);
        nextTurn();
    }

    /**
     * Starts the end game phase
     * @throws AlreadySetException if the end game phase is already set
     */
    private void endGame() throws AlreadySetException {
        gameModel.setEndGamePhase();
        //if it's the last player to play, the extra round starts immediately
        if(gameModel.getActivePlayerIndex() == getPlayerList().size() - 1){
            gameModel.setLastRound(true);
        }
        EndGamePhaseMessage endGamePhaseMessage = new EndGamePhaseMessage();
        connectionHandler.sendAllMessage(endGamePhaseMessage);
    }

    /**
     * Computes the leaderboard and sends it to all players
     */
    private void computeLeaderboard(){
        gameState = GameState.LEADERBOARD;
        AchievementDeck achievementDeck = gameModel.getAchievementDeck();
        try {
            AchievementCard commonAchievement1 = achievementDeck.popCard(DeckPosition.FIRST_CARD);
            AchievementCard commonAchievement2 = achievementDeck.popCard(DeckPosition.SECOND_CARD);
            List<Player> playerlist = getPlayerList();
            Map<String, Integer> playerCardPoints = new HashMap<>();
            Map<String, Integer> playerAchievementPoints = new HashMap<>();
            for (Player player : playerlist) {
                int cardPoints = player.getPoints();
                playerCardPoints.put(player.getName(), cardPoints);
                Manuscript manuscript = player.getManuscript();
                int achievementPoints = manuscript.calculatePoints(commonAchievement1);
                achievementPoints += manuscript.calculatePoints(commonAchievement2);
                achievementPoints += manuscript.calculatePoints(player.getSecretObjective());
                playerAchievementPoints.put(player.getName(), achievementPoints);
            }
            LinkedHashMap<String, Integer> leaderboardMap = new LinkedHashMap<>();//linkedhashmap keeps order of insertion which will be the player order
            for (int i = 0; i < playerlist.size(); i++) {
                List<String> playersWithMaxPoints = new ArrayList<>();
                playerCardPoints.forEach((player, points) -> {
                    if (playersWithMaxPoints.isEmpty()) {
                        playersWithMaxPoints.add(player);
                    } else if (Objects.equals(points, Collections.max(playerCardPoints.values())) ){
                        playersWithMaxPoints.add(player);
                    } else if (points > Collections.max(playerCardPoints.values())) {
                        playersWithMaxPoints.clear();
                        playersWithMaxPoints.add(player);
                    }
                });
                String firstPlayer = playerAchievementPoints.entrySet().stream()
                        .filter(entry -> playersWithMaxPoints.contains(entry.getKey()))
                        .max(Map.Entry.comparingByValue()).get().getKey();
                leaderboardMap.put(firstPlayer, playerCardPoints.get(firstPlayer) + playerAchievementPoints.get(firstPlayer));
                playerCardPoints.remove(firstPlayer);
                playerAchievementPoints.remove(firstPlayer);
            }
            LeaderboardMessage leaderboardMessage = new LeaderboardMessage(leaderboardMap);
            connectionHandler.sendAllMessage(leaderboardMessage);
            clear();
        } catch (AlreadyFinishedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Clears the game model and prepares for a new game
     */
    private void clear() {
        gameModel = new GameModelInstance();
        givenStartingCards = new HashMap<>();
        givenSecretObjectiveCards = new HashMap<>();
        gameState = GameState.LOBBY;
    }

    public void setReady(Player player) throws MissingInfoException, AlreadyStartedException {
        if(player.getColor() == null) {
            throw new MissingInfoException("Color not set");
        }
        if(gameModel.getTurn() > 0){
            throw new AlreadyStartedException("Game already started");
        }
        player.setReady(true);
        ReadyStatusMessage readyStatusMessage = new ReadyStatusMessage(true, player.getName());
        connectionHandler.sendAllMessage(readyStatusMessage);
        if (getPlayerList().stream().allMatch(Player::isReady) && getPlayerList().size() > 1){
            try {
                start();
            } catch (TooFewElementsException | AlreadySetException e) {
                //do nothing as it's normal that it's already set
            }
        }
    }

    public void setNotReady(Player player) throws MissingInfoException, AlreadyStartedException{
        if(player.getColor() == null) {
            throw new MissingInfoException("Color not set");
        }
        if(gameModel.getTurn() > 0){
            throw new AlreadyStartedException("Game already started");
        }
        player.setReady(false);
        ReadyStatusMessage readyStatusMessage = new ReadyStatusMessage(false, player.getName());
        connectionHandler.sendAllMessage(readyStatusMessage);
    }

    public boolean isReady(Player player) {
        return player.isReady();
    }

    public void addMessage(String message, Player sender) throws IllegalArgumentException{
        gameModel.getChat().addMessage(message, sender);
        ChatMessage chatMessage = new ChatMessage(message, sender.getName());
        connectionHandler.sendAllMessage(chatMessage);
    }

    public List<Message> getChatMessages() {
        List<Message> messages = gameModel.getChat().getMessages();
        //Notify
        return messages;
    }

    public void saveGame() {
        try {
            Gson gson = new Gson();
            File file = new File(getClass().getResource("/saves/game.json").toURI());
            FileWriter fileWriter = new FileWriter(file);
            gson.toJson(gameModel, fileWriter);
            fileWriter.close();
        } catch (Exception e) {
            System.err.println("Error while saving game");
        }
    }

    public void loadGame() {
        try {
            Gson gson = new Gson();
            File file = new File(getClass().getResource("/saves/game.json").toURI());
            FileReader fileReader = new FileReader(file);
            gameModel = gson.fromJson(fileReader, GameModelInstance.class);
            fileReader.close();
        } catch (Exception e) {
            System.err.println("Error while loading game");
        }
    }

    @Override
    public Player getPlayerByName(String name) throws PlayerNotFoundByNameException {
        for (Player p : this.gameModel.getPlayerList()){
            if (p.getName().equals(name)) return p;
        }
        throw new PlayerNotFoundByNameException("Player not found");
    }

    public GeneralServerConnectionHandler getConnectionHandler() {
        return this.connectionHandler;
    }


    @Override
    public void printData() {
        System.out.println("----------");
        System.out.println("Players:");
        this.gameModel.getPlayerList().forEach(p -> System.out.print(p.getName() + " " + p.getColor() + " " + p.isReady() +  ", "));
        System.out.println("\n");
    }

    public void reactToDisconnection(String id){
        String playerName;
        try {
            if(getPlayerByName(connectionHandler.getPlayerNameByID(id)) == null){ //this means the client is not a player
                try {
                    connectionHandler.getServerConnectionHandler(id).killClient(id);
                } catch (PlayerNotInAnyServerConnectionHandlerException e) {
                    System.err.println("Player not found in any server connection handler, PLAYER NOT REMOVED!");
                } catch (Exception e){
                    System.err.println("Exception, PLAYER NOT REMOVED!");
                }
                return;
            }
        } catch (PlayerNotFoundByNameException e) {
            try {
                connectionHandler.getServerConnectionHandler(id).killClient(id);
            } catch (PlayerNotInAnyServerConnectionHandlerException e2) {
                System.err.println("Player not found in any server connection handler, PLAYER NOT REMOVED!");
            } catch (Exception e1){
                System.err.println("Exception, PLAYER NOT REMOVED!");
            }
            return;
        }
        playerName = connectionHandler.getPlayerNameByID(id);
        switch(gameState){
            case LOAD_GAME_LOBBY:
                connectionHandler.removePlayerByName(playerName);
                break;
            case LOBBY: //if lobby, just remove the player
                try {
                    removePlayer(getPlayerByName(playerName));
                } catch (PlayerNotFoundByNameException e) {
                    throw new RuntimeException(e);
                }
                if (getPlayerList().stream().allMatch(Player::isReady) && getPlayerList().size() > 1){
                    try {
                        start();
                    } catch (TooFewElementsException | AlreadySetException e) {
                        //do nothing as it's normal that it's already set
                    }
                }
                break;
            case LEADERBOARD: //if leaderboard, do nothing as the game has already ended
                break;
            default: //here we could be at secret or starting cardd choice or in game: we wait a minute to see if the player reconnects and after that we remove the player
                switch (gameState) {
                    case CHOOSE_SECRET_ACHIEVEMENT, CHOOSE_STARTING_CARD:
                        new DisconnectionTimer(this, connectionHandler, id, 60);
                        break;
                    case PLACE_CARD, DRAW_CARD:
                        String activePlayerName = getPlayerList().get(gameModel.getActivePlayerIndex()).getName();
                        if(activePlayerName.equals(playerName)){
                            if(gameState.equals(GameState.DRAW_CARD)){
                                if(!gameModel.getResourceDeck().isEmpty()){
                                    if(gameModel.getResourceDeck().getNumberOfCards() >= 1){
                                        try {
                                            drawCard(getPlayerByName(playerName), DeckPosition.DECK, Decks.RESOURCE);
                                        } catch (TooManyElementsException | InvalidMoveException | AlreadyFinishedException | NotYetStartedException e) {
                                            System.err.println("Error while drawing card for offline player");
                                        } catch (PlayerNotFoundByNameException e) {
                                            throw new RuntimeException(e);
                                        }
                                    } else if (gameModel.getResourceDeck().getBoardCard().get(DeckPosition.FIRST_CARD) != null){
                                        try {
                                            drawCard(getPlayerByName(playerName), DeckPosition.FIRST_CARD, Decks.RESOURCE);
                                        } catch (TooManyElementsException | InvalidMoveException | AlreadyFinishedException | NotYetStartedException e) {
                                            System.err.println("Error while drawing card for offline player");
                                        } catch (PlayerNotFoundByNameException e) {
                                            throw new RuntimeException(e);
                                        }
                                    } else{
                                        try {
                                            drawCard(getPlayerByName(playerName), DeckPosition.SECOND_CARD, Decks.RESOURCE);
                                        } catch (TooManyElementsException | InvalidMoveException |
                                                 AlreadyFinishedException | NotYetStartedException |
                                                 PlayerNotFoundByNameException e) {
                                            System.err.println("Error while drawing card for offline player");
                                        }
                                    }
                                } else if(!gameModel.getGoldDeck().isEmpty()){
                                    if(gameModel.getGoldDeck().getNumberOfCards() >= 1){
                                        try {
                                            drawCard(getPlayerByName(playerName), DeckPosition.DECK, Decks.GOLD);
                                        } catch (TooManyElementsException | InvalidMoveException |
                                                 AlreadyFinishedException | NotYetStartedException |
                                                 PlayerNotFoundByNameException e) {
                                            System.err.println("Error while drawing card for offline player");
                                        }
                                    } else if (gameModel.getGoldDeck().getBoardCard().get(DeckPosition.FIRST_CARD) != null){
                                        try {
                                            drawCard(getPlayerByName(playerName), DeckPosition.FIRST_CARD, Decks.GOLD);
                                        } catch (TooManyElementsException | InvalidMoveException |
                                                 AlreadyFinishedException | NotYetStartedException |
                                                 PlayerNotFoundByNameException e) {
                                            System.err.println("Error while drawing card for offline player");
                                        }
                                    } else{
                                        try {
                                            drawCard(getPlayerByName(playerName), DeckPosition.SECOND_CARD, Decks.GOLD);
                                        } catch (TooManyElementsException | InvalidMoveException |
                                                 AlreadyFinishedException | NotYetStartedException |
                                                 PlayerNotFoundByNameException e) {
                                            System.err.println("Error while drawing card for offline player");
                                        }
                                    }
                                } else {
                                    nextTurn();
                                }
                            }
                        }
                        new DisconnectionTimer(this, connectionHandler, id, 180);
                        break;
        }


    }}
        public void reconnect(String oldId, String newId) throws IllegalArgumentException, AlreadySetException, NotYetStartedException, AlreadyFinishedException {
        System.out.println("Reconnecting player" + oldId + " with new id (id of previous player) " + newId);
        System.out.println(connectionHandler.getDisconnectedList());
        if(!connectionHandler.isInDisconnectedList(newId)){
            throw new AlreadySetException("Player not disconnected");
        }
        String playerName = connectionHandler.getPlayerNameByID(newId);
        Player player;
        try {
            player = getPlayerByName(playerName);
        }
        catch (PlayerNotFoundByNameException e)
        {
            throw new IllegalArgumentException("Player not found");
        }

        switch (gameState){
            case LOBBY, LOAD_GAME_LOBBY:
                throw new NotYetStartedException ("Game not started");
            case LEADERBOARD:
                throw  new AlreadyFinishedException("Game already finished");
            case CHOOSE_STARTING_CARD:
                System.out.println("Reconnecting player in starting card choice");
                connectionHandler.setOnline(newId);
                connectionHandler.changePlayerId(playerName, oldId);
                System.out.println("Player " + playerName + " has now id " + oldId + "=" + connectionHandler.getIdByName(playerName));
                ReconnectionNameMessage reconnectionNameMessage = new ReconnectionNameMessage(playerName);
                connectionHandler.sendMessage(reconnectionNameMessage, playerName);
                StartingCardsMessage startingCardsMessage = new StartingCardsMessage(givenStartingCards.get(player));
                connectionHandler.sendMessage(startingCardsMessage, playerName);
                StartGameMessage startGameMessage = new StartGameMessage(
                        List.of(
                                gameModel.getGoldDeck().getTopCardNoPop(),
                                gameModel.getGoldDeck().getBoardCard().get(DeckPosition.FIRST_CARD),
                                gameModel.getGoldDeck().getBoardCard().get(DeckPosition.SECOND_CARD)
                        ),
                        List.of(
                                gameModel.getResourceDeck().getTopCardNoPop(),
                                gameModel.getResourceDeck().getBoardCard().get(DeckPosition.FIRST_CARD),
                                gameModel.getResourceDeck().getBoardCard().get(DeckPosition.SECOND_CARD)
                        )
                );
                connectionHandler.sendMessage(startGameMessage, playerName);
                for(Player p : getPlayerList()){
                    if(p.getManuscript() != null){
                        SetStartingCardMessage setStartingCardMessage = new SetStartingCardMessage(p.getName(), p.getManuscript().getCardByCoord(0, 0));
                        connectionHandler.sendMessage(setStartingCardMessage, playerName);
                    }
                }
                break;
            case CHOOSE_SECRET_ACHIEVEMENT:
                connectionHandler.setOnline(newId);
                connectionHandler.changePlayerId(playerName, oldId);
                reconnectionNameMessage = new ReconnectionNameMessage(playerName);
                connectionHandler.sendMessage(reconnectionNameMessage, playerName);
                StartGameMessage startMessage = new StartGameMessage(
                        List.of(
                                gameModel.getGoldDeck().getTopCardNoPop(),
                                gameModel.getGoldDeck().getBoardCard().get(DeckPosition.FIRST_CARD),
                                gameModel.getGoldDeck().getBoardCard().get(DeckPosition.SECOND_CARD)
                        ),
                        List.of(
                                gameModel.getResourceDeck().getTopCardNoPop(),
                                gameModel.getResourceDeck().getBoardCard().get(DeckPosition.FIRST_CARD),
                                gameModel.getResourceDeck().getBoardCard().get(DeckPosition.SECOND_CARD)
                        )
                );
                connectionHandler.sendMessage(startMessage, playerName);
                for(Player p: getPlayerList()){
                    SetStartingCardMessage startingMessage = new SetStartingCardMessage(p.getName(), p.getManuscript().getCardByCoord(0, 0));
                    connectionHandler.sendMessage(startingMessage, playerName);
                }
                InitialHandMessage initialHandMessage = new InitialHandMessage(player.getHand());
                connectionHandler.sendMessage(initialHandMessage, playerName);
                for(Player p: getPlayerList()){
                    OtherPlayerInitialHandMessage otherPlayerInitialHandMessage = new OtherPlayerInitialHandMessage(p.getName());
                    connectionHandler.sendMessage(otherPlayerInitialHandMessage, playerName);
                }
                List<AchievementCard> secretObjectiveCards = givenSecretObjectiveCards.get(player);
                List<AchievementCard> commonAchievements = new ArrayList<>();
                commonAchievements.add(gameModel.getAchievementDeck().getBoardCard().get(DeckPosition.FIRST_CARD));
                commonAchievements.add(gameModel.getAchievementDeck().getBoardCard().get(DeckPosition.SECOND_CARD));
                AchievementCardsMessage achievementCardsMessage = new AchievementCardsMessage(secretObjectiveCards, commonAchievements);
                connectionHandler.sendMessage(achievementCardsMessage, playerName);
                for(Player p: getPlayerList()){
                    if(p.getSecretObjective() != null) {
                        SetSecretCardMessage setSecretCardMessage;
                        if(Objects.equals(p.getName(), playerName)){
                            int chosenCardIndex = givenSecretObjectiveCards.get(p).indexOf(p.getSecretObjective());
                            setSecretCardMessage = new SetSecretCardMessage(chosenCardIndex, p.getName());
                        } else {
                            setSecretCardMessage = new SetSecretCardMessage(p.getName());
                        }
                        connectionHandler.sendMessage(setSecretCardMessage, playerName);
                    }
                }
                break;
            default:
                System.out.println("Reconnecting player in game");
                connectionHandler.setOnline(newId);
                connectionHandler.changePlayerId(playerName, oldId);
                List<AchievementCard> commonAchievementCards = new ArrayList<>();
                commonAchievementCards.add(gameModel.getAchievementDeck().getBoardCard().get(DeckPosition.FIRST_CARD));
                commonAchievementCards.add(gameModel.getAchievementDeck().getBoardCard().get(DeckPosition.SECOND_CARD));
                Deck<GoldCard> goldDeck = new Deck<>(
                        gameModel.getGoldDeck().getNumberOfCards(),
                        new ArrayList<>(List.of(gameModel.getGoldDeck().getTopCardNoPop(), gameModel.getGoldDeck().getBoardCard().get(DeckPosition.FIRST_CARD), gameModel.getGoldDeck().getBoardCard().get(DeckPosition.SECOND_CARD)))
                );
                Deck<ResourceCard> resourceDeck = new Deck<>(
                        gameModel.getResourceDeck().getNumberOfCards(),
                        new ArrayList<>(List.of(gameModel.getResourceDeck().getTopCardNoPop(), gameModel.getResourceDeck().getBoardCard().get(DeckPosition.FIRST_CARD), gameModel.getResourceDeck().getBoardCard().get(DeckPosition.SECOND_CARD)))
                );
                List<Client.Player> playerList = new ArrayList<>();
                for (Player p : gameModel.getPlayerList()){
                    playerList.add(new Client.Player(
                            p.getName(),
                            p.getPoints(),
                            p.getHand().size(),
                            gameModel.getActivePlayerIndex() == getPlayerList().indexOf(p),
                            p.getColor(),
                            p.getManuscript()
                    ));
                }
                ReconnectionMessage reconnectionMessage = new ReconnectionMessage(
                        newId,
                        commonAchievementCards,
                        goldDeck,
                        resourceDeck,
                        playerName,
                        player.getSecretObjective(),
                        player.getHand(),
                        gameModel.getTurn(),
                        playerList,
                        gameModel.getChat(),
                        gameState
                );
                connectionHandler.sendMessage(reconnectionMessage, playerName);
                break;
        }
        OtherPlayerReconnectionMessage message = new OtherPlayerReconnectionMessage(playerName);
        connectionHandler.sendAllMessage(message);
    }
}




