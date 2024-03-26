package main.java.Server.Listener;

import main.java.Server.Messages.GeneralMessage;

public class PlayersDataListener implements Listener {
    public PlayersDataListener(){}

    @Override
    public void update(GeneralMessage data) {
        data.printData();
    }
}
