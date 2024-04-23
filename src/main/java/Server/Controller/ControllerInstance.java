package Server.Controller;

import Server.Card.AchievementCard;
import Server.Card.Card;
import Server.Card.CornerCardFace;
import Server.Card.StartingCard;
import Server.Chat.Message;
import Server.Connections.ServerConnectionHandler;
import Server.Deck.AchievementDeck;
import Server.Enums.*;
import Server.Exception.PlayerNotFoundByNameException;
import Server.GameModel.GameModel;
import Server.GameModel.GameModelInstance;
import Server.Manuscript.Manuscript;
import Server.Player.Player;
import Server.Player.PlayerInstance;
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
    /**
     * Calculates achievement points forall players
     * @return void
     */
    private void calculatePoints() {//todo: forall players and forall achievements call manuscript.calculatepoints and sum them up};
    };

    public ControllerInstance(ServerConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
        this.gameModel = new GameModelInstance();
    }
    public void addPlayer(Player player) {
        if(gameModel.getPlayerList().size()<4) {
            gameModel.addPlayer(player);
        }
        //Notify
    }

    public void addPlayer(String name) {
        Player player = new PlayerInstance(name);
        if(gameModel.getPlayerList().size()<4) {
            gameModel.addPlayer(player);
        }
    }
    public void removePlayer(Player player) {
        gameModel.removePlayer(player);
    }
    public List<Player> getPlayerList() {
        return gameModel.getPlayerList();
    }
    public void shufflePlayerList() {
        gameModel.shufflePlayerList();
        //Notify
    }
    public void start()
    {
        shufflePlayerList();
        //TODO : implementare il resto
        //Notify
    }
    public void setPlayerColor(Color color, Player player) {
        List<Color> colors = getPlayerList().stream().map(Player::getColor).collect(Collectors.toList());
        if(!colors.contains(color)){
            player.setColor(color);
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

    public void setSecretObjectiveCard(Player player, AchievementCard card) {
        player.setSecretObjective(card);
    }
    public void giveStartingCards() {
        List<StartingCard> startingCards = gameModel.getStartingCards();
        getPlayerList().forEach(player -> {
            Card card = startingCards.remove(0);
        });
        //Notify
    }
    public void setStartingCard(Player player, StartingCard card, Face face) {
        player.initializeManuscript(card, face);
        //Notify
    }
    public void giveInitialHand() {
        getPlayerList().forEach(player -> {
            Card card1 = gameModel.getResourceDeck().popCard(DeckPosition.DECK);
            Card card2 = gameModel.getResourceDeck().popCard(DeckPosition.DECK);
            Card card3 = gameModel.getGoldDeck().popCard(DeckPosition.DECK);
            player.addCardToHand(card1);
            player.addCardToHand(card2);
            player.addCardToHand(card3);
        });
        //Notify
    }
    public void nextTurn() {
        gameModel.nextTurn();
        //Notify
    }
    public int getTurn() {
        return gameModel.getTurn();
    }
    public boolean isOnline(Player player) {
        //Todo implementare
        return true;
    }
    public void playCard(Player player, Card card, int xCoord, int yCoord, Face face) {
        CornerCardFace cardFace = card.getCornerFace(face);
        player.removeCardFromHand(card);
        int cardPoints = cardFace.getScore();
        Map<Symbol, Integer> scoreRequirements = cardFace.getScoreRequirements();
        if(scoreRequirements != null){
            Symbol requiredSymbol = (Symbol) scoreRequirements.keySet().toArray()[0];
            int requiredQuantity = scoreRequirements.get(requiredSymbol);
            int actualQuantity;
            if(requiredSymbol == Symbol.COVERED_CORNER){
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
    public void drawCard(Player player, DeckPosition deckPosition, Decks deck) {
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
    public void endGame() {
        gameModel.setEndGamePhase();
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

    public void setReady(Player player) {
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

    public void addMessage(String message, Player sender) {
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

    @Override
    public Player getPlayerByName(String name) throws PlayerNotFoundByNameException {
        for (Player p : this.gameModel.getPlayerList()){
            if (p.getName().equals(name)) return p;
        }
        throw new PlayerNotFoundByNameException(name);
    }

    @Override
    public ServerConnectionHandler getConnectionHandler() {
        return this.connectionHandler;
    }
}




