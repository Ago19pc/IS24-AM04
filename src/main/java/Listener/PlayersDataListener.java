package main.java.Listener;

import main.java.Messages.GeneralMessage;

public class PlayersDataListener implements Listener {
    public PlayersDataListener(){}

    @Override
    public void update(GeneralMessage data) {
        data.printData();
    }
}
