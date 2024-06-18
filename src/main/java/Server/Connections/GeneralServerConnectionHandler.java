package Server.Connections;

import Server.Controller.Controller;
import Server.Exception.*;
import Server.Messages.RemovedPlayerMessage;
import Server.Messages.ToClientMessage;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneralServerConnectionHandler {
    private final ServerConnectionHandlerSOCKET serverConnectionHandlerSOCKET;
    private final ServerConnectionHandlerRMI serverConnectionHandlerRMI;
    /**
     * Map<Id, Name>
     */
    private final Map<String, String> playerID = new HashMap<>();
    private final List<String> disconnectedPlayerIds = new ArrayList<>();

    public GeneralServerConnectionHandler() {
        serverConnectionHandlerRMI = new ServerConnectionHandlerRMI();
        serverConnectionHandlerSOCKET = new ServerConnectionHandlerSOCKET();
        PingPong pingPong = new PingPong(this);
        pingPong.start();
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
        System.out.println(playerID);
    }

    public void removePlayerByName(String name) {
        String id = playerID.entrySet().stream().filter(entry -> entry.getValue().equals(name)).findFirst().get().getKey();
        try{
            getServerConnectionHandler(id).killClient(id);
        } catch (PlayerNotInAnyServerConnectionHandlerException e) {
            System.err.println("Player not found in any server connection handler, PLAYER NOT REMOVED!");
        } catch (Exception e){
            System.err.println("Exception, PLAYER NOT REMOVED!");
        }
        disconnectedPlayerIds.remove(id);
        playerID.remove(id);
        RemovedPlayerMessage message = new RemovedPlayerMessage(name);
        sendAllMessage(message);
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

    public Boolean isNameConnectedToId(String name) {
        return playerID.containsValue(name);
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
        System.out.println(playerID);
        try {
            String id = playerID.entrySet().stream().filter(entry -> entry.getValue().equals(name)).findFirst().get().getKey();
            if(disconnectedPlayerIds.contains(id)) {
                return;
            }
            getServerConnectionHandler(id).sendMessage(message, id);
        } catch (PlayerNotInAnyServerConnectionHandlerException e) {
            System.err.println("Player not found in any server connection handler, MESSAGE NOT SENT!");
        } catch (RemoteException e) {
            System.err.println("Remote exception, MESSAGE NOT SENT!");
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

    public void setOnline(String id) {
        disconnectedPlayerIds.remove(id);
        System.out.println("Player " + getPlayerNameByID(id) + " is now set online");
    }

    public void changePlayerId(String name, String id) {
        String idToRemove = playerID.entrySet().stream().filter(entry -> entry.getValue().equals(name)).findFirst().get().getKey();
        playerID.remove(idToRemove);
        playerID.put(id, name);
        try{
            getServerConnectionHandler(idToRemove).changeId(idToRemove, id);
        } catch (PlayerNotInAnyServerConnectionHandlerException e) {
            System.err.println("Player not found in any server connection handler, ID NOT CHANGED!");
        } catch (RemoteException e) {
            System.err.println("Remote exception, ID NOT CHANGED!");
        }
    }

    public boolean isInDisconnectedList(String id) {
        return disconnectedPlayerIds.contains(id);
    }

    public List<String> getDisconnectedList() {
        return disconnectedPlayerIds;
    }
}
