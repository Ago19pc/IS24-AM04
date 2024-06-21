package Server.Manuscript;

import Server.Card.AchievementCard;
import Server.Card.CornerCardFace;
import Server.Enums.CardCorners;
import Server.Enums.Face;
import Server.Enums.Symbol;

import java.io.Serializable;
import java.util.*;

import static java.lang.Math.abs;
import static java.lang.Math.min;

/**
 * Class that represents the manuscript
 */
public class Manuscript implements Serializable {
    /**
     * The graph of cards in the manuscript
     */
    private final Graph graph;
    /**
     * A map containing for each symbol its corresponding number in the manuscript
     */
    private final Map<Symbol, Integer> activeSymbols;

    /**
     * Constructor. Creates the graph and initializes the active symbols
     * @param cardFace the card face to add to the manuscript
     */
    public Manuscript(CornerCardFace cardFace){
        graph = new Graph(cardFace);
        activeSymbols = new HashMap<>();
        for (Symbol symbol : Symbol.values()) {
            activeSymbols.put(symbol, 0);
        }
        updateSymbolCount(cardFace);
    }

    /**
     * Update the symbol count for the manuscript
     * @param cardFace the card face that will be added
     * @throws IllegalArgumentException if the card is not in the manuscript
     */
    private void updateSymbolCount(CornerCardFace cardFace){
        Map<CardCorners, CornerCardFace> cardsUnder = graph.getCardsUnder(cardFace);
        for(CardCorners corner : cardsUnder.keySet()) {
            CornerCardFace neighbor = cardsUnder.get(corner);
            Symbol symbol = neighbor.getCornerSymbols().get(corner.getOppositeCorner());
            activeSymbols.put(symbol, activeSymbols.get(symbol) - 1);
        }

        for(Symbol symbol : cardFace.getCenterSymbols()){
            activeSymbols.put(symbol, activeSymbols.get(symbol) + 1);
        }
        for(CardCorners corner : cardFace.getCornerSymbols().keySet()){
            Symbol symbol = cardFace.getCornerSymbols().get(corner);
            activeSymbols.put(symbol, activeSymbols.get(symbol) + 1);
        }
    }

    /**
     * Add a card to the manuscript
     * @param xcoordinate where to add the card (x)
     * @param ycoordinate where to add the card (y)
     * @param cardFace which face to add
     * @param turn the current turn
     */
    public void addCard(int xcoordinate, int ycoordinate, CornerCardFace cardFace, int turn){
        cardFace.setXCoord(xcoordinate);
        cardFace.setYCoord(ycoordinate);
        Map<CardCorners, CornerCardFace> positions = new HashMap<>();
        CornerCardFace neighbor = getCardByCoord(xcoordinate-1, ycoordinate+1);
        positions.put(CardCorners.TOP_LEFT, neighbor);
        neighbor = getCardByCoord(xcoordinate+1, ycoordinate+1);
        positions.put(CardCorners.TOP_RIGHT, neighbor);
        neighbor = getCardByCoord(xcoordinate-1, ycoordinate-1);
        positions.put(CardCorners.BOTTOM_LEFT, neighbor);
        neighbor = getCardByCoord(xcoordinate+1, ycoordinate-1);
        positions.put(CardCorners.BOTTOM_RIGHT, neighbor);
        this.graph.addCard(cardFace, positions, turn);
        updateSymbolCount(cardFace);
    }
    /**
     * get the card at the specified coordinates
     * @param x the x coordinate
     * @param y the y coordinate
     * @return CornerCardFace | null the card at the specified coordinates
     */
    public CornerCardFace getCardByCoord(int x, int y){
        for(CornerCardFace card : this.graph.getAllCards()){
            if(card.getXCoord() == x && card.getYCoord() == y){
                return card;
            }
        }
        return null;
    }

