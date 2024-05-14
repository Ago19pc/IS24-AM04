package Server.Connections;

public class PingPong extends Thread{
    private GeneralServerConnectionHandler connectionHandler;

    public PingPong(GeneralServerConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
    }

    public void run(){
        while(true) {
            connectionHandler.getServerConnectionHandlerRMI().pingAll();
            connectionHandler.getPlayersToDisconnect().forEach((id) -> {
                connectionHandler.getDisconnected().add(id);
            });
            connectionHandler.getPlayersToDisconnect().clear();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
