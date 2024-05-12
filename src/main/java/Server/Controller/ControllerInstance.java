package Server.Controller;

import Server.Card.AchievementCard;
import Server.Card.Card;
import Server.Card.CornerCardFace;
import Server.Card.StartingCard;
import Server.Chat.Message;
import Server.Connections.GeneralServerConnectionHandler;
import Server.Connections.ServerConnectionHandler;
import Server.Connections.ServerConnectionHandlerRMI;
import Server.Connections.ServerConnectionHandlerSOCKET;
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

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ControllerInstance implements Controller{
    private GameModel gameModel;
    private final GeneralServerConnectionHandler connectionHandler;
    private int activePlayerIndex = -1;
    private boolean lastRound = false;
    private Map<Player, StartingCard> givenStartingCards = new HashMap<>();
    private Map<Player, List<AchievementCard>> givenSecretObjectiveCards = new HashMap<>();

    public ControllerInstance(GeneralServerConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
        this.gameModel = new GameModelInstance();
    }

    @Override
    public void addPlayer(String name, String clientID) throws TooManyPlayersException {
        Player player = new PlayerInstance(name);
        for (Player p : gameModel.getPlayerList()){
            if (p.getName().equals(player.getName())) throw new IllegalArgumentException("Player with same name already exists");
        }
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

    public void removePlayer(Player player) {
        gameModel.removePlayer(player);
        //Notify
    }
    public List<Player> getPlayerList() {
        return gameModel.getPlayerList();
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
                        gameModel.getGoldDeck().getBoardCard().get(DeckPosition.FIRST_CARD),
                        gameModel.getGoldDeck().getBoardCard().get(DeckPosition.SECOND_CARD)
                ),
                List.of(
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
        AchievementCard card = (AchievementCard) givenSecretObjectiveCards.get(player).get(cardNumber);
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
        SetStartingCardMessage setStartingCardMessage = new SetStartingCardMessage(player.getName(), card.getFace(face));
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
     * Sets the next turn
     */
    private void nextTurn(){
        if(activePlayerIndex == -1){ //sets active player as the first player. If it's not the first turn this is not valid
            if(gameModel.getTurn() != 0){
                System.out.println("Active player not set");
                return;
            }
        }
        //if it is end game and all players have played also an extra round, calculate leaderboard
        if(gameModel.isEndGamePhase() && activePlayerIndex == getPlayerList().size() - 1 ){
            if(lastRound){
                try{
                    computeLeaderboard();
                } catch (AlreadyFinishedException e) {
                    e.printStackTrace();
                }
            } else {
                lastRound = true;
            }
        } else {
            //sets end game if necessary
            if (activePlayerIndex != -1 && getPlayerList().get(activePlayerIndex).getPoints() >= 20) {
                try {
                    endGame();
                } catch (AlreadySetException e) {
                    //do nothing as it's normal that it's already set
                }
            }
            //sets new active player
            do {
                if (activePlayerIndex == gameModel.getPlayerList().size() - 1) {
                    activePlayerIndex = 0;
                } else {
                    activePlayerIndex++;
                }
            } while (!isOnline(getPlayerList().get(activePlayerIndex)));//if the player is not online skips to the next one
            gameModel.nextTurn();
        }
    }
    public int getTurn() {
        return gameModel.getTurn();
    }

    public boolean isOnline(Player player) {
        //Todo implementare
        return true;
    }
    public void playCard(Player player, int position, int xCoord, int yCoord, Face face) throws TooFewElementsException, InvalidMoveException {
        //if it's not the player's turn, throw exception
        if(getPlayerList().indexOf(player) != activePlayerIndex){
            throw new InvalidMoveException("Not player's turn");
        }
        CornerCardFace cardFace = player.getHand().get(position).getCornerFace(face);
        if(!player.getManuscript().isPlaceable(xCoord, yCoord, cardFace)) {
            throw new InvalidMoveException("Card not placeable. Check the position and the placement requirements");
        }
        if(player.getHand().size() < 2 || (player.getHand().size() == 2 && !gameModel.isEndGamePhase())){
            throw new TooFewElementsException("Not enough cards in hand");
        }
            player.removeCardFromHand(position);
            int cardPoints;
            try {
                cardPoints = cardFace.getScore();
            } catch (UnsupportedOperationException e) {
                if (e.getMessage() == "Regular cards do not have scores") ;
                cardPoints = 0;
            }
            Map<Symbol, Integer> scoreRequirements;
            try {
                scoreRequirements = cardFace.getScoreRequirements();
            } catch (UnsupportedOperationException e) {
                if (e.getMessage() == "Regular cards do not have score requirements") ;
                scoreRequirements = null;
            }
            if (scoreRequirements != null) {
                Symbol requiredSymbol = (Symbol) scoreRequirements.keySet().toArray()[0];
                int requiredQuantity = scoreRequirements.get(requiredSymbol);
                int actualQuantity;
                if (requiredSymbol == Symbol.COVERED_CORNER) {
                    actualQuantity = player.getManuscript().getCardsUnder(cardFace).size();
                } else {
                    actualQuantity = player.getManuscript().getSymbolCount(requiredSymbol);
                    int quantityOnCard = cardFace.getCornerSymbols().entrySet().stream()
                            .filter(entry -> entry.getValue() == requiredSymbol).collect(Collectors.toList()).size();
                    actualQuantity += quantityOnCard;
                }
                player.addPoints(actualQuantity / requiredQuantity * cardPoints);
            } else {
                player.addPoints(cardPoints);
            }
            player.getManuscript().addCard(xCoord, yCoord, cardFace, getTurn());

        //Notify
    }
    public void drawCard(Player player, DeckPosition deckPosition, Decks deck) throws TooManyElementsException, InvalidMoveException, AlreadyFinishedException, NotYetStartedException {
        if(gameModel.getTurn() < 1) {
            throw new NotYetStartedException("Game not started");
        }
        if(getPlayerList().indexOf(player) != activePlayerIndex){
            throw new InvalidMoveException("Not player's turn");
        }
        if(player.getHand().size() >= 3 /*todo: add case regarding end game phase (you may (but not necessarily) have 1 card less in your hand*/){
            throw new TooManyElementsException("Hand is full");
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
            default -> {
                throw new IllegalArgumentException("Invalid deck");
            }
        }
        DrawCardMessage drawCardMessage = new DrawCardMessage(drawnCard);
        connectionHandler.sendMessage(drawCardMessage, player.getName());
        //if both decks are empty, end game phase is set
        if(gameModel.getResourceDeck().isEmpty() && gameModel.getGoldDeck().isEmpty()){
            try {
                endGame();
            } catch (AlreadySetException e) {
                //do nothing as it's normal that it's already set
            }
        }
        nextTurn();
        OtherPlayerDrawCardMessage otherPlayerDrawCardMessage = new OtherPlayerDrawCardMessage(
                player.getName(),
                deck,
                deckPosition,
                switch (deck) {
                    //todo: aggiungere un metodo ai deck che ritorni le boardcards in una lista per evitare sto macello
                    case RESOURCE -> List.of(gameModel.getResourceDeck().getBoardCard().get(DeckPosition.FIRST_CARD), gameModel.getResourceDeck().getBoardCard().get(DeckPosition.SECOND_CARD));
                    case GOLD -> List.of(gameModel.getGoldDeck().getBoardCard().get(DeckPosition.FIRST_CARD), gameModel.getGoldDeck().getBoardCard().get(DeckPosition.SECOND_CARD));
                    default -> null;
                },
                getTurn(),
                getPlayerList().get(activePlayerIndex).getName()
        );
        connectionHandler.sendAllMessage(otherPlayerDrawCardMessage);
    }

    /**
     * Starts the end game phase
     * @throws AlreadySetException if the end game phase is already set
     */
    private void endGame() throws AlreadySetException {
        gameModel.setEndGamePhase();
        //if it's the last player to play, the extra round starts immediately
        if(activePlayerIndex == getPlayerList().size() - 1){
            lastRound = true;
        }
        EndGamePhaseMessage endGamePhaseMessage = new EndGamePhaseMessage();
        connectionHandler.sendAllMessage(endGamePhaseMessage);
    }

    /**
     * Computes the leaderboard and sends it to all players
     * @throws AlreadyFinishedException if there aren't achievement cards to pop
     */
    private void computeLeaderboard() throws AlreadyFinishedException {
        AchievementDeck achievementDeck = gameModel.getAchievementDeck();
        AchievementCard commonAchievement1 = achievementDeck.popCard(DeckPosition.FIRST_CARD);
        AchievementCard commonAchievement2 = achievementDeck.popCard(DeckPosition.SECOND_CARD);
        List<Player> playerlist = getPlayerList();
        Map<String,Integer> leaderboardMap = new HashMap<>();
        for (Player player : playerlist) {
            Manuscript manuscript = player.getManuscript();
            int points = manuscript.calculatePoints(commonAchievement1);
            points += manuscript.calculatePoints(commonAchievement2);
            points += manuscript.calculatePoints(player.getSecretObjective());
            player.addPoints(points);
            leaderboardMap.put(player.getName(), points); //todo: add parity check
        }
        LeaderboardMessage leaderboardMessage = new LeaderboardMessage(leaderboardMap);
        connectionHandler.sendAllMessage(leaderboardMessage);
        clear();
    }

    private void clear() {
        gameModel = new GameModelInstance();
        activePlayerIndex = -1;
        lastRound = false;
        givenStartingCards = new HashMap<>();
        givenSecretObjectiveCards = new HashMap<>();
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

    public void saveGame() throws IOException {
        Gson gson = new Gson();
        FileWriter fileWriter = new FileWriter("saves/game.json");
        gson.toJson(gameModel, fileWriter);
        fileWriter.close();
    }

    public void loadGame() throws IOException {
        Gson gson = new Gson();
        FileReader fileReader = new FileReader("saves/game.json");
        gameModel = gson.fromJson(fileReader, GameModelInstance.class);
        fileReader.close();
    }

    @Override
    public Player getPlayerByName(String name) throws PlayerNotFoundByNameException {
        for (Player p : this.gameModel.getPlayerList()){
            if (p.getName().equals(name)) return p;
        }
        throw new PlayerNotFoundByNameException(name);
    }
    /**
     * get the ConncectionHandler
     * @return connectionHandler the conncectionHandler
     */
    public GeneralServerConnectionHandler getConnectionHandler() {
        return this.connectionHandler;
    }


    @Override
    public void printData() {
        System.out.println("----------");
        System.out.println("Players:");
        this.gameModel.getPlayerList().stream().forEach(p -> System.out.print(p.getName() + " " + p.getColor() + " " + p.isReady() +  ", "));
        System.out.println("\n");
    }
}




