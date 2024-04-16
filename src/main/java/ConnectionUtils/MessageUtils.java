package ConnectionUtils;

import Server.Messages.PlayerNameMessage;
import com.google.gson.Gson;

public class MessageUtils {


    //server side demux
    public void server_demux(String message){
        Gson gson = new Gson();
        MessagePacket messagePacket = gson.fromJson(message, MessagePacket.class);
        switch (messagePacket.getType()){
            case PLAYERNAME:
                PlayerNameMessage payload = (PlayerNameMessage) messagePacket.getPayload();


                break;
        }

    }


}
