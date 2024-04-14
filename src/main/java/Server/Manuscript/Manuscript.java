package Server.Manuscript;

import Server.Card.AchievementCard;
import Server.Card.CornerCardFace;
import Server.Card.StartingCard;
import Server.Enums.CardCorners;
import Server.Enums.Symbol;
import Server.Enums.Face;


import java.util.HashMap;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Manuscript {
    private final Graph graph;
    private Map<Symbol, Integer> activeSymbols;


    public Manuscript(StartingCard card, Face face){
        CornerCardFace cardFace = card.getCornerFace(face);
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
    private void updateSymbolCount(CornerCardFace cardFace){
        System.out.println(" updateSymbolCount");
        Map<CardCorners, CornerCardFace> cardsUnder = graph.getCardsUnder(cardFace);
        for(CardCorners corner : cardsUnder.keySet()){
            CornerCardFace neighbor = cardsUnder.get(corner);
            Symbol symbol = neighbor.getCornerSymbols().get(corner.getOppositeCorner());
            activeSymbols.put(symbol, activeSymbols.get(symbol) - 1);
        }
        try {
            for(Symbol symbol : cardFace.getCenterSymbols()){
                activeSymbols.put(symbol, activeSymbols.get(symbol) + 1);
            }
        } catch (UnsupportedOperationException e){
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
    public void addCard(int xcoordinate, int ycoordinate, CornerCardFace cardFace, int turn){
        cardFace.setXCoord(xcoordinate);
        cardFace.setYCoord(ycoordinate);
        Map<CardCorners, CornerCardFace> positions = new HashMap<>();
        CornerCardFace neighbor = this.graph.getCardByCoord(xcoordinate-1, ycoordinate+1);
        positions.put(CardCorners.TOP_LEFT, neighbor);
        neighbor = this.graph.getCardByCoord(xcoordinate+1, ycoordinate+1);
        positions.put(CardCorners.TOP_RIGHT, neighbor);
        neighbor = this.graph.getCardByCoord(xcoordinate-1, ycoordinate-1);
        positions.put(CardCorners.BOTTOM_LEFT, neighbor);
        neighbor = this.graph.getCardByCoord(xcoordinate+1, ycoordinate-1);
        positions.put(CardCorners.BOTTOM_RIGHT, neighbor);
        this.graph.addCard(cardFace, positions, turn);
        updateSymbolCount(cardFace);
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
                                    CornerCardFace secondCard = graph.getCardByCoord(card.getXCoord() + 1, card.getYCoord() + 1);
                                    if(secondCard != null && secondCard.getKingdom() == Symbol.FUNGUS && !usedCards.contains(secondCard)){
                                        CornerCardFace thirdCard = graph.getCardByCoord(card.getXCoord() + 2, card.getYCoord() + 2);
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
                                    CornerCardFace secondCard = graph.getCardByCoord(card.getXCoord() + 1, card.getYCoord() + 1);
                                    if(secondCard != null && secondCard.getKingdom() == Symbol.ANIMAL && !usedCards.contains(secondCard)){
                                        CornerCardFace thirdCard = graph.getCardByCoord(card.getXCoord() + 2, card.getYCoord() + 2);
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
                                    CornerCardFace secondCard = graph.getCardByCoord(card.getXCoord() + 1, card.getYCoord() - 1);
                                    if(secondCard != null && secondCard.getKingdom() == Symbol.PLANT && !usedCards.contains(secondCard)){
                                        CornerCardFace thirdCard = graph.getCardByCoord(card.getXCoord() + 2, card.getYCoord() - 2);
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
                                    CornerCardFace secondCard = graph.getCardByCoord(card.getXCoord() + 1, card.getYCoord() - 1);
                                    if(secondCard != null && secondCard.getKingdom() == Symbol.BUG && !usedCards.contains(secondCard)){
                                        CornerCardFace thirdCard = graph.getCardByCoord(card.getXCoord() + 2, card.getYCoord() - 2);
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
                                    CornerCardFace secondCard = graph.getCardByCoord(card.getXCoord(), card.getYCoord() - 2);
                                    if(secondCard != null && secondCard.getKingdom() == Symbol.FUNGUS && !usedCards.contains(secondCard)){
                                        CornerCardFace thirdCard = graph.getCardByCoord(card.getXCoord() + 1, card.getYCoord() - 3);
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
                                    CornerCardFace secondCard = graph.getCardByCoord(card.getXCoord(), card.getYCoord() - 2);
                                    if(secondCard != null && secondCard.getKingdom() == Symbol.PLANT && !usedCards.contains(secondCard)){
                                        CornerCardFace thirdCard = graph.getCardByCoord(card.getXCoord() - 1, card.getYCoord() - 3);
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
                                    CornerCardFace secondCard = graph.getCardByCoord(card.getXCoord(), card.getYCoord() + 2);
                                    if(secondCard != null && secondCard.getKingdom() == Symbol.ANIMAL && !usedCards.contains(secondCard)){
                                        CornerCardFace thirdCard = graph.getCardByCoord(card.getXCoord() + 1, card.getYCoord() + 3);
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
                                    CornerCardFace secondCard = graph.getCardByCoord(card.getXCoord(), card.getYCoord() + 2);
                                    if(secondCard != null && secondCard.getKingdom() == Symbol.BUG && !usedCards.contains(secondCard)){
                                        CornerCardFace thirdCard = graph.getCardByCoord(card.getXCoord() - 1, card.getYCoord() + 3);
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
                int requiredSymbolCount = scoreRequirements.get(symbol); //can either be 2 or 3. 2 points for each set of 2 symbols or 3 points for each set of 3 symbols
                int actualSymbolCount = activeSymbols.get(symbol);
                points = actualSymbolCount / requiredSymbolCount * requiredSymbolCount;
            }
        }
        return points;
    }
}
