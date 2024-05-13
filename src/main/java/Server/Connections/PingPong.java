package Server.Connections;

import java.rmi.RemoteException;

public class PingPong extends Thread{
    private GeneralServerConnectionHandler connectionHandler;

    public PingPong(GeneralServerConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
    }

    public void run(){
        while(true){
            try {
                connectionHandler.getDisconnected().forEach((id) -> {
                    try {
                        connectionHandler.getServerConnectionHandler().killClient(id);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                });
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                connectionHandler.getServerConnectionHandler().ping("ping");
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
