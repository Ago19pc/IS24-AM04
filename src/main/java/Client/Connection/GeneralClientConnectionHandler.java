package Client.Connection;

import Client.Controller.ClientController;
import Server.Messages.LobbyPlayersMessage;
import Server.Messages.ToServerMessage;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * This class is the endpoint for managing the connection to the server.
 * It works by creating an underlying ClientConnectionHandlerSOCKET or ClientConnectionHandlerRMI and using its methods.
 */
public class GeneralClientConnectionHandler {
    private ClientConnectionHandlerSOCKET clientConnectionHandlerSOCKET;
    private ClientConnectionHandlerRMI clientConnectionHandlerRMI;
    private ClientController controller;
    private boolean trueifRMI = false;

    /**
     * Standard constructor.
     * @param controller the client controller
     * @param trueifRMI true if the connection is RMI, false if it is SOCKET. The corrisponding ClientConnectionHandler is created.
     */
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
     * Fast constructor. For testing purposes only.
     * @param controller the client controller
     * @param trueifRMI true if the connection is RMI, false if it is SOCKET
     * @param debugMode if true, the client connects automatically either to RMI-localhost:1099 or to SOCKET-localhost:1234
     */
    public GeneralClientConnectionHandler(ClientController controller, boolean trueifRMI, boolean debugMode) {
        this.trueifRMI = trueifRMI;
        this.controller = controller;
        if(!trueifRMI){
            clientConnectionHandlerSOCKET = new ClientConnectionHandlerSOCKET(debugMode, controller);
        } else {
            try {
                setSocket("localhost", 1099);
            } catch (NotBoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    /**
     * Connects to the server specified
     * @param server_host the server hostname
     * @param server_port the server port
     */
    public void setSocket(String server_host, int server_port) throws IOException, NotBoundException {
        if(trueifRMI) {
            clientConnectionHandlerRMI.setServer(server_host);
            clientConnectionHandlerRMI.setController(controller);
            LobbyPlayersMessage lobby = clientConnectionHandlerRMI.server.join(clientConnectionHandlerRMI.rmi_client_port);
            lobby.clientExecute(controller);
        } else {
            clientConnectionHandlerSOCKET.setSocket(server_host, server_port);
        }

    }

    //todo: ADD JAVADOC
    public void start() {
        if (!trueifRMI) {
            clientConnectionHandlerSOCKET.start();
        }
    }

    /**
     * Sends a message to the server
     * @param message the message to send
     */
    public void sendMessage(ToServerMessage message){
        if(trueifRMI) {
            try {
                System.out.println("Sending message to server");
                clientConnectionHandlerRMI.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                clientConnectionHandlerSOCKET.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
