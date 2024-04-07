package Server.Deck;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Server.Card.RegularBackFace;
import Server.Card.ResourceFrontFace;
import Server.Card.ResourceCard;
import Server.Enums.CardCorners;
import Server.Enums.Symbol;

public class ResourceDeck extends Deck {
    public ResourceDeck(){
        super();
        System.out.println("ResourceDeck");
        this.createCards();
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
            fileFRONT = new File("images\\ResourceFrontFace.txt");
            fileBACK = new File("images\\ResourceBackFace.txt");
            readerFRONT = new BufferedReader(new FileReader(fileFRONT));
            readerBACK = new BufferedReader(new FileReader(fileBACK));
        
        

        String lineF;
        while ((lineF = readerFRONT.readLine()) != null) {

            String[] partsF = lineF.split(" ");

            String partsB = readerBACK.readLine();

           

            Map<CardCorners, Symbol> cornerSymbolsF = new HashMap<>();
            int point = 0;
            for (CardCorners c : CardCorners.values()) {
                cornerSymbolsF.put(c, Symbol.valueOf(partsF[c.ordinal()]));
            }
            ResourceFrontFace frontFace = new ResourceFrontFace("RESOURCEFRONT", cornerSymbolsF, point);
            
            
            List<Symbol> centerSymbolsB = new ArrayList<>();
            centerSymbolsB.add(Symbol.valueOf(partsB));
            

            RegularBackFace backFace = new RegularBackFace("RESOURCEBACK", centerSymbolsB);
            
            ResourceCard card = new ResourceCard(frontFace, backFace);
            this.cards.add(card);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
            
        Collections.shuffle(this.cards);

    }

}
