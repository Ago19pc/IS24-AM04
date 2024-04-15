package Server.Listener;

import Server.GameModel.GameModel;
import Server.Messages.GeneralMessage;
import Server.Messages.PlayerNameMessage;
import Server.Messages.PlayersMessage;
import Server.Player.Player;
import Server.Player.PlayerInstance;

import java.util.List;

public class PlayersDataListener implements Listener {
    GameModel gm;
    public PlayersDataListener(GameModel gm){
        this.gm = gm;
    }

    @Override
    public void update(GeneralMessage data) {
        data.printData();
    }

    public void update(PlayersMessage data){
        System.out.println("TODO");
    }

    public void update(PlayerNameMessage data){
        System.out.println("PlayersDataListener: updating player name");
        gm.addPlayer(new PlayerInstance(data.getName(), gm.getEventManager()));
    }
}
