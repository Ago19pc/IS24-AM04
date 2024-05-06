package Server.Messages;

import Client.Controller.ClientController;
import Server.Card.Card;
import Server.Enums.DeckPosition;
import Server.Enums.Decks;
import Server.Exception.PlayerNotFoundByNameException;

import java.io.Serializable;
import java.util.List;

public class OtherPlayerDrawCardMessage implements Serializable, ToClientMessage {

    private String name;
    private Decks deckFrom;
    private DeckPosition drawPosition;
    private List<Card> newBoardCards;
    private int turnNumber;
    private String activePlayerName;

    public OtherPlayerDrawCardMessage(String name, Decks deckFrom, DeckPosition drawPosition, List<Card> newBoardCards, int turnNumber, String activePlayerName) {
        this.name = name;
        this.deckFrom = deckFrom;
        this.drawPosition = drawPosition;
        this.newBoardCards = newBoardCards;
        this.turnNumber = turnNumber;
        this.activePlayerName = activePlayerName;
    }

    @Override
    public void clientExecute(ClientController controller) throws PlayerNotFoundByNameException {
        controller.drawOtherPlayer(name, deckFrom, drawPosition, newBoardCards, turnNumber, activePlayerName);
    }
}
