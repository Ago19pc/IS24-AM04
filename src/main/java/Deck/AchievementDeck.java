package main.java.Deck;

public class AchievementDeck extends Deck {

    public AchievementDeck(){
        System.out.println(this.toString() + "AchievementDeck");
        this.createCards();
    }

    /**
     * generate the cards
     */
    private void createCards() {
        System.out.println(this.toString() + " createCards");
    }


}
