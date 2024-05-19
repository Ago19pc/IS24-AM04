package Server.Connections;

import Server.Controller.Controller;
import Server.Exception.*;
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
    private final List<String> disconnectedPlayerIds = new ArrayList<>();

    public GeneralServerConnectionHandler() throws IOException {
        serverConnectionHandlerRMI = new ServerConnectionHandlerRMI();
        serverConnectionHandlerSOCKET = new ServerConnectionHandlerSOCKET();
        PingPong pingPong = new PingPong(this);
        pingPong.start();
    }

    public void setPlayerAsDisconnected(String id) {
        disconnectedPlayerIds.add(id);
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

    public void removePlayerByName(String name) {
        String id = playerID.entrySet().stream().filter(entry -> entry.getValue().equals(name)).findFirst().get().getKey();
        try{
            getServerConnectionHandler(id).killClient(id);
        } catch (PlayerNotInAnyServerConnectionHandlerException e) {
            System.out.println("Player not found in any server connection handler, PLAYER NOT REMOVED!");
            e.printStackTrace();
        } catch (Exception e){
            System.out.println("Exception, PLAYER NOT REMOVED!");
            e.printStackTrace();
        }
        disconnectedPlayerIds.remove(id);
        playerID.remove(id);
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
        System.out.println("Sending message to all clients");
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
            System.out.println("Remote exception, MESSAGE NOT SENT!");
            e.printStackTrace();
        }
    }

    public void setName(String name, String clientID) throws IllegalArgumentException, TooManyPlayersException, AlreadyStartedException {
         if (serverConnectionHandlerSOCKET.isClientAvailable(clientID)) {
            serverConnectionHandlerSOCKET.setName(name, clientID);
        } else if (serverConnectionHandlerRMI.isClientAvailable(clientID)){
             serverConnectionHandlerRMI.setName(name, clientID);
         }
         else {
            System.out.println("Client not found in any server connection handler, NAME NOT SET!");
        }

    }

    public void setController(Controller controller) {
        serverConnectionHandlerSOCKET.setController(controller);
        serverConnectionHandlerRMI.setController(controller);
    }

    public void start() {
        serverConnectionHandlerSOCKET.start();
    }

    /**
     * Sets a player as offline i.e. adds it to the to disconnect list
     * @param id the player's id
     */
    public void setOffline(String id) {
        disconnectedPlayerIds.add(id);
        System.out.println("Player " + getPlayerNameByID(id) + " is now set offline");
    }

    public boolean isOnline(String id) {
        return !disconnectedPlayerIds.contains(id);
    }
}
