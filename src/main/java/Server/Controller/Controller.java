package Server.Controller;

import Server.Client.Client;
import Server.Enums.Color;
import Server.GameModel.GameModel;
import Server.GameModel.GameModelInstance;

import java.beans.EventHandler;
import java.util.List;

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
public class Controller {
    private static List<Client> clients;
    private static GameModel gameModel;
    private static EventHandler eventHandler;
    /**
     * main.Main method, here goes the program
     * @param args the arguments, that I don't know, they're useless, just like me :'(
     */
    public static void main(String[] args) {

        gameModel = new GameModelInstance();

    }

    private void setPlayerData(String name, Color color) {};
    private void giveSecretObjectiveCard() {};
    private void subscribeClients() {};
    private void setupMatch() {};
    private void giveStartingCards() {};
    private void giveInitialHand() {};
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

}




