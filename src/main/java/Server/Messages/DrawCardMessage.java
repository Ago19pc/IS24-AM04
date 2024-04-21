package Server.Messages;

import Server.Card.Card;
import Server.Deck.Deck;
import Server.Player.Player;

public class DrawCardMessage implements GeneralMessage {
    private final Card card;
    private final Player player;
    private final Deck from;
    private final Card newBoardCard;

    public DrawCardMessage(Player player, Card card, Deck from, Card newBoardCard) {
        this.player = player;
        this.card = card;
        this.from = from;
        this.newBoardCard = newBoardCard;
    }
    public void printData() {
        System.out.println(player.getName() + " has drawn a card from " + from.toString() + " and placed it on the board");
    }

    public Card getCard() {
        return card;
    }

    public Player getPlayer() {
        return player;
    }

    public Deck getFrom() {
        return from;
    }

    public Card getNewBoardCard() {
        return newBoardCard;
    }

    public boolean equals(GeneralMessage other) {
        System.out.println("DrawCardMessage equals still to be implemented.");
        return this.card.equals(((DrawCardMessage) other).getCard()) && this.player.equals(((DrawCardMessage) other).getPlayer()) && this.from.equals(((DrawCardMessage) other).getFrom()) && this.newBoardCard.equals(((DrawCardMessage) other).getNewBoardCard());
    }


}
