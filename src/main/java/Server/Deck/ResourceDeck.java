package Server.Deck;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
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
        createCards();
        super.shuffle();
        System.out.println("ResourceDeck");

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
            fileFRONT = new File("images/ResourceFrontFace.txt");
            fileBACK = new File("images/ResourceBackFace.txt");
            readerFRONT = new BufferedReader(new FileReader(fileFRONT));
            readerBACK = new BufferedReader(new FileReader(fileBACK));
        
        

        String lineF;
        int cardNumber = 0;
        while ((lineF = readerFRONT.readLine()) != null) {

            String[] partsF = lineF.split(" ");

            String partsB = readerBACK.readLine();

           

            Map<CardCorners, Symbol> cornerSymbolsF = new HashMap<>();
            int point = 0;
            for (CardCorners c : CardCorners.values()) {
                cornerSymbolsF.put(c, Symbol.valueOf(partsF[c.ordinal()]));
            }
            Symbol kingdom;
            switch(cardNumber / 10){
                case 0:
                    kingdom = Symbol.FUNGUS;
                    break;
                case 1:
                    kingdom = Symbol.PLANT;
                    break;
                case 2:
                    kingdom = Symbol.ANIMAL;
                    break;
                case 3:
                    kingdom = Symbol.BUG;
                    break;
                default:
                    kingdom = Symbol.NONE;
                    break;
            }
            ResourceFrontFace frontFace = new ResourceFrontFace("RESOURCEFRONT", cornerSymbolsF, point, kingdom);
            
            
            List<Symbol> centerSymbolsB = new ArrayList<>();
            centerSymbolsB.add(Symbol.valueOf(partsB));
            

            RegularBackFace backFace = new RegularBackFace("RESOURCEBACK", centerSymbolsB);
            
            ResourceCard card = new ResourceCard(frontFace, backFace);
            this.cards.add(card);
            cardNumber++;
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
            


    }

}