    /**
     * Get the active number of a symbol in the manuscript
     * @param symbol the symbol to get the count of
     * @return the number of a specific symbol in the manuscript
     */
    public int getSymbolCount(Symbol symbol) {
        return this.activeSymbols.get(symbol);
    }
    /**
     * Calculates the points given by an achievement card
     * @param achievementCard the achievement card to calculate points for
     * @return the points given by the achievement card
     */
    public int calculatePoints(AchievementCard achievementCard) {
        Map<Symbol, Integer> scoreRequirements = achievementCard.getFace(Face.FRONT).getScoreRequirements();
        List<Symbol> requiredSymbols = new ArrayList<>();
        for(Symbol symbol : scoreRequirements.keySet()){
            if(scoreRequirements.get(symbol) != 0){
                requiredSymbols.add(symbol);
            }
        }
        int points = 0;
        if(requiredSymbols.size() == 1){
            if(requiredSymbols.contains(Symbol.QUILL)){
                int quillCount = activeSymbols.get(Symbol.QUILL);
                points += quillCount/2 * 2;
            }
            if(requiredSymbols.contains(Symbol.PARCHMENT)){
                int parchmentCount = activeSymbols.get(Symbol.PARCHMENT);
                points += parchmentCount/2 * 2;
            }
            if(requiredSymbols.contains(Symbol.BOTTLE)){
                int bottleCount = activeSymbols.get(Symbol.BOTTLE);
                points += bottleCount/2 * 2;
            }
            if(requiredSymbols.contains(Symbol.FUNGUS)){
                int fungusCount = activeSymbols.get(Symbol.FUNGUS);
                points += fungusCount/3 * 2;
            }
            if(requiredSymbols.contains(Symbol.ANIMAL)){
                int animalCount = activeSymbols.get(Symbol.ANIMAL);
                points += animalCount/3 * 2;
            }
            if(requiredSymbols.contains(Symbol.PLANT)){
                int plantCount = activeSymbols.get(Symbol.PLANT);
                points += plantCount/3 * 2;
            }
            if(requiredSymbols.contains(Symbol.BUG)){
                int bugCount = activeSymbols.get(Symbol.BUG);
                points += bugCount/3 * 2;
            }
            if(requiredSymbols.getFirst().isPattern()){ //symbol is a pattern
                List<CornerCardFace> usedCards = new LinkedList<>();
                switch (requiredSymbols.getFirst()){ //probably we can optimize these
                    case PATTERN1F -> {
                        for(CornerCardFace card : graph.getAllCards()){
                            try{
                                if(card.getKingdom() == Symbol.FUNGUS && !usedCards.contains(card)){
                                    CornerCardFace secondCard = getCardByCoord(card.getXCoord() + 1, card.getYCoord() + 1);
                                    if(secondCard != null && secondCard.getKingdom() == Symbol.FUNGUS && !usedCards.contains(secondCard)){
                                        CornerCardFace thirdCard = getCardByCoord(card.getXCoord() + 2, card.getYCoord() + 2);
                                        if(thirdCard != null && thirdCard.getKingdom() == Symbol.FUNGUS && !usedCards.contains(thirdCard)){
                                            usedCards.add(card);
                                            usedCards.add(secondCard);
                                            usedCards.add(thirdCard);
                                            points += 2;
                                        }
                                    }
                                }
                            } catch (UnsupportedOperationException e){
                                //do nothing and skip over the card
                            }
                        }
                    }
                    case PATTERN1A -> {
                        for(CornerCardFace card : graph.getAllCards()){
                            try{
                                if(card.getKingdom() == Symbol.ANIMAL && !usedCards.contains(card)){
                                    CornerCardFace secondCard = getCardByCoord(card.getXCoord() + 1, card.getYCoord() + 1);
                                    if(secondCard != null && secondCard.getKingdom() == Symbol.ANIMAL && !usedCards.contains(secondCard)){
                                        CornerCardFace thirdCard = getCardByCoord(card.getXCoord() + 2, card.getYCoord() + 2);
                                        if(thirdCard != null && thirdCard.getKingdom() == Symbol.ANIMAL && !usedCards.contains(thirdCard)){
                                            usedCards.add(card);
                                            usedCards.add(secondCard);
                                            usedCards.add(thirdCard);
                                            points += 2;
                                        }
                                    }
                                }
                            } catch (UnsupportedOperationException e){
                                //do nothing and skip over the card
                            }
                        }
                    }
                    case PATTERN2P -> {
                        for(CornerCardFace card : graph.getAllCards()){
                            try{
                                if(card.getKingdom() == Symbol.PLANT && !usedCards.contains(card)){
                                    CornerCardFace secondCard = getCardByCoord(card.getXCoord() + 1, card.getYCoord() - 1);
                                    if(secondCard != null && secondCard.getKingdom() == Symbol.PLANT && !usedCards.contains(secondCard)){
                                        CornerCardFace thirdCard = getCardByCoord(card.getXCoord() + 2, card.getYCoord() - 2);
                                        if(thirdCard != null && thirdCard.getKingdom() == Symbol.PLANT && !usedCards.contains(thirdCard)){
                                            usedCards.add(card);
                                            usedCards.add(secondCard);
                                            usedCards.add(thirdCard);
                                            points += 2;
                                        }
                                    }
                                }
                            } catch (UnsupportedOperationException e){
                                //do nothing and skip over the card
                            }
                        }
                    }
                    case PATTERN2B -> {
                        for(CornerCardFace card : graph.getAllCards()){
                            try{
                                if(card.getKingdom() == Symbol.BUG&& !usedCards.contains(card)){
                                    CornerCardFace secondCard = getCardByCoord(card.getXCoord() + 1, card.getYCoord() - 1);
                                    if(secondCard != null && secondCard.getKingdom() == Symbol.BUG && !usedCards.contains(secondCard)){
                                        CornerCardFace thirdCard = getCardByCoord(card.getXCoord() + 2, card.getYCoord() - 2);
                                        if(thirdCard != null && thirdCard.getKingdom() == Symbol.BUG && !usedCards.contains(thirdCard)){
                                            usedCards.add(card);
                                            usedCards.add(secondCard);
                                            usedCards.add(thirdCard);
                                            points += 2;
                                        }
                                    }
                                }
                            } catch (UnsupportedOperationException e){
                                //do nothing and skip over the card
                            }
                        }
                    }
                    case PATTERN3 -> {
                        for(CornerCardFace card : graph.getAllCards()){
                            try{
                                if(card.getKingdom() == Symbol.FUNGUS && !usedCards.contains(card)){
                                    CornerCardFace secondCard = getCardByCoord(card.getXCoord(), card.getYCoord() - 2);
                                    if(secondCard != null && secondCard.getKingdom() == Symbol.FUNGUS && !usedCards.contains(secondCard)){
                                        CornerCardFace thirdCard = getCardByCoord(card.getXCoord() + 1, card.getYCoord() - 3);
                                        if(thirdCard != null && thirdCard.getKingdom() == Symbol.PLANT && !usedCards.contains(thirdCard)){
                                            usedCards.add(card);
                                            usedCards.add(secondCard);
                                            usedCards.add(thirdCard);
                                            points += 3;
                                        }
                                    }
                                }
                            } catch (UnsupportedOperationException e){
                                //do nothing and skip over the card
                            }
                        }
                    }
                    case PATTERN4 -> {
                        for(CornerCardFace card : graph.getAllCards()){
                            try{
                                if(card.getKingdom() == Symbol.PLANT && !usedCards.contains(card)){
                                    CornerCardFace secondCard = getCardByCoord(card.getXCoord(), card.getYCoord() - 2);
                                    if(secondCard != null && secondCard.getKingdom() == Symbol.PLANT && !usedCards.contains(secondCard)){
                                        CornerCardFace thirdCard = getCardByCoord(card.getXCoord() - 1, card.getYCoord() - 3);
                                        if(thirdCard != null && thirdCard.getKingdom() == Symbol.BUG && !usedCards.contains(thirdCard)){
                                            usedCards.add(card);
                                            usedCards.add(secondCard);
                                            usedCards.add(thirdCard);
                                            points += 3;
                                        }
                                    }
                                }
                            } catch (UnsupportedOperationException e){
                                //do nothing and skip over the card
                            }
                        }
                    }
                    case PATTERN5 -> {
                        for(CornerCardFace card : graph.getAllCards()){
                            try{
                                if(card.getKingdom() == Symbol.ANIMAL && !usedCards.contains(card)){
                                    CornerCardFace secondCard = getCardByCoord(card.getXCoord(), card.getYCoord() + 2);
                                    if(secondCard != null && secondCard.getKingdom() == Symbol.ANIMAL && !usedCards.contains(secondCard)){
                                        CornerCardFace thirdCard = getCardByCoord(card.getXCoord() + 1, card.getYCoord() + 3);
                                        if(thirdCard != null && thirdCard.getKingdom() == Symbol.FUNGUS && !usedCards.contains(thirdCard)){
                                            usedCards.add(card);
                                            usedCards.add(secondCard);
                                            usedCards.add(thirdCard);
                                            points += 3;
                                        }
                                    }
                                }
                            } catch (UnsupportedOperationException e){
                                //do nothing and skip over the card
                            }
                        }
                    }
                    case PATTERN6 -> {
                        for(CornerCardFace card : graph.getAllCards()){
                            try{
                                if(card.getKingdom() == Symbol.BUG && !usedCards.contains(card)){
                                    CornerCardFace secondCard = getCardByCoord(card.getXCoord(), card.getYCoord() + 2);
                                    if(secondCard != null && secondCard.getKingdom() == Symbol.BUG && !usedCards.contains(secondCard)){
                                        CornerCardFace thirdCard = getCardByCoord(card.getXCoord() - 1, card.getYCoord() + 3);
                                        if(thirdCard != null && thirdCard.getKingdom() == Symbol.ANIMAL && !usedCards.contains(thirdCard)){
                                            usedCards.add(card);
                                            usedCards.add(secondCard);
                                            usedCards.add(thirdCard);
                                            points += 3;
                                        }
                                    }
                                }
                            } catch (UnsupportedOperationException e){
                                //do nothing and skip over the card
                            }
                        }
                    }
                }
            }
        }
        else if(requiredSymbols.size() == 3){
            int quillCount = activeSymbols.get(Symbol.QUILL);
            int parchmentCount = activeSymbols.get(Symbol.PARCHMENT);
            int bottleCount = activeSymbols.get(Symbol.BOTTLE);
            int minimum = min(quillCount, min(parchmentCount, bottleCount));
            if(minimum > 0){
                points += minimum * 3;
            }
        }
        return points;
    }

