package Client.Connection;

import Server.Connections.GeneralServerConnectionHandler;

public class PingServer extends Thread{
    private final ClientConnectionHandlerRMI connectionHandler;
    private boolean shouldPing;

    /**
     * Constructor
     * @param connectionHandler the connection handler
     */
    public PingServer(ClientConnectionHandlerRMI connectionHandler) {
        this.connectionHandler = connectionHandler;
        this.shouldPing = true;
    }
    /**
     * Sends a ping message to the RMI server every 5 seconds.
     */
    public void run(){
        while(shouldPing) {
            connectionHandler.pingServer();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    /**
     * Stops the ping server
     */
    public void stopPinging() {
        this.shouldPing = false;
    }
}
