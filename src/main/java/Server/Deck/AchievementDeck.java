package Server.Deck;

import Server.Card.AchievementCard;
import Server.Card.AchievementFrontFace;
import Server.Card.Card;
import Server.Card.EmptyCardFace;
import Server.Enums.DeckPosition;
import Server.Enums.Symbol;
import Server.Exception.AlreadyFinishedException;
import Server.Exception.IncorrectDeckPositionException;

import java.io.*;
import java.util.*;

import static Server.Enums.DeckPosition.*;

/**
 * Class for the Achievement Deck
 * In this deck, the board cards are the common achievements
 */
public class AchievementDeck implements Deckable{
    private final List<AchievementCard> cards;
    private final Map<DeckPosition, AchievementCard> boardCards;

    /**
     * Constructor. Creates the cards, Shuffles them and moves the first two (the common achievements) to the board
     */
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
            System.err.println("Error while moving cards to the board (AchievementDeck)");
        }
    }

    /**
     * Debug constructor
     * @param ignoredTest gets ignored. Only there to differentiate the two constructors
     */
    public AchievementDeck(Boolean ignoredTest){
        this.boardCards = new HashMap<>();
        this.cards = new ArrayList<>();
        boardCards.put(FIRST_CARD, null);
        boardCards.put(SECOND_CARD, null);
        createCards();
        try {
            moveCardToBoard(FIRST_CARD);
            moveCardToBoard(SECOND_CARD);
        } catch (Exception e) {
            System.err.println("Error while moving cards to the board (AchievementDeck)");
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
     * Moves a card from the deck to the board. This is used to set the common achievements.
     * @param where_to the position to add the card to
     * @throws IncorrectDeckPositionException if the position is not in the board (i.e. the deck itself)
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
     * Gets the board cards (common achievements)
     * @return a map linking board positions to the cards
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
        InputStream fileFRONT;
        
        BufferedReader readerFRONT;
        

        try {
            fileFRONT = getClass().getResourceAsStream("/images/AchievementFrontFace.txt");

            assert fileFRONT != null;
            readerFRONT = new BufferedReader(new InputStreamReader(fileFRONT));
            
        
        

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
            
                AchievementFrontFace frontFace = new AchievementFrontFace("front-" + counter + ".jpeg", scoreRequirements, score);
            
                EmptyCardFace backFace = new EmptyCardFace("back-" + counter + ".jpeg");
            
                AchievementCard card = new AchievementCard(frontFace, backFace);
                this.cards.add(card);
            }
        } catch (Exception e) {
            System.err.println("An error occurred while generating cards (AchievementDeck)");
        }
            


    }
    @Override
    public AchievementCard popCard(DeckPosition position) throws AlreadyFinishedException {
        if(position == DECK){
            if(cards.isEmpty())
                throw new AlreadyFinishedException("Achievement Deck is empty");
            return cards.removeFirst();
        } else {
            if (boardCards.get(position) == null) {
                throw new AlreadyFinishedException("No card in the specified position");
            }
            return getBoardCard().get(position);
        }
    }


    public boolean isEmpty() {
        return cards.isEmpty();
    }


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
        return cards.getFirst();
    }


    /**
     * Get the number of cards in the deck
     * @return the number of cards in the deck
     */
    public int getNumberOfCards() {
        return cards.size();
    }


}