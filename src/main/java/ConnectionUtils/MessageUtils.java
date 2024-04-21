package ConnectionUtils;


import Server.Connections.ServerConnectionHandler;
import Server.Messages.PlayerNameMessage;

import java.io.IOException;

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

    /**
     * Demultiplexes the message and calls the appropriate method
     * @param message the message to demultiplex
     * @param threadID the thread ID of the client
     * @throws IOException if the server connection handler is not set
     * @throws ClassNotFoundException if the message type is not recognized
     */
    public void server_demux(String message, Long threadID) throws IOException, ClassNotFoundException {
        if (serverConnectionHandler == null) {
            throw new IOException("ServerConnectionHandler not set, put parameter in MessageUtils constructor");
        }
        MessagePacket messagePacket = new MessagePacket(message);
        switch (messagePacket.getType()){
            case PLAYERNAME:
                PlayerNameMessage payload = (PlayerNameMessage) messagePacket.getPayload();
                // SETTA IL NOME ANCHE NEL ServerConnectionHandler O AGGIORNA IL NUOVO
                serverConnectionHandler.addClientName(threadID, payload.getName()); // GESISCE ANCHE LA RICONNESSIONE PER IL MODULO DI CONNESSIONI

                // CALL CONTROLLER RELATIVE ACTION CHE DEVE SETTARE ONLINE_STATUS A TRUE SE IL PLAYER GIA ESISTE
                break;
            case PLAYERCOLOR:
                break;
            case UNAVAILABLECOLORS:
                break;
            case PLAYERSORDER:
                break;
            case NEWPLAYER:
                break;
            case SECRETCARDSELECTION:
                break;
            case OTHERSSECRETCARD:
                break;
            case STARTINGCARD:
                break;
            case OTHERSSTARTINGCARD:
                break;
            case DRAWCARD:
                break;
            case OTHERSDRAWCARD:
                break;
            case TIMEOUT:
                break;
            case NEXTTURN:
                break;
            case CARDPLACEMENT:
                break;
            case OTHERSCARDPLACEMENT:
                break;
            case ENDGAMEPHASE:
                break;
            case LEADERBOARD:
                break;
            case GAMEOVER:
                break;
            case NEWMESSAGE:
                break;
            case RECONNECTION:
                break;
            case OHTERRECONECTION:
                break;
            case READYSTATUS:
                break;
            case BOARDINIT:
                break;
            case NEWPOINTS:
                break;
            case OTHERSNEWPOINTS:
                break;
            case NEWSYMBOLS:
                break;
            case QUITORPLAYAGAIN:
                break;
            case MATCHALREADYFULL:
                break;
            default:
                break;

        }

    }

    public void client_demux (String message) throws IOException, ClassNotFoundException {
        MessagePacket messagePacket = new MessagePacket(message);
        switch (messagePacket.getType()){
            case PLAYERNAME:
                break;
            case PLAYERCOLOR:
                break;
            case UNAVAILABLECOLORS:
                break;
            case PLAYERSORDER:
                break;
            case NEWPLAYER:
                break;
            case SECRETCARDSELECTION:
                break;
            case OTHERSSECRETCARD:
                break;
            case STARTINGCARD:
                break;
            case OTHERSSTARTINGCARD:
                break;
            case DRAWCARD:
                break;
            case OTHERSDRAWCARD:
                break;
            case TIMEOUT:
                break;
            case NEXTTURN:
                break;
            case CARDPLACEMENT:
                break;
            case OTHERSCARDPLACEMENT:
                break;
            case ENDGAMEPHASE:
                break;
            case LEADERBOARD:
                break;
            case GAMEOVER:
                break;
            case NEWMESSAGE:
                break;
            case RECONNECTION:
                break;
            case OHTERRECONECTION:
                break;
            case READYSTATUS:
                break;
            case BOARDINIT:
                break;
            case NEWPOINTS:
                break;
            case OTHERSNEWPOINTS:
                break;
            case NEWSYMBOLS:
                break;
            case QUITORPLAYAGAIN:
                break;
            case MATCHALREADYFULL:
                break;

        }
    }

}

