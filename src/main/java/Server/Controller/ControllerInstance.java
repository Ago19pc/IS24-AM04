package Server.Controller;

import Server.Card.AchievementCard;
import Server.Card.Card;
import Server.Card.ResourceFrontFace;
import Server.Card.StartingCard;
import Server.Connections.ConnectionHandler;
import Server.Enums.*;
import Server.GameModel.GameModel;
import Server.GameModel.GameModelInstance;
import Server.Manuscript.Manuscript;
import Server.Player.Player;

import java.util.HashMap;
import java.util.List;

import static java.lang.System.in;

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
    private final GameModel gameModel;
    private final ConnectionHandler connectionHandler;
    private void subscribeClients() {};
    private void setupMatch() {};
    private void playerTurn() {};
    private void saveData() {};
    private void setEndGamePhase() {};
    private void reset() {};
    private void chat() {};
    /**
     * Calculates achievement points forall players
     * @return void
     */
    private void calculatePoints() {//todo: forall players and forall achievements call manuscript.calculatepoints and sum them up};
    };

    public ControllerInstance(ConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
        this.gameModel = new GameModelInstance();
    }
    public void addPlayer(Player player) {
        if(gameModel.getPlayerList().size()<4) {
            gameModel.addPlayer(player);
        }
        //Notify
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
    public void start()
    {
        shufflePlayerList();
        //TODO : implementare il resto
        //Notify
    }
    public void setPlayerColor(Color color, Player player) {
        List<Color> colors = getPlayerList().stream().map(Player::getColor).toList();
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
        player.removeCardFromHand(card);
        if (card.getCornerFace((Face.FRONT)).equals(card.getCornerFace(face))) {
            player.getManuscript().addCard(xCoord, yCoord, card.getCornerFace(Face.FRONT), getTurn());
        }
        if (card.getCornerFace((Face.BACK)).equals(card.getCornerFace(face))) {
            player.getManuscript().addCard(xCoord, yCoord, card.getCornerFace(Face.BACK), getTurn());
        }
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

}




