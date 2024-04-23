package Server.Controller;

import Server.Card.AchievementCard;
import Server.Card.Card;
import Server.Card.CornerCardFace;
import Server.Card.StartingCard;
import Server.Chat.Message;
import Server.Connections.ServerConnectionHandler;
import Server.Deck.AchievementDeck;
import Server.Enums.*;
import Server.Exception.*;
import Server.GameModel.GameModel;
import Server.GameModel.GameModelInstance;
import Server.Manuscript.Manuscript;
import Server.Player.Player;
import com.google.gson.Gson;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *  FASE 0: INIZIALIZZAZIONE
 *  -
 *  <p>
 *  FASE 1: CONNESSIONE
 *      DOPO LA CONNESSIONE
 *
 * <p>
 *  FASE 2: PREPARAZIONE PARTITA
 *  2.0:
 *      subscribeClients()
 *        sottoscrivere i client agli eventi
 *  2.1: SHUFFLE PLAYERS, DISTRIBUISCI SEGNALINO -> INVIA DATI (ORDINE DI GIOCO DEI PLAYER)
 *       setupMatch()
 *          gameModel = new GamemodelInstance()
 *              shufflePlayers() (nel costruttore)
 *          eventHandler::notify(Event.playersOrder, )
 *  2.2: PLAYER SCEGLIE NOME, COLORE - PARALLELO -> RICEVI DATI (CONTROLLO NOME DIVERSO)
 *        gameModel.getPlayers[].setName() (dentro setplayerData)
 *  2.3: CONFERMA PER IL COLORE -> INVIA DATI
 *        setPlayerData()
 *        notify(Event.playersData) (in setPlayerData)
 *  2.5.0 : PLAYER RICEVE CARTA SEGRETA -> INVIA DATI
 *          giveSecretObjectiveCard()
 *              achievementDeck::popCard()
 *              achievementDeck::popCard()
 *              notify(Event.secretCardSelection,)
 *              receive chosen card from client
 *              player::setSecretObjectiveCard(card)
 *              notify(Event.othersSecretCardSelection,
 *  2.5: PLAYER SCEGLIE CARTE INIZIALI -> INVIA & RICEVI DATI
 *          giveStartingCards()
 *              getStartingCards() ritorna la lista mescolata di carte iniziali
 *              notify(Event.StartingCards, )
 *              receive cards face from client
 *              forEach(player -> player.initManuscript(startingCard, face));
 *              notify(Event.othersStartingCards, )
 *
 *
 *  2.6: DISTRIBUISCI CARTE -> INVIA DATI
 *          giveInitialHand()
 *              resourceDeck::popCard()
 *              player.addCardToHand(card)
 *              notify(Event.drawCard,)
 *              resourceDeck::popCard()
 *              player.addCardToHand(card)
 *              notify(Event.drawCard,)
 *              goldDeck::popCard()
 *              player::addCardToHand(card)
 *              notify(Event.drawCard,)
 *  <p>
 *  FASE 3: GIOCO
 *         gameModel.nextTurn()
 *
 *  3.0: VEDI SE IL PLAYER E ONLINE ALTRIMENTI SALTA TURNO
 *          try {
 *            new Thread(() -> {while (!fineTurno) {if (timeout) {throw new TimeoutException();}}}).start()
 *
 *  3.1: PLAYER SCEGLIE LA CARTA DA GIOCARE E LA POSIZIONE -> RICEVI DATI
 *          playerTurn()
 *              notify(Event.nextTurn, numero di turno e chi deve giocare)
 *              receive client move (resourceFrontFace, position)
 *              player::removeCardFromHand(card)
 *              player::getManuscript().setCard(resourceFrontFace, position)
 *

 *  3.2: CONTROLLO EFFETTI CARTA
 *          evaluate effects of move
 *          player::addPoints(points)
 *
 *          notify(Event.cardPlacement, carta, posizione e punti e se proprio devo, giocatroie)
 *
 *  3.4: PLAYER SCEGLIE LA CARTA DA PESCARE -> RICEVI DATI
 *          player::addCardToHand(deck.popCard())
 *  3.5: RIGENERA LA CARTA PESCATA & AGGIORNA STATO -> INVIA DATI
 *        deck::moveCardToBoard(deckPosition)
 *        notify(Event.drawCard, carta pescata e il giocatroie anche, e il mazzo, quello che serve, che cazzo ti aspettavi, ma anche il timestamp, ma perché continui, ma una chiave asimmetrica resistente ad attacchi quantici no? e certo che ce la metti scusa, che ti fai mancare le basi, ma sparati brutto frocio dimmerda comunista, assassino bastardo e la madonna mo questi vogliono occuparsi della sicurezza ma che cazzo stanno dicendo? Solo Dio sa cosa vogliono fare. Ora fil dice che in 36 ore ce la fa a implementare. Abbiamo ancora 3 mesi e 6 giorni, manca la view il server)
 *
 *
 *
 *  3.6: FINE TURNO
 *          saveData()
 *         } catch (Exception TimeoutException) {
 *                notify(Event.timeout, )
 *                skipTurn()
 *         }
 *  <p>
 *  FASE 4: FINE PARTITA
 *         setEndGamePhase()
 *         notify(Event.endGamePhase,)
 *
 *
 *  4.1: CALCOLA PUNTEGGIO OBBIETTIVI & CLASSIFICA
 *          notify(Event.gameOver, )
 *         calculate all objective cards
 *         update points
 *         sort player by points
 *         notify(Event.leaderboard, )
 *
 *  <p>
 *  FASE 5: NUOVA PARTITA / TERMINA
 *          clear all variables for reset
*/
public class ControllerInstance implements Controller{
    private GameModel gameModel;
    private final ServerConnectionHandler connectionHandler;
    private Player activePlayer;
    private boolean lastRound = false;

    public ControllerInstance(ServerConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
        this.gameModel = new GameModelInstance();
    }
    public void addPlayer(Player player) throws TooManyPlayersException, IllegalArgumentException {
        if(gameModel.getPlayerList().size()<4) {
            gameModel.addPlayer(player);
        } else {
            throw new TooManyPlayersException("Too many players");
        }
        //Notify
    }

    public void removePlayer(Player player) throws IllegalArgumentException{
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
        for(Player player : gameModel.getPlayerList()) {
            if(!player.isReady()){
                throw new TooFewElementsException("Not all players are ready");
            }
        }
        shufflePlayerList();
        gameModel.createGoldResourceDecks();
        gameModel.createStartingCards();
        giveStartingCards();
        //a questo punto start finisce. Si attende il set delle starting cards. Quando tutte saranno settate si procede con giveInitialHand
    }
    public void setPlayerColor(Color color, Player player) throws IllegalArgumentException{
        List<Color> colors = getPlayerList().stream().map(Player::getColor).collect(Collectors.toList());
        if(!colors.contains(color)){
            player.setColor(color);
        } else {
            throw new IllegalArgumentException("Color already taken");
        }
        //Notify
    }
    public void giveSecretObjectiveCards() {
        getPlayerList().forEach(player -> {
            Card card1 = gameModel.getAchievementDeck().popCard(DeckPosition.DECK);
            Card card2 = gameModel.getAchievementDeck().popCard(DeckPosition.DECK);
        });
        //Notify
    }

    public void setSecretObjectiveCard(Player player, AchievementCard card) throws AlreadySetException {
        player.setSecretObjective(card);
    }
    public void giveStartingCards() {
        List<StartingCard> startingCards = gameModel.getStartingCards();
        getPlayerList().forEach(player -> {
            Card card = startingCards.remove(0);
        });
        //Notify
    }
    public void setStartingCard(Player player, StartingCard card, Face face) throws AlreadySetException {
        player.initializeManuscript(card, face);
        //Notify
    }
    public void giveInitialHand() throws AlreadySetException{
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
        //a questo punto si attende. Quando tutti i giocatori avranno scelto il proprio obiettivo segreto è necessario che venga chiamato nextTurn in modo che la partita inizi
    }
    public void nextTurn() throws MissingInfoException {
        if(activePlayer == null){ //sets active player as the first player. If it's not the first turn this is not valid
            if(gameModel.getTurn() != 0){
                throw new MissingInfoException("Active player not set");
            } else {
                activePlayer = gameModel.getPlayerList().getFirst();
            }
        }
        //if it is end game and all players have played also an extra round, calculate leaderboard
        if(gameModel.isEndGamePhase() && getPlayerList().indexOf(activePlayer) == getPlayerList().size() - 1 ){
            if(lastRound){
                computeLeaderboard();
            } else {
                lastRound = true;
            }
        } else {
            //sets end game if necessary
            if (activePlayer.getPoints() >= 20) {
                try {
                    endGame();
                } catch (AlreadySetException e) {
                    //do nothing as it's normal that it's already set
                }
            }
            //sets new active player
            do {
                int playerIndex = gameModel.getPlayerList().indexOf(activePlayer);
                if (playerIndex == gameModel.getPlayerList().size() - 1) {
                    activePlayer = gameModel.getPlayerList().getFirst();
                } else {
                    activePlayer = gameModel.getPlayerList().get(playerIndex + 1);
                }
            } while (!isOnline(activePlayer));//if the player is not online skips to the next one
            gameModel.nextTurn();
        }
        //Notify
    }
    public int getTurn() {
        return gameModel.getTurn();
    }

    @Override
    public Player getActivePlayer() {
        return activePlayer;
    }

    public boolean isOnline(Player player) {
        //Todo implementare
        return true;
    }
    public void playCard(Player player, int position, int xCoord, int yCoord, Face face) throws TooFewElementsException, InvalidMoveException {
        //if it's not the player's turn, throw exception
        if(player != activePlayer){
            throw new InvalidMoveException("Not player's turn");
        }
        CornerCardFace cardFace = player.getHand().get(position).getCornerFace(face);
        if(!player.getManuscript().isPlaceable(xCoord, yCoord, cardFace)){
            throw new InvalidMoveException("Card not placeable. Check the position and the placement requirements");
        }
        try {
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
        } catch (TooFewElementsException e) { //if it's end game, you can play a card when you have 2 and not 3
            if(gameModel.isEndGamePhase()){
                if(player.getHand().size() < 2){
                    throw new TooFewElementsException("Not enough cards in hand");
                }
            } else {
                throw new TooFewElementsException("Not enough cards in hand");
            }
        }

        //Notify
    }
    public void drawCard(Player player, DeckPosition deckPosition, Decks deck) throws TooManyElementsException, InvalidMoveException {
        if(player != activePlayer){
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

    @Override
    public void endGame() throws AlreadySetException {
        gameModel.setEndGamePhase();
        //if it's the last player to play, the extra round starts immediately
        if(getPlayerList().indexOf(activePlayer) == getPlayerList().size() - 1){
            lastRound = true;
        }
        //Notify
    }

    public List<Player> computeLeaderboard() {
        for (Player player : getPlayerList()) {
            Manuscript manuscript = player.getManuscript();
            AchievementDeck achievementDeck = gameModel.getAchievementDeck();
            int points = manuscript.calculatePoints(achievementDeck.popCard(DeckPosition.FIRST_CARD));
            points += manuscript.calculatePoints(achievementDeck.popCard(DeckPosition.SECOND_CARD));
            points += manuscript.calculatePoints(player.getSecretObjective());
            player.addPoints(points);
        }
        List<Player> leaderboard = getPlayerList().stream().sorted((p1, p2) -> p2.getPoints() - p1.getPoints()).collect(Collectors.toList());
        //Notify
        return leaderboard;
    }

    public void clear() {
        gameModel = new GameModelInstance();
    }

    public void setReady(Player player) throws MissingInfoException{
        if(player.getColor() == null) {
            throw new MissingInfoException("Color not set");
        }
        player.setReady(true);
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
        //Notify
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
}




