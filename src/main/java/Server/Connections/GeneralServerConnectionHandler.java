package Server.Connections;

import Server.Controller.Controller;
import Server.Exception.AddressNotInAnyServerConnectionHandlerException;
import Server.Exception.PlayerNotInAnyServerConnectionHandlerException;
import Server.Messages.GeneralMessage;

import java.io.IOException;

public class GeneralServerConnectionHandler {
    private ServerConnectionHandlerSOCKET serverConnectionHandlerSOCKET;
    private ServerConnectionHandlerRMI serverConnectionHandlerRMI;

    public GeneralServerConnectionHandler() throws IOException {
        serverConnectionHandlerRMI = new ServerConnectionHandlerRMI();
        serverConnectionHandlerSOCKET = new ServerConnectionHandlerSOCKET();
    }

    public GeneralServerConnectionHandler(boolean debugMode) {
        serverConnectionHandlerRMI = new ServerConnectionHandlerRMI(debugMode);
        serverConnectionHandlerSOCKET = new ServerConnectionHandlerSOCKET(debugMode);
    }

    public ServerConnectionHandler getServerConnectionHandler() {
        return serverConnectionHandlerSOCKET;
    }

    public ServerConnectionHandler getServerConnectionHandler(String name) throws PlayerNotInAnyServerConnectionHandlerException {
        if (serverConnectionHandlerRMI.isClientNameAvailable(name)) {
            return serverConnectionHandlerRMI;
        } else if (serverConnectionHandlerSOCKET.isClientNameAvailable(name)) {
            return serverConnectionHandlerSOCKET;
        } else throw new PlayerNotInAnyServerConnectionHandlerException();

    }

    public ServerConnectionHandlerRMI getServerConnectionHandlerRMI() {
        return serverConnectionHandlerRMI;
    }

    public ServerConnectionHandlerSOCKET getServerConnectionHandlerSOCKET() {
        return serverConnectionHandlerSOCKET;
    }

    public void sendAllMessage(GeneralMessage message) {
        serverConnectionHandlerSOCKET.sendAllMessage(message);
        serverConnectionHandlerRMI.sendAllMessage(message);
    }

    public void sendMessage(GeneralMessage message, String name) {
        try {
            getServerConnectionHandler(name).sendMessage(message, name);
        } catch (PlayerNotInAnyServerConnectionHandlerException e) {
            System.out.println("Player not found in any server connection handler, MESSAGE NOT SENT!");
            e.printStackTrace();
        }
    }

    public void setName(String host, int port, String name) {
         if (serverConnectionHandlerSOCKET.isClientAddressAvailable(host, port)) {
            serverConnectionHandlerSOCKET.setName(host, port, name);
        } else {
             System.out.println("Client not found in [Socket], assuming it is in [RMI]... Hope it works, if not I'm the problem (FIND ME IN GeneralServerConnectionHandler.java)");
             serverConnectionHandlerRMI.setName(host, port, name);
         }
         //else {
         //   System.out.println("Client not found in any server connection handler, NAME NOT SET!");
         //   throw new AddressNotInAnyServerConnectionHandlerException();
        //}

    }

    public void setController(Controller controller) {
        serverConnectionHandlerSOCKET.setController(controller);
        serverConnectionHandlerRMI.setController(controller);
    }

    public void start() {
        serverConnectionHandlerSOCKET.start();
    }

    public void killClient(String name) {
        try {
            getServerConnectionHandler(name).killClient(name);
        } catch (PlayerNotInAnyServerConnectionHandlerException e) {
            System.out.println("Player not found in any server connection handler, CLIENT NOT KILLED!");
            e.printStackTrace();
        }
    }
}
