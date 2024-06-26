package Server.Controller;

import Server.Card.AchievementCard;
import Server.Card.Card;
import Server.Card.CornerCardFace;
import Server.Card.StartingCard;
import Server.Connections.GeneralServerConnectionHandler;
import Server.Deck.AchievementDeck;
import Server.Enums.Color;
import Server.Enums.DeckPosition;
import Server.Enums.Decks;
import Server.Enums.Face;
import Server.Exception.*;
import Server.GameModel.GameModel;
import Server.GameModel.GameModelInstance;
import Server.Manuscript.Manuscript;
import Server.Messages.*;
import Server.Player.Player;
import com.google.gson.Gson;

import java.io.*;
import java.util.*;

/**
 * This is the implementation of the controller interface. It manages the game flow and the game model.
 */
public class ControllerInstance implements Controller{
    private GameModel gameModel;
    private final GeneralServerConnectionHandler connectionHandler;
    private Map<Player, StartingCard> givenStartingCards = new HashMap<>();
    private Map<Player, List<AchievementCard>> givenSecretObjectiveCards = new HashMap<>();
    private ServerState gameState;

    /**
     * Asks whether the user wants to start a new game or load a saved game and initializes the game model
     * @param connectionHandler the connection handler
     */
    public ControllerInstance(GeneralServerConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
        this.gameModel = new GameModelInstance();
        changeState(new LobbyState(this.gameModel, this.connectionHandler, this));
        Scanner inputReader = new Scanner(System.in);
        System.out.println("Vuoi iniziare una nuova partita (n) o caricare una partita salvata (l)?");
        String input = inputReader.nextLine();
        if(input.equals("l")){
            try {
                loadGame();
                System.out.println("Partita caricata. I giocatori sono:");
                for (Player p : gameModel.getPlayerList()) {
                    System.out.println(p.getName());
                }
            } catch (NullPointerException e) {
                System.out.println("Non esiste nessuna partita salvata. Inizio nuova partita.");
            }
        }
    }
    public void changeState(ServerState state){
        gameState = state;
    }

