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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ControllerInstance implements Controller{
    private GameModel gameModel;
    private final GeneralServerConnectionHandler connectionHandler;
    private int activePlayerIndex = -1;
    private boolean lastRound = false;
    private Map<Player, StartingCard> givenStartingCards = new HashMap<>();
    private Map<Player, List<AchievementCard>> givenSecretObjectiveCards = new HashMap<>();

    private static int ackInitBoard = 0;
    private static int ackInitHand = 0;

    public ControllerInstance(GeneralServerConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
        this.gameModel = new GameModelInstance();
    }

    @Override
    public void addPlayer(String name) throws TooManyPlayersException {

        Player player = new PlayerInstance(name);
        for (Player p : gameModel.getPlayerList()){
            if (p.getName().equals(player.getName())) throw new IllegalArgumentException("Player with same name already exists");
        }
        if(gameModel.getPlayerList().size()<4) {
            gameModel.addPlayer(player);
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
    public void shufflePlayerList() {
        gameModel.shufflePlayerList();
        //Notify
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
        shufflePlayerList();
        gameModel.createGoldResourceDecks();

    }

    public void ackInitBoard() {
        if (ackInitBoard == gameModel.getPlayerList().size()) {
            try {
                gameModel.createStartingCards();
                giveStartingCards();
            } catch (AlreadySetException e) {
                System.out.println("ERROR IN INITIALBOARD ACK");
            }
        } else {
            ackInitBoard++;
        }
    }

    public void ackInitHand() {
        if (ackInitHand == gameModel.getPlayerList().size()) {
                giveSecretObjectiveCards();
        } else {
            ackInitHand++;
        }
    }

    public void setPlayerColor(Color color, Player player) throws IllegalArgumentException{
        List<Color> colors = getPlayerList().stream().map(Player::getColor).toList();
        if(!colors.contains(color)){
            player.setColor(color);
            PlayerColorMessage playerMessage = new PlayerColorMessage(true, player.getName(), color, true);
            PlayerColorMessage allPlayersMessage = new PlayerColorMessage(true, player.getName(), color, false);
            connectionHandler.sendMessage(playerMessage, player.getName());
            connectionHandler.sendAllMessage(allPlayersMessage);
        } else {
            throw new IllegalArgumentException("Color not available");
        }

    }
    public void giveSecretObjectiveCards() {
        getPlayerList().forEach(player -> {
            givenSecretObjectiveCards.put(player, new ArrayList<>());
            List<AchievementCard> secretObjectiveCardsToPlayer = givenSecretObjectiveCards.get(player);
            try {
                AchievementCard card1 = gameModel.getAchievementDeck().popCard(DeckPosition.DECK);
                AchievementCard card2 = gameModel.getAchievementDeck().popCard(DeckPosition.DECK);
                secretObjectiveCardsToPlayer.add(card1);
                secretObjectiveCardsToPlayer.add(card2);
            } catch (AlreadyFinishedException e) {
                throw new RuntimeException(e);
            }

        });
        //Notify
    }

    public void setSecretObjectiveCard(Player player, int cardNumber) throws AlreadySetException, AlreadyFinishedException, MissingInfoException {
        AchievementCard card = givenSecretObjectiveCards.get(player).get(cardNumber);
        player.setSecretObjective(card);
        boolean allSet = getPlayerList().stream().allMatch(p -> p.getSecretObjective() != null);
        if(allSet){
            nextTurn();
        }
    }
    public void giveStartingCards() {
        List<StartingCard> startingCards = gameModel.getStartingCards();
        getPlayerList().forEach(player -> {
            StartingCard card = startingCards.removeFirst();
            givenStartingCards.put(player, card);
        });
        //Notify
    }
    public void setStartingCard(Player player, Face face) throws AlreadySetException {
        StartingCard card = givenStartingCards.get(player);
        player.initializeManuscript(card, face);
        boolean allSet = getPlayerList().stream().allMatch(p -> p.getManuscript() != null);
        if(allSet){
            try {
                giveInitialHand();
            } catch (AlreadySetException | AlreadyFinishedException e) {
                //do nothing as it's normal that it's already set
            }
        }
        //Notify
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
            } catch (TooManyElementsException e) {
                throw new AlreadySetException("Initial Hand already given");
            }

        }

        //Notify

        //Dopo aver dato la mano iniziale si prosegue con la seconda parte dell'inizializzazione: si scoprono gli obiettivi segreti e non
        gameModel.createAchievementDeck();
        giveSecretObjectiveCards();

    }
    public void nextTurn() throws MissingInfoException, AlreadyFinishedException {
        if(activePlayerIndex == -1){ //sets active player as the first player. If it's not the first turn this is not valid
            if(gameModel.getTurn() != 0){
                throw new MissingInfoException("Active player not set");
            }
        }
        //if it is end game and all players have played also an extra round, calculate leaderboard
        if(gameModel.isEndGamePhase() && activePlayerIndex == getPlayerList().size() - 1 ){
            if(lastRound){
                computeLeaderboard();
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
        //Notify
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
    public void drawCard(Player player, DeckPosition deckPosition, Decks deck) throws TooManyElementsException, InvalidMoveException, AlreadyFinishedException {
        if(getPlayerList().indexOf(player) != activePlayerIndex){
            throw new InvalidMoveException("Not player's turn");
        }
        switch (deck) {
            case RESOURCE -> {
                Card card = gameModel.getResourceDeck().popCard(deckPosition);
                player.addCardToHand(card);

            }
            case GOLD -> {
                Card card = gameModel.getGoldDeck().popCard(deckPosition);
                player.addCardToHand(card);
            }
            default -> {
                throw new IllegalArgumentException("Invalid deck");
            }
        }
        //if both decks are empty, end game phase is set
        if(gameModel.getResourceDeck().isEmpty() && gameModel.getGoldDeck().isEmpty()){
            try {
                endGame();
            } catch (AlreadySetException e) {
                //do nothing as it's normal that it's already set
            }
        }
        //Notify
    }
    /*public Boolean isPlayable(Card card , Face face)
    {
        if(face.equals(Face.BACK)) return Boolean.TRUE;
        if(card.getType() == Decks.GOLD && card.getFace(Face.FRONT).getScore() == 1 //&& gli angoli mi permetono di giocarla
            )
        {

        }
        return null;
    }*/

    @Override
    public void endGame() throws AlreadySetException {
        gameModel.setEndGamePhase();
        //if it's the last player to play, the extra round starts immediately
        if(activePlayerIndex == getPlayerList().size() - 1){
            lastRound = true;
        }
        //Notify
    }

    public List<Player> computeLeaderboard() throws AlreadyFinishedException {
        AchievementDeck achievementDeck = gameModel.getAchievementDeck();
        AchievementCard commonAchievement1 = achievementDeck.popCard(DeckPosition.FIRST_CARD);
        AchievementCard commonAchievement2 = achievementDeck.popCard(DeckPosition.SECOND_CARD);
        Map<Player,Integer> playerAchievementsPoints = new HashMap<>();
        List<Player> playerlist = getPlayerList();
        for (Player player : playerlist) {
            Manuscript manuscript = player.getManuscript();
            int points = manuscript.calculatePoints(commonAchievement1);
            points += manuscript.calculatePoints(commonAchievement2);
            points += manuscript.calculatePoints(player.getSecretObjective());
            player.addPoints(points);
            playerAchievementsPoints.put(player, points);
        }
        gameModel.setPlayerList(playerlist);
        List<Player> leaderboard = getPlayerList().stream()
                .sorted((p1, p2) -> {
                    if(p1.getPoints() == p2.getPoints()){
                        return playerAchievementsPoints.get(p2) - playerAchievementsPoints.get(p1);
                    }
                    return p2.getPoints() - p1.getPoints();
                })
                .collect(Collectors.toList());
        //Notify
        return leaderboard;
    }

    public void clear() {
        gameModel = new GameModelInstance();
        activePlayerIndex = -1;
        lastRound = false;
        givenStartingCards = new HashMap<>();
        givenSecretObjectiveCards = new HashMap<>();
    }

    public void setReady(Player player) throws MissingInfoException{
        if(player.getColor() == null) {
            throw new MissingInfoException("Color not set");
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
        //Notify
    }

    public void setNotReady(Player player) {
        player.setReady(false);
        //Notify
    }

    public boolean isReady(Player player) {
        return player.isReady();
    }

    public void addMessage(String message, Player sender) throws IllegalArgumentException{
        gameModel.getChat().addMessage(message, sender);
        ChatMessage chatMessage = new ChatMessage(new Message(message, sender.getName()));
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

    /*
    @Override
    public ServerConnectionHandler getConnectionHandler(String name) {
        try {
            return this.connectionHandler.getServerConnectionHandler(name);
        } catch (PlayerNotInAnyServerConnectionHandlerException e) {
            System.out.println("Player not found in any connection handler");
            throw new RuntimeException(e);
        }
    }

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




