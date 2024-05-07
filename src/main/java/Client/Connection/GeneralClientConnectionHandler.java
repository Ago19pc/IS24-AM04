package Client.Connection;

import Client.Controller.ClientController;
import Server.Messages.ToServerMessage;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class GeneralClientConnectionHandler {
    private ClientConnectionHandlerSOCKET clientConnectionHandlerSOCKET;
    private ClientConnectionHandlerRMI clientConnectionHandlerRMI;
    private ClientController controller;
    private boolean trueifRMI = false;

    public GeneralClientConnectionHandler(ClientController controller, boolean trueifRMI) throws RemoteException {
        this.trueifRMI = trueifRMI;
        this.controller = controller;
        if(!trueifRMI){
            clientConnectionHandlerSOCKET = new ClientConnectionHandlerSOCKET(controller);
        } else {
            clientConnectionHandlerRMI = new ClientConnectionHandlerRMI();
            clientConnectionHandlerRMI.setRmi_client_port(2000);
        }
    }

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

    public void setSocket(String server_host, int server_port) throws NotBoundException, IOException {
        if(trueifRMI) {
            clientConnectionHandlerRMI.setServer(server_host);
            clientConnectionHandlerRMI.setController(controller);
            controller.setId(clientConnectionHandlerRMI.server.join(clientConnectionHandlerRMI.rmi_client_port));
        } else {
            clientConnectionHandlerSOCKET.setSocket(server_host, server_port);
        }
    }

    public void start() {
        if (!trueifRMI) {
            clientConnectionHandlerSOCKET.start();
        }
    }

    public void sendMessage(ToServerMessage message){
        if(trueifRMI) {
            try {
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

    public boolean isConnectedToServer() {
        if(trueifRMI) {
            return clientConnectionHandlerRMI != null;
        } else {
            return clientConnectionHandlerSOCKET.sender.getOutputBuffer() == null;
        }
    }

    public ClientConnectionHandlerRMI getClientConnectionHandlerRMI() {
        return clientConnectionHandlerRMI;
    }

    public ClientConnectionHandlerSOCKET getClientConnectionHandlerSOCKET() {
        return clientConnectionHandlerSOCKET;
    }

    public boolean getTrueIfRMI() {
        return trueifRMI;
    }
}
