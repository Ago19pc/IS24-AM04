package ConnectionUtils;

import Server.Exception.IllegalMessageTypeException;
import Server.Messages.ToClientMessage;

import java.io.*;
import java.util.Base64;

/**
 * MessagePacket class is a wrapper class for GeneralMessage and EventType
 * It is used to send messages between server and client
 */
public class ToClientMessagePacket implements Serializable {
    /**
     * payload: GeneralMessage object
     * type: EventType object
     */
    ToClientMessage payload;

    /**
     * Constructor
     * @param payload: GeneralMessage object

     */
    public ToClientMessagePacket(ToClientMessage payload) {
        this.payload = payload;
    }

    /**
     * Constructor used to regenerate the packet from a serialized string, aka deserialization
     * @param serialized: String
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public ToClientMessagePacket(String serialized) throws IOException, ClassNotFoundException, IllegalMessageTypeException {
        byte[] data = Base64.getDecoder().decode(serialized);

        ObjectInputStream oInputStream = new ObjectInputStream(new ByteArrayInputStream(data));
        this.payload =  ((ConnectionUtils.ToClientMessagePacket) oInputStream.readObject()).getPayload();
        oInputStream.close();
    }

    /**
     * @return payload: GeneralMessage object, aka the payload of the packet (data but not the event info)
     */
    public ToClientMessage getPayload() {
        return payload;
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
    public boolean equals(ConnectionUtils.ToClientMessagePacket other){
        return this.payload.equals(other.getPayload());
    }



}
