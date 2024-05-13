package Server.Connections;

import Server.Exception.PlayerNotFoundByNameException;
import Server.Exception.PlayerNotInAnyServerConnectionHandlerException;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class PingPong extends Thread{
    private GeneralServerConnectionHandler connectionHandler;

    public PingPong(GeneralServerConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
    }

    public void run(){
        while(true) {
            List<String> removed = new ArrayList<>();
            connectionHandler.getServerConnectionHandlerRMI().pingAll();
            connectionHandler.getDisconnected().forEach((id) -> {
                try {
                    connectionHandler.getServerConnectionHandler(id).killClient(id);
                    removed.add(id);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                } catch (PlayerNotFoundByNameException e) {
                    throw new RuntimeException(e);
                } catch (PlayerNotInAnyServerConnectionHandlerException e) {
                    throw new RuntimeException(e);
                }
            });
            removed.forEach(connectionHandler::removeFromDisconnected);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