    @Override
    public void addPlayer(String name, String clientID) throws TooManyPlayersException, AlreadyStartedException, IllegalArgumentException {
    if(connectionHandler.isIdConnectedToName(clientID)) throw new IllegalArgumentException("Player already connected");
    gameState.addPlayer(name, clientID);
    }
    @Override
    public void addSavedPlayer(String clientId, String name) throws AlreadyStartedException, IllegalArgumentException, PlayerNotFoundByNameException {
        gameState.addSavedPlayer(clientId, name);
    }
    @Override
    public void removePlayer(Player player) {
        System.out.println("Removing player object" + player.getName());
        gameModel.removePlayer(player);
        System.out.println("Removing player name");
        connectionHandler.removePlayerByName(player.getName());
        System.out.println("Player removed");
        gameState.removePlayer(player);
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


    public void shufflePlayerList() {
        gameModel.shufflePlayerList();
        PlayerOrderMessage playerOrderMessage = new PlayerOrderMessage(gameModel.getPlayerList().stream().map(Player::getName).toList());
        connectionHandler.sendAllMessage(playerOrderMessage);
    }


    public void start() throws TooFewElementsException, AlreadySetException {
        if(gameModel.getPlayerList().size() < 2){
            throw new TooFewElementsException("Not enough players");
        }
        for(Player player : gameModel.getPlayerList()) {
            if(!player.isReady()){
                throw new TooFewElementsException("Not all players are ready");
            }
        }
        createDecks();
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
        giveStartingCards();
    }

    @Override
    public boolean isInSavedGameLobby() {
        return gameState.isInSavedGameLobby();
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
        changeState(new ChooseSecretAchievementState(this, gameModel));
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
        changeState(new ChooseStartingCardState(this, gameModel));
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
            } catch (AlreadySetException | AlreadyFinishedException e) {
                //do nothing as it's normal that it's already set
            }
        }
    }


    public void giveInitialHand() throws AlreadySetException, AlreadyFinishedException{
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


    public void nextTurn(){
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
            changeState(new PlaceCardState(this, gameModel));
        } while (!isOnline(getPlayerList().get(gameModel.getActivePlayerIndex())));//if the player is not online skips to the next one
        gameModel.nextTurn();
        if(!gameModel.isLastRound()) {
            saveGame();
        }
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
        gameState.playCard(player, position, xCoord, yCoord, cardFace);
    }
    public void drawCard(Player player, DeckPosition deckPosition, Decks deck) throws TooManyElementsException, InvalidMoveException, AlreadyFinishedException, NotYetStartedException {
        if(gameModel.getTurn() < 1) {
            throw new NotYetStartedException("Game not started");
        }
        if(getPlayerList().indexOf(player) != gameModel.getActivePlayerIndex()){
            throw new InvalidMoveException("Not player's turn");
        }
        gameState.drawCard(player, deckPosition, deck);
    }


    public void endGame() throws AlreadySetException {
        gameModel.setEndGamePhase();
        //if it's the last player to play, the extra round starts immediately
        if(gameModel.getActivePlayerIndex() == getPlayerList().size() - 1){
            gameModel.setLastRound(true);
        }
        EndGamePhaseMessage endGamePhaseMessage = new EndGamePhaseMessage();
        connectionHandler.sendAllMessage(endGamePhaseMessage);
    }


    public void computeLeaderboard() throws AlreadyFinishedException {
        changeState(new LeaderboardState());
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
                        .max(Map.Entry.comparingByValue()).orElseThrow().getKey();
                leaderboardMap.put(firstPlayer, playerCardPoints.get(firstPlayer) + playerAchievementPoints.get(firstPlayer));
                playerCardPoints.remove(firstPlayer);
                playerAchievementPoints.remove(firstPlayer);
            }
            LeaderboardMessage leaderboardMessage = new LeaderboardMessage(leaderboardMap);
            connectionHandler.sendAllMessage(leaderboardMessage);
        } catch (AlreadyFinishedException e) {
            System.err.println("Leaderboard already computed");
        } finally {
            System.out.println("Leaderboard computed, game finished. Bye bye");
            System.exit(0);
        }
    }

    public void disconnectionLeaderboard() {
        changeState(new LeaderboardState());
        LinkedHashMap<String, Integer> leaderboardMap = new LinkedHashMap<>();
        for (Player player : getPlayerList()) {
            leaderboardMap.put(player.getName(), player.getPoints());
        }
        LeaderboardMessage leaderboardMessage = new LeaderboardMessage(leaderboardMap);
        connectionHandler.sendAllMessage(leaderboardMessage);
        clear();
        System.out.println("Leaderboard computed, game finished because only one player left. Bye bye");
        System.exit(0);
    }

    public void clear() {
        connectionHandler.clear();
        gameModel = new GameModelInstance();
        givenStartingCards = new HashMap<>();
        givenSecretObjectiveCards = new HashMap<>();
        changeState(new LobbyState(gameModel, connectionHandler, this));
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

    public void saveGame() {
        try {
            Gson gson = new Gson();
            File file = new File(Objects.requireNonNull(getClass().getResource("/saves/game.json")).toURI());
            FileWriter fileWriter = new FileWriter(file);
            gson.toJson(gameModel, fileWriter);
            fileWriter.close();
        } catch (Exception e) {
            System.err.println("Error while saving game");
        }
    }

    public void loadGame() throws NullPointerException{
        try {
            Gson gson = new Gson();
            InputStream in = Objects.requireNonNull(getClass().getResourceAsStream("/saves/game.json"));
            InputStreamReader reader = new InputStreamReader(in);
            gameModel = gson.fromJson(reader, GameModelInstance.class);
            reader.close();
            if(gameModel == null){
                throw new NullPointerException("Failed to load game. Game model is null");
            }
            changeState(new LoadGameLobbyState(connectionHandler, gameModel, this));
        } catch (IOException e) {
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

    public void reactToDisconnection(String id){
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
        gameState.reactToDisconnection(id);
    }

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
        gameState.reconnect(oldId, newId, player, givenSecretObjectiveCards, givenStartingCards);
        OtherPlayerReconnectionMessage message = new OtherPlayerReconnectionMessage(playerName);
        connectionHandler.sendAllMessage(message);
    }
}




