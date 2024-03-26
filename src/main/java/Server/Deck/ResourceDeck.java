package main.java.Server.Deck;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.java.Server.Card.RegularBackFace;
import main.java.Server.Card.ResourceFrontFace;
import main.java.Server.Card.ResourceCard;
import main.java.Server.Card.Card;
import main.java.Server.Enums.Symbol;
import static main.java.Server.Enums.Face.BACK;
import static main.java.Server.Enums.Face.FRONT;

public class ResourceDeck extends Deck {
    public ResourceDeck(){
        super();
        System.out.println(this.toString() + "ResourceDeck");
        this.createCards();
    }

    /**
     * generate the cards
     */
    private void createCards() {
        File fileFRONT = null;
        File fileBACK = null;
        BufferedReader readerFRONT = null;
        BufferedReader readerBACK = null;

        try {
            fileFRONT = new File("images\\ResourceFrontFace.txt");
            fileBACK = new File("images\\ResourceBackFace.txt");
            readerFRONT = new BufferedReader(new FileReader(fileFRONT));
            readerBACK = new BufferedReader(new FileReader(fileBACK));
        
        

        String lineF;
        while ((lineF = readerFRONT.readLine()) != null) {

            String[] partsF = lineF.split(" ");
            //System.out.println(lineF);

            String partsB = readerBACK.readLine();
            //System.out.println(partsB);

           

            Map<Integer, Symbol> cornerSymbolsF = new HashMap<Integer, Symbol>();
            int point = 0;
            for (int i = 0; i < partsF.length; i++) {
                if (i < 4) cornerSymbolsF.put(i, Symbol.valueOf(partsF[i]));
                else point = Integer.parseInt(partsF[i]);
            }
            
            ResourceFrontFace frontFace = new ResourceFrontFace("RESOURCEFRONT", cornerSymbolsF, point);
            
            
            List<Symbol> centerSymbolsB = new ArrayList<Symbol>();
            centerSymbolsB.add(Symbol.valueOf(partsB));
            

            RegularBackFace backFace = new RegularBackFace("RESOURCEBACK", centerSymbolsB);
            
            ResourceCard card = new ResourceCard(frontFace, backFace);
            this.cards.add(card);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
        /*This is to print to check if the cards are generated correctly 
         for(Card card : this.cards) {
             System.out.println("Front Corner Symbols:");
             card.getFace(FRONT).getCornerSymbols().forEach((key, value) -> System.out.println(key + " " + value));
             System.out.println("Front Points:");
             System.out.println(card.getFace(FRONT).getScore());
             
             System.out.println("Back Corner Symbols:");
             card.getFace(BACK).getCornerSymbols().forEach((key, value) -> System.out.println(key + " " + value));
             System.out.println("back Center Symbols:");
             card.getFace(BACK).getCenterSymbols().forEach(symbol -> System.out.println(symbol));
             System.out.println();
            }
        */
            
        Collections.shuffle(this.cards);

    }

}
