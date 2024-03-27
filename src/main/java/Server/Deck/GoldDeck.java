package main.java.Server.Deck;

import main.java.Server.Card.*;
import main.java.Server.Enums.Symbol;
import static main.java.Server.Enums.Face.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class GoldDeck extends Deck {
    public GoldDeck(){
        System.out.println(this.toString() + "GoldDeck");
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
            fileFRONT = new File("images\\GoldFrontFace.txt");
            fileBACK = new File("images\\GoldBackFace.txt");
            readerFRONT = new BufferedReader(new FileReader(fileFRONT));
            readerBACK = new BufferedReader(new FileReader(fileBACK));



            String lineF;
            while ((lineF = readerFRONT.readLine()) != null) {

                String[] partsF = lineF.split(" ");
                //System.out.println(lineF);

                String partsB = readerBACK.readLine();
                //System.out.println(partsB);



                Map<Integer, Symbol> cornerSymbolsF = new HashMap<Integer, Symbol>();

                Map<Symbol, Integer> scoreRequirementsF = new HashMap<Symbol, Integer>();
                for (Symbol s: Symbol.values()) {
                    scoreRequirementsF.put(s, 0);
                }

                Map<Symbol, Integer> placementRequirementsF = new HashMap<Symbol, Integer>();
                for (Symbol s: Symbol.values()) {
                    placementRequirementsF.put(s, 0);
                }




                int point = 0;
                for (int i = 0; i < partsF.length; i++) {
                    if (i < 4) cornerSymbolsF.put(i, Symbol.valueOf(partsF[i]));
                    else if (i ==  partsF.length - 1) point = Integer.parseInt(partsF[i]);
                    else {
                        // SCORE REQUIREMENT DOPO LA ,
                        if (Objects.equals(partsF[i], ",")) {
                            i++;
                            scoreRequirementsF.put(Symbol.valueOf(partsF[i]), 1);
                        }
                        // PLACEMENT REQUIREMENT I RIMANENTI
                        else {
                            int quantity = Integer.parseInt(partsF[i]);
                            i++;
                            placementRequirementsF.put(Symbol.valueOf(partsF[i]), quantity);
                        }
                    }

                }

                ResourceFrontFace frontFace = new GoldFrontFace("GOLDFRONT", cornerSymbolsF, point, placementRequirementsF, scoreRequirementsF);

                // DA QUI E DA VEDERE
                List<Symbol> centerSymbolsB = new ArrayList<Symbol>();
                centerSymbolsB.add(Symbol.valueOf(partsB));


                RegularBackFace backFace = new RegularBackFace("GOLDBACK", centerSymbolsB);

                ResourceCard card = new ResourceCard(frontFace, backFace);
                this.cards.add(card);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*This is to print to check if the cards are generated correctly
         for(Card card : this.cards) {
             System.out.println("Front Corner Symbols:");
             card.getFace(FRONT).getCornerSymbols().forEach((key, value) -> System.out.println(value));
             System.out.print("Front Points:");
             System.out.println(card.getFace(FRONT).getScore());
             System.out.println("Front Placement Requirements:");
             card.getFace(FRONT).getPlacementRequirements().forEach((key, value) -> {if (value> 0) System.out.println(key + " " + value);});
             System.out.println("Front Score Requirements:");
             card.getFace(FRONT).getScoreRequirements().forEach((key, value) -> {if (value> 0) System.out.println(key + " " + value);});

             System.out.println("Back Center Symbols:");
             card.getFace(BACK).getCenterSymbols().forEach(System.out::println);
             System.out.println();
            }
        //*/

        Collections.shuffle(this.cards);
    }
}
