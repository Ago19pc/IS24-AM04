package ConnectionUtils;


import Server.Enums.MessageType;
import Server.Exception.IllegalMessageTypeException;
import Server.Messages.*;

import java.io.*;
import java.util.Base64;

/**
 * MessagePacket class is a wrapper class for GeneralMessage and EventType
 * It is used to send messages between server and client
 */
public class MessagePacket implements Serializable {
    /**
     * payload: GeneralMessage object
     * type: EventType object
     */
    GeneralMessage payload;
    MessageType type;

    /**
     * Constructor
     * @param payload: GeneralMessage object
     * @param type: EventType object
     */
    public MessagePacket(GeneralMessage payload, MessageType type) {
        this.payload = payload;
        this.type = type;
    }

    /**
     * Constructor used to regenerate the packet from a serialized string, aka deserialization
     * @param serialized: String
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public MessagePacket(String serialized) throws IOException, ClassNotFoundException, IllegalMessageTypeException {
        byte[] data = Base64.getDecoder().decode(serialized);

        ObjectInputStream oInputStream = new ObjectInputStream(new ByteArrayInputStream(data));
        MessagePacket restored = (MessagePacket) oInputStream.readObject();
        oInputStream.close();
        this.type = restored.getType();
        switch (this.type) {

            case BOARDINIT -> this.payload = (BoardInitMessage) restored.getPayload();
            case CARDPLACEMENT -> this.payload = (CardPlacementMessage) restored.getPayload();
            case CHAT -> this.payload = (ChatMessage) restored.getPayload();
            case DRAWCARD -> this.payload = (DrawCardMessage) restored.getPayload();
            case ENDGAMEPHASE -> this.payload = (EndGamePhaseMessage) restored.getPayload();
            case INITIALHAND -> this.payload = (InitialHandMessage) restored.getPayload();
            case LEADERBOARD -> this.payload = (LeaderboardMessage) restored.getPayload();
            case MATCHALREADYFULL -> this.payload = (MatchAlreadyFullMessage) restored.getPayload();
            case NEWPLAYER -> this.payload = (NewPlayerMessage) restored.getPayload();
            case NEWPOINTS -> this.payload = (NewPointsMessage) restored.getPayload();
            case NEWSYMBOLS -> this.payload = (NewSymbolsMessage) restored.getPayload();
            case NEXTTURN -> this.payload = (NextTurnMessage) restored.getPayload();
            case OTHERCARDPLACEMENT -> this.payload = (OtherCardPlacementMessage) restored.getPayload();
            case OTHERDRAWCARD -> this.payload = (OtherDrawCardMessage) restored.getPayload();
            case OTHERNEWPOINTS -> this.payload = (OtherNewPointsMessage) restored.getPayload();
            case OTHERRECONNECTION -> this.payload = (OtherReconnectionMessage) restored.getPayload();
            case OTHERSECRETCARD -> this.payload = (OtherSecretCardMessage) restored.getPayload();
            case OTHERSTARTINGCARD -> this.payload = (OtherStartingCardMessage) restored.getPayload();
            case PLAYERCOLOR -> this.payload = (PlayerColorMessage) restored.getPayload();
            case PLAYERNAME -> this.payload = (PlayerNameMessage) restored.getPayload();
            case PLAYERSORDER -> this.payload = (PlayersOrderMessage) restored.getPayload();
            case QUITORPLAYAGAIN -> this.payload = (QuitOrPlayAgainMessage) restored.getPayload();
            case READYSTATUS -> this.payload = (ReadyStatusMessage) restored.getPayload();
            case RECONNECTION -> this.payload = (ReconnectionMessage) restored.getPayload();
            case SECRETCARDS -> this.payload = (SecretCardsMessage) restored.getPayload();
            case TIMEOUTMESSAGE -> this.payload = (TimeoutMessage) restored.getPayload();
            case UNAVAIABLECOLORS -> this.payload = (UnavailableColorsMessage) restored.getPayload();
            case STARTINGCARDS -> this.payload = (StartingCardsMessage) restored.getPayload();
            default -> throw new IllegalMessageTypeException(this.type.toString());

        }
        this.payload = restored.getPayload();

    }

    /**
     * @return payload: GeneralMessage object, aka the payload of the packet (data but not the event info)
     */
    public GeneralMessage getPayload() {
        return payload;
    }

    /**
     * @return type: EventType object, aka the event type of the packet
     */
    public MessageType getType() {
        return type;
    }

    /**
     * Serialize the packet to a string
     * @return serialized: String, aka the serialized version of the packet
     * @throws IOException
     */
    public String stringify() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bos);
        os.writeObject(this);
        os.close();
        return Base64.getEncoder().encodeToString(bos.toByteArray());
    }

    /**
     * @return boolean: true if the two packets are equal, false otherwise
     */
    public boolean equals(MessagePacket other){
        return this.payload.equals(other.getPayload()) && this.type.equals(other.getType());
    }



}
