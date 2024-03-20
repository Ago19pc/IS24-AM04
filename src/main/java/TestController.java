package main.java;
/**
 *  FASE 0: INIZIALIZZAZIONE
 *  -
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
 *          send card to client
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
 *
 *
 *  3.5: RIGENERA LA CARTA PESCATA & AGGIORNA STATO -> INVIA DATI
 *  3.6: FINE TURNO
 *  <p>
 *  FASE 4: FINE PARTITA
 *  4.1: CALCOLA PUNTEGGIO OBBIETTIVI & CLASSIFICA
 *  4.2: AGGIORNA STATO -> INVIA DATI
 *  <p>
 *
 *  FASE 5: NUOVA PARTITA / TERMINA
*/
public class TestController {
    public static void main(String[] args) {

    }
}




