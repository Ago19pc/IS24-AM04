package ConnectionUtils;


import Server.Connections.ServerConnectionHandlerSOCKET;

public class MessageUtils {
    ServerConnectionHandlerSOCKET serverConnectionHandler;

    /**
     * Constructor for MessageUtils for server
     * @param serverConnectionHandler the server connection handler
     */
    public MessageUtils(ServerConnectionHandlerSOCKET serverConnectionHandler) {
        this.serverConnectionHandler = serverConnectionHandler;
    }

    /**
     * Constructor for MessageUtils for client
     */
    public MessageUtils() {}




}
