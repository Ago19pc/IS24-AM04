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

/**
 * This class is the endpoint for managing the connections to the clients.
 * It works by creating a ServerConnectionHandlerSOCKET and a ServerConnectionHandlerRMI and using their methods.
 * It also holds a map between player ids and names and a list of disconnected ids.
 */
public class GeneralServerConnectionHandler {
    private ServerConnectionHandlerSOCKET serverConnectionHandlerSOCKET;
    private ServerConnectionHandlerRMI serverConnectionHandlerRMI;
    /**
     * Map<Id, Name>
     */
    private final Map<String, String> playerID = new HashMap<>();
    private final List<String> disconnectedPlayerIds = new ArrayList<>();

    /**
     * Constructor
     */
    public GeneralServerConnectionHandler() {
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

    /**
     * Removes a client from the data structures by its name
     * @param name the name of the player whose client has to be removed
     */
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

    /**
     * Gets a player id by name
     * @param name the name
     * @return the id
     */
    public String getIdByName(String name) {
        return playerID.entrySet().stream().filter(entry -> entry.getValue().equals(name)).findFirst().get().getKey();
    }

    /**
     * Checks if a name is already linked to an id
     * @param name the name to check
     * @return true if the name is already linked to an id, false if it's free
     */
    public Boolean isNameConnectedToId(String name) {
        return playerID.containsValue(name);
    }

    /**
     * Returns the socket connection handler as a ServerConnectionHandler
     * @return the socket connection handler
     */
    public ServerConnectionHandler getServerConnectionHandler() {
        return serverConnectionHandlerSOCKET;
    }

    /**
     * Returns the server connection handler corresponding to the specific client's connection mode
     * @param id the client's id
     * @return the appropriate server connection handler
     * @throws PlayerNotInAnyServerConnectionHandlerException if the player is not found in any server connection handler (i.e. the player is not connected)
     */
    public ServerConnectionHandler getServerConnectionHandler(String id) throws PlayerNotInAnyServerConnectionHandlerException {
        if (serverConnectionHandlerRMI.isClientAvailable(id)) {
            return serverConnectionHandlerRMI;
        } else if (serverConnectionHandlerSOCKET.isClientAvailable(id)) {
            return serverConnectionHandlerSOCKET;
        } else throw new PlayerNotInAnyServerConnectionHandlerException();

    }
    /**
     * Returns the RMI connection handler
     * @return the socket connection handler
     */
    public ServerConnectionHandlerRMI getServerConnectionHandlerRMI() {
        return serverConnectionHandlerRMI;
    }

    /**
     * Sends a message to all the clients
     * @param message the message to send
     */
    public void sendAllMessage(ToClientMessage message) {
        serverConnectionHandlerSOCKET.sendAllMessage(message);
        serverConnectionHandlerRMI.sendAllMessage(message);
    }

    /**
     * Sends a message to a specific client by name
     * @param message the message to send
     * @param name the name of the client to send the message to
     */
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

    /**
     * Creates the connection handlers and sets their controller
     * @param controller the controller to set
     */
    public void setController(Controller controller) {
        serverConnectionHandlerRMI = new ServerConnectionHandlerRMI();
        serverConnectionHandlerRMI.setController(controller);
        serverConnectionHandlerSOCKET = new ServerConnectionHandlerSOCKET();
        serverConnectionHandlerSOCKET.setController(controller);
    }

    /**
     * Starts the socket connection handler and starts a new ping pong thread
     */
    public void start() {

        PingPong pingPong = new PingPong(this);
        pingPong.start();
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

    /**
     * Sets a player as online i.e. removes it from the to disconnect list
     * @param id the player's id
     */
    public void setOnline(String id) {
        disconnectedPlayerIds.remove(id);
        System.out.println("Player " + getPlayerNameByID(id) + " is now set online");
    }
    /**
     * Checks if an id is associated to a player
     * @param id the id to check
     * @return true if the id is associated to a player, false otherwise
     */
    public boolean isIdConnectedToName(String id) {
        return playerID.containsKey(id);
    }

    /**
     * Changes the client id of a player
     * @param name the player's name
     * @param id the new id
     */
    public void changePlayerId(String name, String id) {
        String idToRemove = playerID.entrySet().stream().filter(entry -> entry.getValue().equals(name)).findFirst().get().getKey();
        playerID.remove(idToRemove);
        playerID.put(id, name);
        try{
            getServerConnectionHandler(idToRemove).killClient(idToRemove);
        } catch (PlayerNotInAnyServerConnectionHandlerException e) {
            System.err.println("Player not found in any server connection handler, ID NOT CHANGED!");
        } catch (RemoteException e) {
            System.err.println("Remote exception, ID NOT CHANGED!");
        }
    }
    /**
     * Checks if a player is in the disconnected list
     * @param id the player's id
     * @return true if the player is in the disconnected list, false otherwise
     */
    public boolean isInDisconnectedList(String id) {
        return disconnectedPlayerIds.contains(id);
    }

    /**
     * Returns the list of disconnected players
     * @return the list of disconnected players' ids
     */
    public List<String> getDisconnectedList() {
        return disconnectedPlayerIds;
    }

    /**
     * Clears the server preparing it for a new game
     */
    public void clear(){
        playerID.clear();
        disconnectedPlayerIds.clear();
    }
}
