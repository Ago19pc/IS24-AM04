package ConnectionUtils;

import Payloads.PlayerNamePayload;
import Server.Enums.EventType;
import Server.EventManager.EventManager;
import Server.Messages.PlayerNameMessage;
import Server.Messages.PlayersMessage;
import Server.Player.Player;
import Server.Player.PlayerInstance;
import com.google.gson.Gson;


import java.util.ArrayList;
import java.util.List;

import static Server.Enums.EventType.PLAYERSDATA;

public class MessageUtils {

    EventManager eventManager;

    public MessageUtils(EventManager eventManager){
        this.eventManager = eventManager;
    }

    //server side demux
    public void server_demux(String message){
        Gson gson = new Gson();
        MessagePacket messagePacket = gson.fromJson(message, MessagePacket.class);
        switch (messagePacket.getType()){
            case PLAYERSDATA:
                PlayerNamePayload payload = (PlayerNamePayload) messagePacket.getPayload();
                PlayerNameMessage playerNameMessage = new PlayerNameMessage(payload.getPlayerName());

                eventManager.notify(PLAYERSDATA, playerNameMessage);
                break;
        }

    }


}
