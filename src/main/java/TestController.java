package main.java;

import main.java.GameModel.GameModel;
import main.java.GameModel.GameModelInstance;

/**
 *  FASE 0: INIZIALIZZAZIONE
 *  -
 *  <p>
 *  FASE 1: CONNESSIONE
 * <p>
 *  FASE 2: PREPARAZIONE PARTITA
 *  2.1: SHUFFLE PLAYERS, DISTRIBUISCI SEGNALINO -> INVIA DATI (ORDINE DI GIOCO DEI PLAYER)
 *  2.2: PLAYER SCEGLIE NOME, COLORE - PARALLELO -> RICEVI DATI (CONTROLLO NOME DIVERSO)
 *  2.3: AGGIORNA STATO -> INVIA DATI
            player::setColor(color)
 *  2.4: CONFERMA PER IL COLORE -> INVIA DATI
 *  2.5.0 : PLAYER RICEVE CARTA SEGRETA -> INVIA DATI
 *          achievementDeck::popCard()
 *          achievementDeck::popCard()
 *          send cards to client
 *          receive chosen card from client
 *          player::setSecretObjectiveCard(card)
 *  2.5: PLAYER SCEGLIE CARTE INIZIALI -> INVIA & RICEVI DATI
 *          getStartingCards() ritorna la lista mescolata di carte iniziali
 *          send cards to client
 *          receive cards face from client
 *          forEach(player -> player.initManuscript(startingCard, face));
 *          achievementDeck::popCard()
 *          send card to client
 *
 *  2.6: DISTRIBUISCI CARTE -> INVIA DATI
 *        resourceDeck::popCard()
 *        player.addCardToHand(card)
 *        resourceDeck::popCard()
 *           player.addCardToHand(card)
 *         goldDeck::popCard()
 *           player::addCardToHand(card)
 *         send player::getHand()
 *  <p>
 *  FASE 3: GIOCO
 *         turn ++
 *         notify decks & manuscript to all players
 *         forall deck: deck::getBoardCards()
 *         forall player: player::getHand()
 *
 *  3.0: VEDI SE IL PLAYER E ONLINE ALTRIMENTI SALTA TURNO
 *          check online status
 *          if not online skip turn (continue)
 *
 *         for each client:
 *          notify begin turn
 *
 *  3.1: PLAYER SCEGLIE LA CARTA DA GIOCARE E LA POSIZIONE -> RICEVI DATI
 *          recive client move (resourceFrontFace, position)
 *          player::removeCardFromHand(card)
 *          player::getManuscript().setCard(resourceFrontFace, position)

 *  3.2: CONTROLLO EFFETTI CARTA
 *          evaluete effects of move
 *          player::addPoints(points)
 *          notify player of new point
 *
 *  3.4: PLAYER SCEGLIE LA CARTA DA PESCARE -> RICEVI DATI
 *          notify player to draw card (deck, deckPosition)
 *          player::addCardToHand(deck.popCard())
 *
 *          notify end turn
 *
 *  3.5: RIGENERA LA CARTA PESCATA & AGGIORNA STATO -> INVIA DATI
 *        deck::moveCardToBoard(deckPosition)
 *        notify client of new board deck::getBoardCard()
 *
 *  3.6: FINE TURNO
 *         notify end turn to client
 *  <p>
 *  FASE 4: FINE PARTITA
 *         setEndGamePhase(true)
 *         notify clients of end game
 *
 *  4.1: CALCOLA PUNTEGGIO OBBIETTIVI & CLASSIFICA
 *         calculate all objective cards
 *         update points
 *         sort player by points
 *         send leaderboard
 *  <p>
 *  FASE 5: NUOVA PARTITA / TERMINA
 *          clear all variables for reset
*/
public class TestController {
    public static void main(String[] args) {
        GameModel game = new GameModelInstance();
    }
}




