package Server.Manuscript;

import Server.Card.AchievementCard;
import Server.Card.CornerCardFace;
import Server.Enums.CardCorners;
import Server.Enums.Face;
import Server.Enums.Symbol;

import java.io.Serializable;
import java.util.*;

import static java.lang.Math.abs;

public class Manuscript implements Serializable {
    private final Graph graph;
    private final Map<Symbol, Integer> activeSymbols;


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
     */
    private void updateSymbolCount(CornerCardFace cardFace) throws IllegalArgumentException{
        Map<CardCorners, CornerCardFace> cardsUnder = graph.getCardsUnder(cardFace);
        try {
            for(CardCorners corner : cardsUnder.keySet()) {
                CornerCardFace neighbor = cardsUnder.get(corner);
                Symbol symbol = neighbor.getCornerSymbols().get(corner.getOppositeCorner());
                activeSymbols.put(symbol, activeSymbols.get(symbol) - 1);
            }

            for(Symbol symbol : cardFace.getCenterSymbols()){
                activeSymbols.put(symbol, activeSymbols.get(symbol) + 1);
            }
        } catch (Exception e){
            //do nothing
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
     * @param turn the turn the card was placed
     */
    public void addCard(int xcoordinate, int ycoordinate, CornerCardFace cardFace, int turn) throws IllegalArgumentException{
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
     * @return GraphNode the root of the manuscript
     */
    public int getSymbolCount(Symbol symbol) {
        return this.activeSymbols.get(symbol);
    }
    /**
     * Calculates the points given by an achievement card
     * @param achievementCard the achievement card to calculate points for
     * @return int the points given by the achievement card
     */
    public int calculatePoints(AchievementCard achievementCard) {
        Map<Symbol, Integer> scoreRequirements = achievementCard.getFace(Face.FRONT).getScoreRequirements();
        int points = 0;
        int symbolCount = scoreRequirements.keySet().toArray().length; //symbolcount can be either 1 or 3.
        if(symbolCount == 3){ //3 points for each set of the 3 symbols
            int leastCommonSymbolCount = activeSymbols.entrySet().stream().filter(entry -> scoreRequirements.containsKey(entry.getKey()))
                                                    .min(Map.Entry.comparingByValue()).get().getValue();
            points = leastCommonSymbolCount * 3;
        }
        if(symbolCount == 1){
            Symbol symbol = scoreRequirements.keySet().stream().findFirst().get();
            if(symbol.isPattern()){ //symbol is a pattern
                int patternCount = 0;
                List<CornerCardFace> usedCards = new LinkedList<>();
                switch (symbol){ //probably we can optimize these
                    case PATTERN1F -> {
                        for(CornerCardFace card : graph.getAllCards()){
                            try{
                                if(card.getKingdom() == Symbol.FUNGUS && !usedCards.contains(card)){
                                    CornerCardFace secondCard = getCardByCoord(card.getXCoord() + 1, card.getYCoord() + 1);
                                    if(secondCard != null && secondCard.getKingdom() == Symbol.FUNGUS && !usedCards.contains(secondCard)){
                                        CornerCardFace thirdCard = getCardByCoord(card.getXCoord() + 2, card.getYCoord() + 2);
                                        if(thirdCard != null && thirdCard.getKingdom() == Symbol.FUNGUS && !usedCards.contains(thirdCard)){
                                            patternCount++;
                                            usedCards.add(card);
                                            usedCards.add(secondCard);
                                            usedCards.add(thirdCard);
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
                                            patternCount++;
                                            usedCards.add(card);
                                            usedCards.add(secondCard);
                                            usedCards.add(thirdCard);
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
                                            patternCount++;
                                            usedCards.add(card);
                                            usedCards.add(secondCard);
                                            usedCards.add(thirdCard);
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
                                            patternCount++;
                                            usedCards.add(card);
                                            usedCards.add(secondCard);
                                            usedCards.add(thirdCard);
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
                                            patternCount++;
                                            usedCards.add(card);
                                            usedCards.add(secondCard);
                                            usedCards.add(thirdCard);
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
                                            patternCount++;
                                            usedCards.add(card);
                                            usedCards.add(secondCard);
                                            usedCards.add(thirdCard);
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
                                            patternCount++;
                                            usedCards.add(card);
                                            usedCards.add(secondCard);
                                            usedCards.add(thirdCard);
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
                                            patternCount++;
                                            usedCards.add(card);
                                            usedCards.add(secondCard);
                                            usedCards.add(thirdCard);
                                        }
                                    }
                                }
                            } catch (UnsupportedOperationException e){
                                //do nothing and skip over the card
                            }
                        }
                    }
                }
                int pointsGained = scoreRequirements.get(symbol);
                return patternCount * pointsGained;
            } else {
                int requiredSymbolCount = scoreRequirements.get(symbol); //can either be 2 or 3. 2 points for each set of 2 symbols or 2 points for each set of 3 symbols
                int actualSymbolCount = activeSymbols.get(symbol);
                points = actualSymbolCount / requiredSymbolCount * 2;
            }
        }
        return points;
    }

    /**
     * Get all cards under a certain card
     * @param cardFace the card face to get the cards under
     * @return Map<CardCorners, CornerCardFace> the cards under the card
     * @throws IllegalArgumentException if the card is not in the manuscript
     */
    public Map<CardCorners, CornerCardFace> getCardsUnder(CornerCardFace cardFace) throws IllegalArgumentException{
        return this.graph.getCardsUnder(cardFace);
    }

    public Map<CardCorners, CornerCardFace> getNeighbors(int x, int y){
        Map<CardCorners, CornerCardFace> neighbors = new HashMap<>();
        neighbors.put(CardCorners.TOP_LEFT, getCardByCoord(x-1, y+1));
        neighbors.put(CardCorners.TOP_RIGHT, getCardByCoord(x+1, y+1));
        neighbors.put(CardCorners.BOTTOM_LEFT, getCardByCoord(x-1, y-1));
        neighbors.put(CardCorners.BOTTOM_RIGHT, getCardByCoord(x+1, y-1));
        return neighbors;
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

    public List<CornerCardFace> getAllCards() {
        return graph.getAllCards();
    }
}
