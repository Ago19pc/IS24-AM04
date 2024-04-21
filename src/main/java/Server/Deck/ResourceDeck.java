package Server.Deck;

import Server.Card.RegularBackFace;
import Server.Card.ResourceCard;
import Server.Card.ResourceFrontFace;
import Server.Enums.CardCorners;
import Server.Enums.Symbol;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

import static Server.Enums.DeckPosition.FIRST_CARD;
import static Server.Enums.DeckPosition.SECOND_CARD;

public class ResourceDeck extends Deck {
    public ResourceDeck(){
        super();
        createCards();
        super.shuffle();
        System.out.println("ResourceDeck");
        try {
            moveCardToBoard(FIRST_CARD);
            moveCardToBoard(SECOND_CARD);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                System.out.println("generate card " + cardNumber);
                String[] partsF = lineF.split(" ");

                String partsB = readerBACK.readLine();

           
                Map<CardCorners, Symbol> cornerSymbolsF = new HashMap<>();
                int point = 0;
                for(int i = 0; i < 4; i++){
                    System.out.print(CardCorners.values()[i]);
                    System.out.println(Symbol.valueOf(partsF[i]));
                    cornerSymbolsF.put(CardCorners.values()[i], Symbol.valueOf(partsF[i]));
                }

                Symbol kingdom;
                switch(cardNumber / 10){
                    case 0:
                        kingdom = Symbol.FUNGUS;
                        if(cardNumber%10 > 6){
                            point = 1;
                        }
                        break;
                    case 1:
                        kingdom = Symbol.PLANT;
                        if(cardNumber%10 > 6){
                            point = 1;
                        }
                        break;
                    case 2:
                        kingdom = Symbol.ANIMAL;
                        if(cardNumber%10 > 6){
                            point = 1;
                        }
                        break;
                    case 3:
                        kingdom = Symbol.BUG;
                        if(cardNumber%10 > 6){point = 1;}
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
