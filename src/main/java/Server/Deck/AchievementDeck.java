package Server.Deck;

import Server.Card.AchievementCard;
import Server.Card.AchievementFrontFace;
import Server.Card.Card;
import Server.Card.EmptyCardFace;
import Server.Enums.DeckPosition;
import Server.Enums.Symbol;
import Server.Exception.AlreadyFinishedException;
import Server.Exception.IncorrectDeckPositionException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

import static Server.Enums.DeckPosition.*;


public class AchievementDeck implements Deckable{
    protected List<AchievementCard> cards;
    private final Map<DeckPosition, AchievementCard> boardCards;

    public AchievementDeck(){
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
    public Map<DeckPosition, AchievementCard> getBoardCard() {
        Map<DeckPosition, AchievementCard> boardCardsToReturn = new HashMap<>();
        for (DeckPosition position : boardCards.keySet()) {
            boardCardsToReturn.put(position, boardCards.get(position));
        }
        return boardCardsToReturn;
    }

    /**
     * generate the cards
     */
    private void createCards() {
        File fileFRONT;
        
        BufferedReader readerFRONT;
        

        try {
            fileFRONT = new File(getClass().getResource("/images/AchievementFrontFace.txt").toURI());
            
            readerFRONT = new BufferedReader(new FileReader(fileFRONT));
            
        
        

            String lineF;
            int counter = 85;
            while ((lineF = readerFRONT.readLine()) != null) {
                counter++;

                String[] partsF = lineF.split(" ");

                Map<Symbol, Integer> scoreRequirements = new HashMap<>();
                for (Symbol s : Symbol.values()) {
                    scoreRequirements.put(s, 0);
                }
                int score = 0;
                for (int i = 0; i < partsF.length; i++) {
                    if (i == partsF.length - 1) {
                        score = Integer.parseInt(partsF[i]);
                    } else {
                        int quantity = Integer.parseInt(partsF[i]);
                        //System.out.println(partsF[i]);
                        scoreRequirements.put(Symbol.valueOf(partsF[i + 1]), quantity);
                        i++;
                    }
                }
            
                AchievementFrontFace frontFace = new AchievementFrontFace(counter + ".jpeg", scoreRequirements, score);
            
                EmptyCardFace backFace = new EmptyCardFace(counter + ".jpeg");
            
                AchievementCard card = new AchievementCard(frontFace, backFace, counter + ".jpeg");
                this.cards.add(card);
            }
        } catch (Exception e) {
            System.out.println("An error occurred while generating cards");
            e.printStackTrace();
        }
            


    }
    @Override
    public AchievementCard popCard(DeckPosition position) throws AlreadyFinishedException {
        if(position == DECK){
            if(cards.isEmpty())
                throw new AlreadyFinishedException("Achievement Deck is empty");
            return (AchievementCard) cards.remove(0);
        } else {
            if (boardCards.get(position) == null) {
                throw new AlreadyFinishedException("No card in the specified position");
            }
            return (AchievementCard) getBoardCard().get(position);
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
            boardCards.put(position, (AchievementCard) card);
    }

    @Override
    public Card getTopCardNoPop() {
        if(cards.isEmpty())
            return null;
        return cards.get(0);
    }

    /**
     * @return int the number of cards in the deck
     */
    public int getNumberOfCards() {
        return cards.size();
    }


}