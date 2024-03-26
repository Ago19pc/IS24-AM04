package main.java.Server.Deck;

public class ResourceDeck extends Deck {
    public ResourceDeck(){
        System.out.println(this.toString() + "ResourceDeck");
        this.createCards();
    }

    /**
     * generate the cards
     */
    private void createCards() {
        System.out.println(this.toString() + " createCards");
    }

}
