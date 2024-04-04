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
import Server.Enums.Symbol;

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

            String partsB = readerBACK.readLine();

           

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
            
        Collections.shuffle(this.cards);

    }

}
