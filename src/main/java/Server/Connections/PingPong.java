package Server.Connections;

/**
 * This class is responsible for sending ping messages to all RMI clients every 5 seconds.
 */
public class PingPong extends Thread{
    private final GeneralServerConnectionHandler connectionHandler;

    /**
     * Constructor
     * @param connectionHandler the connection handler
     */
    public PingPong(GeneralServerConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
    }
    /**
     * Sends a ping message to all RMI clients every 5 seconds.
     */
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
