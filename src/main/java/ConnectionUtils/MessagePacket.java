package ConnectionUtils;

import Payloads.GeneralPayload;
import Server.Enums.EventType;
import Server.Messages.GeneralMessage;

public class MessagePacket {
    GeneralPayload payload;
    EventType type;

    public MessagePacket(GeneralPayload payload, EventType type) {
        this.payload = payload;
        this.type = type;
    }

    public GeneralPayload getPayload() {
        return payload;
    }

    public EventType getType() {
        return type;
    }


}
