package Client.Connection;

import Client.Controller.ClientController;
import Server.Messages.GeneralMessage;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class GeneralClientConnectionHandler {
    private ClientConnectionHandlerSOCKET clientConnectionHandlerSOCKET;
    private ClientConnectionHandlerRMI clientConnectionHandlerRMI;
    private ClientController controller;
    private boolean trueifRMI = false;

    public GeneralClientConnectionHandler(ClientController controller, boolean trueifRMI) {
        this.trueifRMI = trueifRMI;
        this.controller = controller;
        if(!trueifRMI){
            clientConnectionHandlerSOCKET = new ClientConnectionHandlerSOCKET(controller);
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

    public void setSocket(String host, int port) throws NotBoundException, IOException {
        if(trueifRMI) {
            clientConnectionHandlerRMI = new ClientConnectionHandlerRMI(host);
            clientConnectionHandlerRMI.setController(controller);
        } else {
            clientConnectionHandlerSOCKET.setSocket(host, port);
        }
    }

    public void start() {
        if (!trueifRMI) {
            clientConnectionHandlerSOCKET.start();
        }
    }

    public void sendMessage(GeneralMessage message){
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

}
