package Server.Messages;

import Client.Controller.ClientController;
import Server.Card.Card;
import Server.Controller.Controller;
import Server.Enums.DeckPosition;
import Server.Enums.Decks;
import Server.Exception.*;
import Server.Player.Player;

public class DrawCardMessage implements GeneralMessage {
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
        }catch (PlayerNotFoundByNameException | TooManyElementsException | InvalidMoveException |
                AlreadyFinishedException e){
            e.printStackTrace();
        }
    }
    @Override
    public void clientExecute(ClientController controller) throws ClientExecuteNotCallableException {
        throw new ClientExecuteNotCallableException();
    }


}
