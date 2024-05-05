package ConnectionUtils;


import Server.Connections.ServerConnectionHandler;

public class MessageUtils {
    ServerConnectionHandler serverConnectionHandler;

    /**
     * Constructor for MessageUtils for server
     * @param serverConnectionHandler the server connection handler
     */
    public MessageUtils(ServerConnectionHandler serverConnectionHandler) {
        this.serverConnectionHandler = serverConnectionHandler;
    }

    /**
     * Constructor for MessageUtils for client
     */
    public MessageUtils() {}




}
