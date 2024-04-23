package Server.Deck;

import Server.Card.*;
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
import static Server.Enums.DeckPosition.DECK;

public class ResourceDeck implements Deckable {
    protected List<ResourceCard> cards;
    private final Map<DeckPosition, ResourceCard> boardCards;
    public ResourceDeck(){
        this.boardCards = new HashMap<>();
        this.cards = new ArrayList<>();
        boardCards.put(FIRST_CARD, null);
        boardCards.put(SECOND_CARD, null);
        createCards();
        shuffle();
        System.out.println("ResourceDeck");
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
    public ResourceCard popCard(DeckPosition position) throws  AlreadyFinishedException{

            if (position == DECK) {
                return (ResourceCard) cards.remove(0);
            } else {
                if(boardCards.get(position) == null){
                    throw new AlreadyFinishedException("There is no card in the position " + position);
                }
                return (ResourceCard) getBoardCard().get(position);
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
            boardCards.put(position, (ResourceCard) card);
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
            fileFRONT = new File("images/ResourceFrontFace.txt");
            fileBACK = new File("images/ResourceBackFace.txt");
            readerFRONT = new BufferedReader(new FileReader(fileFRONT));
            readerBACK = new BufferedReader(new FileReader(fileBACK));

            String lineF;
            int cardNumber = 0;
            while ((lineF = readerFRONT.readLine()) != null) {
                //System.out.println("generate card " + cardNumber);
                String[] partsF = lineF.split(" ");

                String partsB = readerBACK.readLine();

           
                Map<CardCorners, Symbol> cornerSymbolsF = new HashMap<>();
                int point = 0;
                for(int i = 0; i < 4; i++){
                    //System.out.print(CardCorners.values()[i]);
                    //System.out.println(Symbol.valueOf(partsF[i]));
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
                ResourceFrontFace frontFace = new ResourceFrontFace("RESOURCEFRONT", cornerSymbolsF, point, kingdom);
            
            
                List<Symbol> centerSymbolsB = new ArrayList<>();
                centerSymbolsB.add(Symbol.valueOf(partsB));
            

                RegularBackFace backFace = new RegularBackFace("RESOURCEBACK", centerSymbolsB);
            
                ResourceCard card = new ResourceCard(frontFace, backFace);
                this.cards.add(card);
                cardNumber++;
            }
        } catch (Exception e) {
            System.out.println("An error occurred while generating starting cards");
            e.printStackTrace();
        }

    }

}
