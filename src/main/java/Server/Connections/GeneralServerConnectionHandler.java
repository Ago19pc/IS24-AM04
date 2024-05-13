package Server.Connections;

import Server.Controller.Controller;
import Server.Exception.PlayerNotFoundByNameException;
import Server.Exception.PlayerNotInAnyServerConnectionHandlerException;
import Server.Messages.ToClientMessage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneralServerConnectionHandler {
    private ServerConnectionHandlerSOCKET serverConnectionHandlerSOCKET;
    private ServerConnectionHandlerRMI serverConnectionHandlerRMI;
    /**
     * Map<Id, Name>
     */
    private Map<String, String> playerID = new HashMap<>();

    public GeneralServerConnectionHandler() throws IOException {
        serverConnectionHandlerRMI = new ServerConnectionHandlerRMI();
        serverConnectionHandlerSOCKET = new ServerConnectionHandlerSOCKET();
    }

    public GeneralServerConnectionHandler(boolean debugMode) {
        serverConnectionHandlerRMI = new ServerConnectionHandlerRMI(debugMode);
        serverConnectionHandlerSOCKET = new ServerConnectionHandlerSOCKET(debugMode);
    }

    /**
     * Associates the name to an id
     * @param name, the player name
     * @param clientID, the id to be associated with
     */
    public void addPlayerByID(String name, String clientID) {
        playerID.put(clientID, name);
    }

    /**
     * Gets a player name by id
     * @param id, the id
     * @return the name
     */
    public String getPlayerNameByID(String id) {
        return playerID.get(id);
    }

    public String getIdByName(String name) {
        return playerID.entrySet().stream().filter(entry -> entry.getValue().equals(name)).findFirst().get().getKey();
    }

    public ServerConnectionHandler getServerConnectionHandler() {
        return serverConnectionHandlerSOCKET;
    }

    public ServerConnectionHandler getServerConnectionHandler(String id) throws PlayerNotInAnyServerConnectionHandlerException {
        if (serverConnectionHandlerRMI.isClientAvailable(id)) {
            return serverConnectionHandlerRMI;
        } else if (serverConnectionHandlerSOCKET.isClientAvailable(id)) {
            return serverConnectionHandlerSOCKET;
        } else throw new PlayerNotInAnyServerConnectionHandlerException();

    }

    public ServerConnectionHandlerRMI getServerConnectionHandlerRMI() {
        return serverConnectionHandlerRMI;
    }

    public ServerConnectionHandlerSOCKET getServerConnectionHandlerSOCKET() {
        return serverConnectionHandlerSOCKET;
    }

    public void sendAllMessage(ToClientMessage message) {
        serverConnectionHandlerSOCKET.sendAllMessage(message);
        serverConnectionHandlerRMI.sendAllMessage(message);
    }

    public void sendMessage(ToClientMessage message, String name) {
        try {
            String id = playerID.entrySet().stream().filter(entry -> entry.getValue().equals(name)).findFirst().get().getKey();
            getServerConnectionHandler(id).sendMessage(message, id);
        } catch (PlayerNotInAnyServerConnectionHandlerException e) {
            System.out.println("Player not found in any server connection handler, MESSAGE NOT SENT!");
            e.printStackTrace();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void setName(String name, String clientID) throws RemoteException {
         if (serverConnectionHandlerSOCKET.isClientAvailable(clientID)) {
            serverConnectionHandlerSOCKET.setName(name, clientID);
        } else {
             System.out.println("Client not found in [Socket], assuming it is in [RMI]... Hope it works, if not I'm the problem (FIND ME IN GeneralServerConnectionHandler.java)");
             serverConnectionHandlerRMI.setName(name, clientID);
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

    /**
     *
     * @return a list of disconnected clients ids
     */
    public List<String> getDisconnected(){
       List<String> disconnected = new ArrayList<>();
        serverConnectionHandlerSOCKET.getAllIds().forEach(id -> {
                if (!serverConnectionHandlerSOCKET.ping(id)){
                    disconnected.add(getPlayerNameByID(id));
                }
        });

        serverConnectionHandlerRMI.getAllIds().forEach(id -> {
            if (!serverConnectionHandlerRMI.ping(id)){
                disconnected.add(getPlayerNameByID(id));

            }
        });

        return disconnected;
    }



    public void killClient(String name) {
        String id = playerID.entrySet().stream().filter(entry -> entry.getValue().equals(name)).findFirst().get().getKey();
        try {
            getServerConnectionHandler(id).killClient(id);
        } catch (PlayerNotInAnyServerConnectionHandlerException | RemoteException e) {
            System.out.println("Player not found in any server connection handler, CLIENT NOT KILLED!");
            e.printStackTrace();
        } catch (PlayerNotFoundByNameException e) {
            throw new RuntimeException(e);
        }
    }
}
