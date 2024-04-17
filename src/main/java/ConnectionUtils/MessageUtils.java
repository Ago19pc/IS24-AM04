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
            case SET_COLOR:
                // ColorMessage payload = (ColorMessage) messagePacket.getPayload();
                // CALL CONTROLLER RELATIVE ACTION
                break;
            case PLAYERSORDER:
                // PlayersOrderMessage payload = (PlayersOrderMessage) messagePacket.getPayload();
                // CALL CONTROLLER RELATIVE ACTION
                break;
            case SECRETCARDSELECTION:
                // SecretCardSelectionMessage payload = (SecretCardSelectionMessage) messagePacket.getPayload();
                // CALL CONTROLLER RELATIVE ACTION
                break;
            case OTHERSSECRETCARDSELECTION:
                // OthersSecretCardSelectionMessage payload = (OthersSecretCardSelectionMessage) messagePacket.getPayload();
                // CALL CONTROLLER RELATIVE ACTION
                break;
            case STARTINGCARDS:
                // StartingCardsMessage payload = (StartingCardsMessage) messagePacket.getPayload();
                // CALL CONTROLLER RELATIVE ACTION
                break;
            case OTHERSSTARTINGCARDS:
                // OthersStartingCardsMessage payload = (OthersStartingCardsMessage) messagePacket.getPayload();
                // CALL CONTROLLER RELATIVE ACTION
                break;
            case DRAWCARD:
                // DrawCardMessage payload = (DrawCardMessage) messagePacket.getPayload();
                // CALL CONTROLLER RELATIVE ACTION
                break;
            case TIMEOUT:
                // TimeoutMessage payload = (TimeoutMessage) messagePacket.getPayload();
                // CALL CONTROLLER RELATIVE ACTION
                break;
            case NEXTTURN:
                // NextTurnMessage payload = (NextTurnMessage) messagePacket.getPayload();
                // CALL CONTROLLER RELATIVE ACTION
                break;
            case CARDPLACEMENT:
                // CardPlacementMessage payload = (CardPlacementMessage) messagePacket.getPayload();
                // CALL CONTROLLER RELATIVE ACTION
                break;
            case ENDGAMEPHASE:
                // EndGamePhaseMessage payload = (EndGamePhaseMessage) messagePacket.getPayload();
                // CALL CONTROLLER RELATIVE ACTION
                break;
            case LEADERBOARD:
                // LeaderboardMessage payload = (LeaderboardMessage) messagePacket.getPayload();
                // CALL CONTROLLER RELATIVE ACTION
                break;
            case GAMEOVER:
                // GameOverMessage payload = (GameOverMessage) messagePacket.getPayload();
                // CALL CONTROLLER RELATIVE ACTION
                break;
            case NEWMESSAGE:
                // NewMessageMessage payload = (NewMessageMessage) messagePacket.getPayload();
                // CALL CONTROLLER RELATIVE ACTION
                break;
            case RECONNECTION:
                // ReconnectionMessage payload = (ReconnectionMessage) messagePacket.getPayload();
                // CALL CONTROLLER RELATIVE ACTION
                break;
            default:
                break;

        }

    }


}