    /**
     * Get all cards under a certain card
     * @param cardFace the card face to get the cards under
     * @return the cards under the card
     * @throws IllegalArgumentException if the card is not in the manuscript
     */
    public Map<CardCorners, CornerCardFace> getCardsUnder(CornerCardFace cardFace) throws IllegalArgumentException{
        return this.graph.getCardsUnder(cardFace);
    }

    /**
     * Checks whether a card is placeable in a certain position
     * @param x the x coordinate
     * @param y the y coordinate
     * @param cardFace the card face to place
     * @return boolean true if the card is placeable, false otherwise
     */
    public boolean isPlaceable(int x, int y, CornerCardFace cardFace){
        //The coordinates must be valid: both even or both odd. This solves the problem where a card is adjacient to another one on more than one corner
        if((abs(x)+abs(y)) % 2 != 0){
            return false;
        }
        //A card cannot be placed over another card
        if(getCardByCoord(x, y) != null){
            return false;
        }
        //A card cannot be placed over a "NONE" symbol
        Map<CardCorners, CornerCardFace> neighbors = new HashMap<>();
        if(getCardByCoord(x-1, y+1) != null)
            neighbors.put(CardCorners.TOP_LEFT, getCardByCoord(x-1, y+1));
        if(getCardByCoord(x+1, y+1) != null)
            neighbors.put(CardCorners.TOP_RIGHT, getCardByCoord(x+1, y+1));
        if(getCardByCoord(x-1, y-1) != null)
            neighbors.put(CardCorners.BOTTOM_LEFT, getCardByCoord(x-1, y-1));
        if(getCardByCoord(x+1, y-1) != null)
            neighbors.put(CardCorners.BOTTOM_RIGHT, getCardByCoord(x+1, y-1));
        //l'errore è da qualche parte nel for loop, bisogna fare il nullpointerexception per getCardByCoord


        // NEW:
        // If is all null then is misplaced!
        if (neighbors.values().stream().allMatch(Objects::isNull)) return false;

        for(CardCorners corner : neighbors.keySet()){
            if(neighbors.get(corner).getCornerSymbols().get(corner.getOppositeCorner()) == Symbol.NONE){
                System.out.println(neighbors.get(corner).getCornerSymbols() + " Return false in isPlaceble with corner" + corner + " and oppositeCorner " + corner.getOppositeCorner() );
                return false;
            }
        }
        //If the face has placement requirements, they need to be met
        try{
            Map<Symbol, Integer> placementRequirements = cardFace.getPlacementRequirements();
            for(Symbol requiredSymbol : placementRequirements.keySet()){
                int requiredCount = placementRequirements.get(requiredSymbol);
                int actualCount = getSymbolCount(requiredSymbol);
                if(actualCount < requiredCount){
                    return false;
                }
            }
        } catch (UnsupportedOperationException e){ //if the card does not have placement requirements
            //do nothing
        }
        return true;
    }

    /**
     * Get all cards in the manuscript
     * @return the cards in the manuscript, as a list of CornerCardFace
     */
    public List<CornerCardFace> getAllCards() {
        return graph.getAllCards();
    }
}
