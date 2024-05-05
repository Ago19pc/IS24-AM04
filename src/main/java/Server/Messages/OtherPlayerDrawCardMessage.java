package Server.Messages;

import Client.Controller.ClientController;
import Server.Card.Card;
import Server.Enums.DeckPosition;
import Server.Enums.Decks;

import java.io.Serializable;
import java.util.List;

public class OtherPlayerDrawCardMessage implements Serializable, ToClientMessage {

    private String name;
    private Decks deckFrom;
    private DeckPosition drawPosition;
    private List<Card> newBoardCards;
    private int turnNumber;
    private String activePlayerName;

    @Override
    public void clientExecute(ClientController controller) {
        controller.drawOtherPlayer(name, deckFrom, drawPosition, newBoardCards, turnNumber, activePlayerName);
    }
}
