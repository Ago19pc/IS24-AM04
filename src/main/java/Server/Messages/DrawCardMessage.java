package Server.Messages;

import Server.Card.Card;
import Server.Controller.Controller;
import Server.Deck.Deck;
import Server.Enums.DeckPosition;
import Server.Enums.Decks;
import Server.Exception.PlayerNotFoundByNameException;
import Server.Player.Player;
import java.io.Serializable;

public class DrawCardMessage implements Serializable, GeneralMessage {

    private final String  name;
    private final Decks from;
    private final DeckPosition deckPosition;

    public DrawCardMessage(String name, DeckPosition deckPosition, Decks from) {
        this.name = name;
        this.from = from;
        this.deckPosition= deckPosition;
    }


    @Override
    public void serverExecute(Controller controller) {
        try {
            controller.drawCard(controller.getPlayerByName(name), this.deckPosition, this.from);
        }catch (PlayerNotFoundByNameException e){
            e.printStackTrace();
        }
    }
    @Override
    public void clientExecute() {

    }
}
