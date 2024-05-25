package Server.Deck;

import Server.Card.Card;
import Server.Card.GoldCard;
import Server.Card.GoldFrontFace;
import Server.Card.RegularBackFace;
import Server.Enums.CardCorners;
import Server.Enums.DeckPosition;
import Server.Enums.Symbol;
import Server.Exception.AlreadyFinishedException;
import Server.Exception.IncorrectDeckPositionException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

import static Server.Enums.DeckPosition.*;

public class GoldDeck implements Deckable {
    protected List<GoldCard> cards;
    private final Map<DeckPosition, GoldCard> boardCards;
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
            e.printStackTrace();
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
     * @param where_to the position to add the card to
     */
    public void moveCardToBoard(DeckPosition where_to) throws IncorrectDeckPositionException {
        if (where_to == DECK)
            throw new IncorrectDeckPositionException("Cannot add card to the deck, only to FIST_CARD or SECOND_CARD.");


        Card cardToMove;
        try{
            cardToMove = popCard(DECK);
        } catch (AlreadyFinishedException e) {
            cardToMove = null;
        }
        addCard(cardToMove, where_to);
    }

    /**
     * @return Card the card from the position
     */
    public Map<DeckPosition, GoldCard> getBoardCard() {
        Map<DeckPosition, GoldCard> boardCardsToReturn = new HashMap<>();
        for (DeckPosition position : boardCards.keySet()) {
            boardCardsToReturn.put(position, boardCards.get(position));
        }
        return boardCardsToReturn;
    }

    /**
     * @param position the position pop the card from
     * @return Card the popped card
     */
    public GoldCard popCard(DeckPosition position) throws AlreadyFinishedException{

            if (position == DECK) {
                if(cards.isEmpty())
                    throw new AlreadyFinishedException("The GoldDeck is empty");
                return (GoldCard) cards.remove(0);
            } else {
                if(boardCards.get(position) == null){
                    throw new AlreadyFinishedException("No card in the position");
                }
                return (GoldCard) getBoardCard().get(position);
            }

    }

    /**
     * @return boolean true if the deck is empty and there are no board cards
     */
    public boolean isEmpty() {
        return cards.isEmpty() && boardCards.get(FIRST_CARD) == null && boardCards.get(SECOND_CARD) == null;
    }

    /**
     * Add card to the board in the specified position
     * @param card card to add
     * @param position position to add the card to
     */
    public void addCard(Card card, DeckPosition position) throws IncorrectDeckPositionException {
        if (position == DECK)
            throw new IncorrectDeckPositionException("Cannot add card to the deck, only to FIST_CARD or SECOND_CARD.");
        else
            boardCards.put(position, (GoldCard) card);
    }

    /**
     * @return int the number of cards in the deck
     */
    public int getNumberOfCards() {
        return cards.size();
    }

    /**
     * generate the cards
     */
    private void createCards() {
        File fileFRONT;
        File fileBACK;
        BufferedReader readerFRONT;
        BufferedReader readerBACK;

        try {
            fileFRONT = new File(getClass().getResource("/images/GoldFrontFace.txt").toURI());
            fileBACK = new File(getClass().getResource("/images/GoldBackFace.txt").toURI());
            readerFRONT = new BufferedReader(new FileReader(fileFRONT));
            readerBACK = new BufferedReader(new FileReader(fileBACK));



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
                Symbol kingdom;
                switch (cardNumber / 10) {
                    case 0:
                        kingdom = Symbol.FUNGUS;
                        break;
                    case 1:
                        kingdom = Symbol.PLANT;
                        break;
                    case 2:
                        kingdom = Symbol.ANIMAL;
                        break;
                    case 3:
                        kingdom = Symbol.BUG;
                        break;
                    default:
                        kingdom = Symbol.NONE;
                        break;
                }
                /*for (int j = 0; j < 4; j++) {
                    System.out.println("corner: " + CardCorners.values()[j] + " symbol: " + cornerSymbolsF.get(CardCorners.values()[j]) + " Kingdom: "+kingdom );
                }*/

                GoldFrontFace frontFace = new GoldFrontFace(counter + ".jpeg", cornerSymbolsF, point, placementRequirementsF, scoreRequirementsF, kingdom);

                // DA QUI E DA VEDERE
                List<Symbol> centerSymbolsB = new ArrayList<>();
                centerSymbolsB.add(Symbol.valueOf(partsB));


                RegularBackFace backFace = new RegularBackFace(counter + ".jpeg", centerSymbolsB);

                GoldCard card = new GoldCard(frontFace, backFace,counter + ".jpeg");
                this.cards.add(card);
                cardNumber++;
            }
        } catch (Exception e) {
            System.out.println("An error occurred while generating cards");

            e.printStackTrace();
        }




    }
}
