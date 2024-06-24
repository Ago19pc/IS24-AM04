package Server.Deck;

import Server.Card.*;
import Server.Enums.CardCorners;
import Server.Enums.DeckPosition;
import Server.Enums.Symbol;
import Server.Exception.AlreadyFinishedException;
import Server.Exception.IncorrectDeckPositionException;

import java.io.*;
import java.util.*;

import static Server.Enums.DeckPosition.*;

/**
 * Class for the Gold cards Deck
 */
public class GoldDeck implements Deckable {
    private final List<GoldCard> cards;
    private final Map<DeckPosition, GoldCard> boardCards;

    /**
     * Constructor
     * Creates the cards, shuffles them and moves the first two to the board
     */
    public GoldDeck(){
        this.boardCards = new HashMap<>();
        this.cards = new ArrayList<>();
        boardCards.put(FIRST_CARD, null);
        boardCards.put(SECOND_CARD, null);
        createCards();
        shuffle();
        try {
            moveCardToBoard(FIRST_CARD);
            moveCardToBoard(SECOND_CARD);
        } catch (Exception e) {
            System.err.println("Error while moving cards to the board, generating gold deck");
        }
    }
    public GoldDeck(Boolean test){
        this.boardCards = new HashMap<>();
        this.cards = new ArrayList<>();
        boardCards.put(FIRST_CARD, null);
        boardCards.put(SECOND_CARD, null);
        createCards();
        try {
            moveCardToBoard(FIRST_CARD);
            moveCardToBoard(SECOND_CARD);
        } catch (Exception e) {
            System.err.println("Error while moving cards to the board, generating gold deck");
        }
    }

    /**
     * Shuffle the deck
     */
    void shuffle() {
        // shuffle the deck
        Collections.shuffle(cards);
    }

    /**
     * Moves a card from the deck to the board
     * @param where_to the position to add the card to
     */
    public void moveCardToBoard(DeckPosition where_to) {
        if (where_to != DECK) {
            Card cardToMove;
            try {
                cardToMove = popCard(DECK);
            } catch (AlreadyFinishedException e) {
                cardToMove = null;
            }
            try {
                addCard(cardToMove, where_to);
            } catch (IncorrectDeckPositionException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Gets the board cards (common achievements)
     * @return a map linking board positions to the cards
     */
    public Map<DeckPosition, GoldCard> getBoardCard() {
        Map<DeckPosition, GoldCard> boardCardsToReturn = new HashMap<>();
        boardCardsToReturn.put(DECK, getTopCardNoPop());
        boardCardsToReturn.put(FIRST_CARD, boardCards.get(FIRST_CARD));
        boardCardsToReturn.put(SECOND_CARD, boardCards.get(SECOND_CARD));

        return boardCardsToReturn;
    }

    public GoldCard popCard(DeckPosition position) throws AlreadyFinishedException{
        if (position == DECK) {
            return cards.removeFirst();
        } else {
            // LA POSIZIONE NON E' DECK

            if(boardCards.get(position) == null){
                throw new AlreadyFinishedException("There is no card in the position " + position);
            }
            // STIAMO PESCANDO DA FIRST O SECOND

            // RITORNA LA CARTA E LA RIMUOVE DALLA POSIZIONE E RIMPIAZZALA
            GoldCard toReturn = boardCards.get(position);
            moveCardToBoard(position);

            return toReturn;
        }

    }


    public boolean isEmpty() {
        return cards.isEmpty() && boardCards.get(FIRST_CARD) == null && boardCards.get(SECOND_CARD) == null;
    }


    public void addCard(Card card, DeckPosition position) throws IncorrectDeckPositionException {
        if (position == DECK)
            throw new IncorrectDeckPositionException("Cannot add card to the deck, only to FIST_CARD or SECOND_CARD.");
        if (position != DECK)
            boardCards.put(position, (GoldCard) card);
    }

    @Override
    public GoldCard getTopCardNoPop() {
        if(cards.isEmpty())
            return null;
        return cards.getFirst();
    }

    @Override
    public Card getCard(int position) {
        return cards.get(position);
    }

    /**
     * Get the number of cards in the deck
     * @return int the number of cards in the deck
     */
    public int getNumberOfCards() {
        return cards.size();
    }

    /**
     * generate the cards
     */
    private void createCards() {
        InputStream fileFRONT;
        InputStream fileBACK;
        BufferedReader readerFRONT;
        BufferedReader readerBACK;

        try {
            fileFRONT = getClass().getResourceAsStream("/images/GoldFrontFace.txt");
            fileBACK = getClass().getResourceAsStream("/images/GoldBackFace.txt");
            readerFRONT = new BufferedReader(new InputStreamReader(fileFRONT));
            readerBACK = new BufferedReader(new InputStreamReader(fileBACK));



            String lineF;
            int cardNumber = 0;
            int counter = 39;
            while ((lineF = readerFRONT.readLine()) != null) {
                counter++;

                String[] partsF = lineF.split(" ");
                //System.out.println(lineF);

                String partsB = readerBACK.readLine();
                //System.out.println(partsB);


                Map<CardCorners, Symbol> cornerSymbolsF = new HashMap<>();

                Map<Symbol, Integer> scoreRequirementsF = new HashMap<>();
                for (Symbol s : Symbol.values()) {
                    scoreRequirementsF.put(s, 0);
                }

                Map<Symbol, Integer> placementRequirementsF = new HashMap<>();
                for (Symbol s : Symbol.values()) {
                    placementRequirementsF.put(s, 0);
                }


                int point = 0;
                for (int i = 0; i < partsF.length; i++) {

                    if (i < 4) {
                        //System.out.println("corner: " + CardCorners.values()[i] + " symbol: " + Symbol.valueOf(partsF[i])+" ");
                        cornerSymbolsF.put(CardCorners.values()[i], Symbol.valueOf(partsF[i]));
                    } else if (i == partsF.length - 1) point = Integer.parseInt(partsF[i]);
                    else {
                        // SCORE REQUIREMENT DOPO LA ,
                        if (Objects.equals(partsF[i], ",")) {
                            i++;
                            scoreRequirementsF.put(Symbol.valueOf(partsF[i]), 1);
                        }
                        // PLACEMENT REQUIREMENT I RIMANENTI
                        else {
                            int quantity = Integer.parseInt(partsF[i]);
                            i++;
                            placementRequirementsF.put(Symbol.valueOf(partsF[i]), quantity);

                        }
                    }

                }
                Symbol kingdom = switch (cardNumber / 10) {
                    case 0 -> Symbol.FUNGUS;
                    case 1 -> Symbol.PLANT;
                    case 2 -> Symbol.ANIMAL;
                    case 3 -> Symbol.BUG;
                    default -> Symbol.NONE;
                };
                GoldFrontFace frontFace = new GoldFrontFace("front-" + counter + ".jpeg", cornerSymbolsF, point, placementRequirementsF, scoreRequirementsF, kingdom);

                // DA QUI E DA VEDERE
                List<Symbol> centerSymbolsB = new ArrayList<>();
                centerSymbolsB.add(Symbol.valueOf(partsB));


                RegularBackFace backFace = new RegularBackFace("back-" + counter + ".jpeg", centerSymbolsB);

                GoldCard card = new GoldCard(frontFace, backFace);
                this.cards.add(card);
                cardNumber++;
            }
        } catch (Exception e) {
            System.err.println("An error occurred while generating cards");
        }




    }
}
