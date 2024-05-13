package ConnectionUtils;


import Server.Exception.IllegalMessageTypeException;
import Server.Messages.ToServerMessage;

import java.io.*;
import java.util.Base64;

/**
 * MessagePacket class is a wrapper class for GeneralMessage and EventType
 * It is used to send messages between server and client
 */
public class ToServerMessagePacket implements Serializable {
    /**
     * payload: GeneralMessage object
     * type: EventType object
     */
    ToServerMessage payload;

    /**
     * Constructor
     * @param payload: GeneralMessage object

     */
    public ToServerMessagePacket(ToServerMessage payload) {
        this.payload = payload;
    }

    /**
     * Constructor used to regenerate the packet from a serialized string, aka deserialization
     * @param serialized: String
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public ToServerMessagePacket(String serialized) throws IOException, ClassNotFoundException, IllegalMessageTypeException {
        byte[] data = Base64.getDecoder().decode(serialized);

        ObjectInputStream oInputStream = new ObjectInputStream(new ByteArrayInputStream(data));
        this.payload =  ((ToServerMessagePacket) oInputStream.readObject()).getPayload();
        oInputStream.close();
    }

    /**
     * @return payload: GeneralMessage object, aka the payload of the packet (data but not the event info)
     */
    public ToServerMessage getPayload() {
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
    public boolean equals(ToServerMessagePacket other){
        return this.payload.equals(other.getPayload());
    }



}
