package ConnectionUtils;


import Server.Enums.EventType;
import Server.Messages.GeneralMessage;

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
    EventType type;

    /**
     * Constructor
     * @param payload: GeneralMessage object
     * @param type: EventType object
     */
    public MessagePacket(GeneralMessage payload, EventType type) {
        this.payload = payload;
        this.type = type;
    }

    /**
     * Constructor used to regenerate the packet from a serialized string, aka deserialization
     * @param serialized: String
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public MessagePacket(String serialized) throws IOException, ClassNotFoundException {
        byte[] data = Base64.getDecoder().decode(serialized);
        ObjectInputStream oInputStream = new ObjectInputStream(new ByteArrayInputStream(data));
        MessagePacket restored = (MessagePacket) oInputStream.readObject();
        oInputStream.close();

        this.payload = restored.getPayload();
        this.type = restored.getType();
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
    public EventType getType() {
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
