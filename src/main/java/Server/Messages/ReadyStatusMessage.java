package Server.Messages;

import Client.Controller.ClientController;
import Server.Connections.ClientHandler;
import Server.Controller.Controller;
import Server.Exception.AlreadyStartedException;
import Server.Exception.MissingInfoException;
import Server.Exception.PlayerNotFoundByNameException;
import Server.Player.Player;

import java.io.Serializable;

public class ReadyStatusMessage implements Serializable, ToClientMessage, ToServerMessage {
    private boolean ready;
    private String name;

    public ReadyStatusMessage(boolean isReady){
        this.ready = isReady;
    }

    public ReadyStatusMessage(boolean isReady, String name){
        this.ready = isReady;
        this.name = name;
    }

    @Override
    public void serverExecute(Controller controller) {
        String playerName = "";
        try {
            ClientHandler client = controller.getConnectionHandler().getServerConnectionHandlerSOCKET().getThreads()
                    .stream().filter(c -> c.getReceiver().threadId() == Thread.currentThread().threadId()).toList().getFirst();
            playerName = controller.getConnectionHandler().getServerConnectionHandlerSOCKET().getThreadName(client);
            Player player = controller.getPlayerByName(playerName);
            if(this.ready) {
                controller.setReady(player);
            } else {
                controller.setNotReady(player);
            }
        } catch(PlayerNotFoundByNameException e){
            NameNotYetSetMessage nameNotYetSetMessage = new NameNotYetSetMessage();
            //todo: send message to client based on ip
        } catch (MissingInfoException e){
            ColorNotYetSetMessage colorNotYetSetMessage = new ColorNotYetSetMessage();
            controller.getConnectionHandler().sendMessage(colorNotYetSetMessage, playerName);
        } catch (AlreadyStartedException e ){
            GameAlreadyStartedMessage gameAlreadyStartedMessage = new GameAlreadyStartedMessage();
            controller.getConnectionHandler().sendMessage(gameAlreadyStartedMessage, playerName);
        }

    }

    @Override
    public void clientExecute(ClientController controller) {
        controller.updatePlayerReady(this.ready, this.name);
    }
}
