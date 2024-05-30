package Server.Messages;

import Server.Controller.Controller;
import Server.Exception.AlreadyStartedException;
import Server.Exception.PlayerNotFoundByNameException;
import Server.Exception.PlayerNotInAnyServerConnectionHandlerException;

import java.io.Serializable;

public class SavedGameMessage implements Serializable, ToServerMessage {
    private final String id;
    private final String name;

    public SavedGameMessage(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public void serverExecute(Controller controller) {
        try {
            controller.addSavedPlayer(id, name);
        } catch (AlreadyStartedException e) {
            GameAlreadyStartedMessage message = new GameAlreadyStartedMessage();
            try{
                controller.getConnectionHandler().getServerConnectionHandler(id).sendMessage(message, id);
            } catch (Exception exception) {
                System.out.println("Player not found");
            }
        } catch (PlayerNotFoundByNameException e) {
            PlayerAlreadyPlayingMessage message = new PlayerAlreadyPlayingMessage();
            try{
                controller.getConnectionHandler().getServerConnectionHandler(id).sendMessage(message, id);
            } catch (Exception exception) {
                System.out.println("Player not found");
            }
        } catch (IllegalArgumentException e) {
            InvalidNameMessage message = new InvalidNameMessage();
            try{
                controller.getConnectionHandler().getServerConnectionHandler(id).sendMessage(message, id);
            } catch (Exception exception) {
                System.out.println("Player not found");
            }
        }
    }
}
