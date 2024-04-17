package ConnectionUtils;


import Server.Messages.PlayerNameMessage;

import java.io.IOException;

public class MessageUtils {


    //server side demux
    public void server_demux(String message) throws IOException, ClassNotFoundException {
        MessagePacket messagePacket = new MessagePacket(message);
        switch (messagePacket.getType()){
            case PLAYERNAME:
                PlayerNameMessage payload = (PlayerNameMessage) messagePacket.getPayload();
                // CALL CONTROLLER RELATIVE ACTION
                break;
            case SET_COLOR:
                // ColorMessage payload = (ColorMessage) messagePacket.getPayload();
                // CALL CONTROLLER RELATIVE ACTION
                break;
        }

    }


}
