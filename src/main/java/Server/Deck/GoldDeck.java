package main.java.Server.Deck;

public class GoldDeck extends Deck {
    public GoldDeck(){
        System.out.println(this.toString() + "GoldDeck");
        this.createCards();
    }

    /**
     * generate the cards
     */
    private void createCards() {
        System.out.println(this.toString() + " createCards");
    }
}
