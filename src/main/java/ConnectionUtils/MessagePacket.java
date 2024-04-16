package ConnectionUtils;


import Server.Enums.EventType;
import Server.Messages.GeneralMessage;

import java.io.*;
import java.util.Base64;

public class MessagePacket implements Serializable {
    GeneralMessage payload;
    EventType type;

    public MessagePacket(GeneralMessage payload, EventType type) {
        this.payload = payload;
        this.type = type;
    }

    public MessagePacket(String serialized) throws IOException, ClassNotFoundException {
        byte[] data = Base64.getDecoder().decode(serialized);

        ObjectInputStream oInputStream = new ObjectInputStream(new ByteArrayInputStream(data));
        MessagePacket restored = (MessagePacket) oInputStream.readObject();
        oInputStream.close();
        this.payload = restored.getPayload();
        this.type = restored.getType();
    }
    public GeneralMessage getPayload() {
        return payload;
    }

    public EventType getType() {
        return type;
    }

    public String stringify() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bos);
        os.writeObject(this);
        os.close();
        return Base64.getEncoder().encodeToString(bos.toByteArray());
    }

    //public boolean equals(MessagePacket other){
    //    return this.payload.equals(other.getPayload()) && this.type.equals(other.getType());
    //}

}
