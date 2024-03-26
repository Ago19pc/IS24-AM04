package main.java.Server.Deck;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import main.java.Server.Card.AchievementCard;
import main.java.Server.Card.AchievementFrontFace;
import main.java.Server.Card.EmptyCardFace;
import main.java.Server.Enums.Symbol;


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

                List<Symbol> scoreRequirements = new ArrayList<>();
                int score = 0;
                for (int i = 0; i < partsF.length; i++) {
                    if (i == partsF.length - 1) {
                        score = Integer.parseInt(partsF[i]);
                    } else {
                        scoreRequirements.add(Symbol.valueOf(partsF[i]));
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

        Collections.shuffle(this.cards);

    }
    

}
