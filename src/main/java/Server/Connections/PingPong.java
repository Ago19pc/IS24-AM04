package Server.Connections;

public class PingPong extends Thread{
    private final GeneralServerConnectionHandler connectionHandler;

    public PingPong(GeneralServerConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
    }

    public void run(){
        while(true) {
            connectionHandler.getServerConnectionHandlerRMI().pingAll();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
