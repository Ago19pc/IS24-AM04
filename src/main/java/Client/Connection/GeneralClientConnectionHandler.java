package Client.Connection;

import Client.Controller.ClientController;
import Server.Messages.LobbyPlayersMessage;
import Server.Messages.ToServerMessage;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class GeneralClientConnectionHandler {
    private ClientConnectionHandlerSOCKET clientConnectionHandlerSOCKET;
    private ClientConnectionHandlerRMI clientConnectionHandlerRMI;
    private final ClientController controller;
    private final boolean trueifRMI;

    public GeneralClientConnectionHandler(ClientController controller, boolean trueifRMI) throws RemoteException {
        this.trueifRMI = trueifRMI;
        this.controller = controller;
        if(!trueifRMI){
            clientConnectionHandlerSOCKET = new ClientConnectionHandlerSOCKET(controller);
        } else {
            clientConnectionHandlerRMI = new ClientConnectionHandlerRMI(1100);
            clientConnectionHandlerRMI.setRmi_client_port(1100);
        }
    }

    /**
     * Constructor for debug only
     * @param controller the controller
     * @param trueifRMI true if RMI
     * @param debugMode true if debug
     */
    public GeneralClientConnectionHandler(ClientController controller, boolean trueifRMI, boolean debugMode) {
        this.trueifRMI = trueifRMI;
        this.controller = controller;
        if(!trueifRMI){
            clientConnectionHandlerSOCKET = new ClientConnectionHandlerSOCKET(debugMode, controller);
        } else {
            try {
                setSocket("localhost", 1099);
            } catch (NotBoundException | IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    /**
     * Sets the socket
     * @param server_host, the ip
     * @param server_port, the port
     * @throws NotBoundException if you can't bound to RMI
     * @throws IOException when problem with socket
     */
    public void setSocket(String server_host, int server_port) throws NotBoundException, IOException {
        if(trueifRMI) {
            clientConnectionHandlerRMI.setServer(server_host);
            clientConnectionHandlerRMI.setController(controller);
            LobbyPlayersMessage lobby = clientConnectionHandlerRMI.server.join(clientConnectionHandlerRMI.rmi_client_port);
            lobby.clientExecute(controller);
        } else {
            clientConnectionHandlerSOCKET.setSocket(server_host, server_port);
        }

    }

    /**
     * Starts the connection (for socket only)
     */
    public void start() {
        if (!trueifRMI) {
            clientConnectionHandlerSOCKET.start();
        }
    }

    /**
     * This method sends a message to the server
     * @param message, the message to send
     */
    public void sendMessage(ToServerMessage message){
        if(trueifRMI) {
            clientConnectionHandlerRMI.sendMessage(message);
        } else {
            try {
                clientConnectionHandlerSOCKET.sendMessage(message);
            } catch (IOException e) {
                System.err.println("[SOCKET] Error while sending message to the server");
            }
        }
    }

    /**
     * @return true if the client is connected to the server
     */
    public boolean isConnectedToServer() {
        if(trueifRMI) {
            return clientConnectionHandlerRMI != null;
        } else {
            return clientConnectionHandlerSOCKET.sender.getOutputBuffer() == null;
        }
    }

    /**
     * @return the client connection handler RMI
     */
    public ClientConnectionHandlerRMI getClientConnectionHandlerRMI() {
        return clientConnectionHandlerRMI;
    }

    /**
     * @return the client connection handler SOCKET
     */
    public ClientConnectionHandlerSOCKET getClientConnectionHandlerSOCKET() {
        return clientConnectionHandlerSOCKET;
    }

    /**
     * @return true if the client is using RMI
     */
    public boolean getTrueIfRMI() {
        return trueifRMI;
    }
}
