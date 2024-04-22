package Server.Deck;

import Server.Card.*;
import Server.Enums.CardCorners;
import Server.Enums.DeckPosition;
import Server.Enums.Symbol;
import Server.Exception.IncorrectDeckPositionException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

import static Server.Enums.DeckPosition.*;
import static Server.Enums.DeckPosition.DECK;

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
        System.out.println("GoldDeck");
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
        System.out.println(this.toString() + "shuffle");
    }

    /**
     * @param where_to the position to add the card to
     */
    public void moveCardToBoard(DeckPosition where_to) throws IncorrectDeckPositionException {
        if (where_to == DECK)
            throw new IncorrectDeckPositionException("Cannot add card to the deck, only to FIST_CARD or SECOND_CARD.");
        else
            System.out.println(this.toString() + "moveCardToBoard");
        Card cardToMove = popCard(DECK);
        addCard(cardToMove, where_to);
    }

    /**
     * @return Card the card from the position
     */
    public Map<DeckPosition, Card> getBoardCard() {
        Map<DeckPosition, Card> boardCardsToReturn = new HashMap<>();
        for (DeckPosition position : boardCards.keySet()) {
            boardCardsToReturn.put(position, (Card) boardCards.get(position));
        }
        return boardCardsToReturn;
    }

    /**
     * @param position the position pop the card from
     * @return Card the popped card
     */
    public GoldCard popCard(DeckPosition position){
        try {
            if (position == DECK) {
                return cards.remove(0);
            } else {
                GoldCard drawnCard = boardCards.remove(position);
                moveCardToBoard(position);
                return drawnCard;
            }
        } catch (IncorrectDeckPositionException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @return boolean true if the deck is empty
     */
    public boolean isEmpty() {
        return cards.isEmpty();
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
            fileFRONT = new File("images/GoldFrontFace.txt");
            fileBACK = new File("images/GoldBackFace.txt");
            readerFRONT = new BufferedReader(new FileReader(fileFRONT));
            readerBACK = new BufferedReader(new FileReader(fileBACK));



            String lineF;
            int cardNumber = 0;
            while ((lineF = readerFRONT.readLine()) != null) {
                System.out.println("generating card " + cardNumber);

                String[] partsF = lineF.split(" ");
                //System.out.println(lineF);

                String partsB = readerBACK.readLine();
                //System.out.println(partsB);



                Map<CardCorners, Symbol> cornerSymbolsF = new HashMap<>();

                Map<Symbol, Integer> scoreRequirementsF = new HashMap<>();
                for (Symbol s: Symbol.values()) {
                    scoreRequirementsF.put(s, 0);
                }

                Map<Symbol, Integer> placementRequirementsF = new HashMap<>();
                for (Symbol s: Symbol.values()) {
                    placementRequirementsF.put(s, 0);
                }




                int point = 0;
                for (int i = 0; i < partsF.length; i++) {

                    if (i < 4) {
                        System.out.println("corner: " + CardCorners.values()[i] + " symbol: " + Symbol.valueOf(partsF[i]));
                        cornerSymbolsF.put(CardCorners.values()[i], Symbol.valueOf(partsF[i]));
                    }
                    else if (i ==  partsF.length - 1) point = Integer.parseInt(partsF[i]);
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
                GoldFrontFace frontFace = new GoldFrontFace("GOLDFRONT", cornerSymbolsF, point, placementRequirementsF, scoreRequirementsF, kingdom);

                // DA QUI E DA VEDERE
                List<Symbol> centerSymbolsB = new ArrayList<>();
                centerSymbolsB.add(Symbol.valueOf(partsB));


                RegularBackFace backFace = new RegularBackFace("GOLDBACK", centerSymbolsB);

                GoldCard card = new GoldCard(frontFace, backFace);
                this.cards.add(card);
                cardNumber++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*This is to print to check if the cards are generated correctly
         for(Card card : this.cards) {
             System.out.println("Front Corner Symbols:");
             card.getFace(FRONT).getCornerSymbols().forEach((key, value) -> System.out.println(value));
             System.out.print("Front Points:");
             System.out.println(card.getFace(FRONT).getScore());
             System.out.println("Front Placement Requirements:");
             card.getFace(FRONT).getPlacementRequirements().forEach((key, value) -> {if (value> 0) System.out.println(key + " " + value);});
             System.out.println("Front Score Requirements:");
             card.getFace(FRONT).getScoreRequirements().forEach((key, value) -> {if (value> 0) System.out.println(key + " " + value);});

             System.out.println("Back Center Symbols:");
             card.getFace(BACK).getCenterSymbols().forEach(System.out::println);
             System.out.println();
            }
        //*/


    }
}
