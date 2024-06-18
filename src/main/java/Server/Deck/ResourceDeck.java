package Server.Deck;

import Server.Card.Card;
import Server.Card.RegularBackFace;
import Server.Card.ResourceCard;
import Server.Card.ResourceFrontFace;
import Server.Enums.CardCorners;
import Server.Enums.DeckPosition;
import Server.Enums.Symbol;
import Server.Exception.AlreadyFinishedException;
import Server.Exception.IncorrectDeckPositionException;

import java.io.*;
import java.util.*;

import static Server.Enums.DeckPosition.*;

/**
 * Class for the Resource cards Deck
 */
public class ResourceDeck implements Deckable {
    protected final List<ResourceCard> cards;
    private final Map<DeckPosition, ResourceCard> boardCards;
    public ResourceDeck(){
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
            System.err.println("Error while moving cards to the board, generating resource deck");
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
    public Map<DeckPosition, ResourceCard> getBoardCard() {
        Map<DeckPosition, ResourceCard> boardCardsToReturn = new HashMap<>();
        boardCardsToReturn.put(DECK, getTopCardNoPop());
        boardCardsToReturn.put(FIRST_CARD, boardCards.get(FIRST_CARD));
        boardCardsToReturn.put(SECOND_CARD, boardCards.get(SECOND_CARD));

        return boardCardsToReturn;
    }


    public ResourceCard popCard(DeckPosition position) throws  AlreadyFinishedException{

            if (position == DECK) {
                return cards.removeFirst();
            } else {
                // LA POSIZIONE NON E' DECK

                if(boardCards.get(position) == null){
                    throw new AlreadyFinishedException("There is no card in the position " + position);
                }
                // STIAMO PESCANDO DA FIRST O SECOND

                // RITORNA LA CARTA E LA RIMUOVE DALLA POSIZIONE E RIMPIAZZALA
                ResourceCard toReturn = boardCards.get(position);
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
            boardCards.put(position, (ResourceCard) card);
    }

    @Override
    public ResourceCard getTopCardNoPop() {
        if(cards.isEmpty())
            return null;
        return cards.getFirst();
    }

    /**
     * Returns the number of cards in the deck
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
            fileFRONT = getClass().getResourceAsStream("/images/ResourceFrontFace.txt");
            fileBACK = getClass().getResourceAsStream("/images/ResourceBackFace.txt");
            readerFRONT = new BufferedReader(new InputStreamReader(fileFRONT));
            readerBACK = new BufferedReader(new InputStreamReader(fileBACK));

            String lineF;
            int cardNumber = 0;
            int counter = -1;
            while ((lineF = readerFRONT.readLine()) != null) {
                //System.out.println("generate card " + cardNumber);
                counter++;
                String[] partsF = lineF.split(" ");

                String partsB = readerBACK.readLine();

           
                Map<CardCorners, Symbol> cornerSymbolsF = new HashMap<>();
                int point = 0;
                for(int i = 0; i < 4; i++){
                    cornerSymbolsF.put(CardCorners.values()[i], Symbol.valueOf(partsF[i]));
                }

                Symbol kingdom;
                switch(cardNumber / 10){
                    case 0:
                        kingdom = Symbol.FUNGUS;
                        if(cardNumber%10 > 6){
                            point = 1;
                        }
                        break;
                    case 1:
                        kingdom = Symbol.PLANT;
                        if(cardNumber%10 > 6){
                            point = 1;
                        }
                        break;
                    case 2:
                        kingdom = Symbol.ANIMAL;
                        if(cardNumber%10 > 6){
                            point = 1;
                        }
                        break;
                    case 3:
                        kingdom = Symbol.BUG;
                        if(cardNumber%10 > 6){point = 1;}
                        break;
                    default:
                        kingdom = Symbol.NONE;
                        break;
                }
                ResourceFrontFace frontFace = new ResourceFrontFace("front-" + counter + ".jpeg", cornerSymbolsF, point, kingdom);
            
            
                List<Symbol> centerSymbolsB = new ArrayList<>();
                centerSymbolsB.add(Symbol.valueOf(partsB));
            

                RegularBackFace backFace = new RegularBackFace("back-" + counter + ".jpeg", centerSymbolsB);
            
                ResourceCard card = new ResourceCard(frontFace, backFace, counter + ".jpeg");
                this.cards.add(card);
                cardNumber++;
            }
        } catch (Exception e) {
            System.err.println("An error occurred while generating starting cards");
        }

    }

}
