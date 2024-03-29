package main.java.Server.Deck;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

import main.java.Server.Card.AchievementCard;
import main.java.Server.Card.AchievementFrontFace;
import main.java.Server.Card.EmptyCardFace;
import main.java.Server.Enums.Symbol;
import static main.java.Server.Enums.Face.FRONT;
import main.java.Server.Card.Card;


public class AchievementDeck extends Deck {

    public AchievementDeck(){
        System.out.println(this.toString() + "AchievementDeck");
        this.createCards();
    }

    /**
     * generate the cards
     */
    private void createCards() {
        File fileFRONT = null;
        
        BufferedReader readerFRONT = null;
        

        try {
            fileFRONT = new File("images\\AchievementFrontFace.txt");
            
            readerFRONT = new BufferedReader(new FileReader(fileFRONT));
            
        
        

            String lineF;
            while ((lineF = readerFRONT.readLine()) != null) {

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
            
                AchievementFrontFace frontFace = new AchievementFrontFace("ACHIEVEMENTFRONT", scoreRequirements, score);
            
                EmptyCardFace backFace = new EmptyCardFace("ACHIEVEMENTBACK");
            
                AchievementCard card = new AchievementCard(frontFace, backFace);
                this.cards.add(card);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*This is to print to check if the cards are generated correctly
         for(Card card : this.cards) {
            System.out.println("Requirements Symbols:");
            card.getFace(FRONT).getScoreRequirements().forEach((symbol, quantity) -> {if (quantity > 0) System.out.println(symbol + " " + quantity);});
            System.out.println("Points:");
            System.out.println(card.getFace(FRONT).getScore());
            System.out.println();
            }
        */
            
        Collections.shuffle(this.cards);

    }
    


}
