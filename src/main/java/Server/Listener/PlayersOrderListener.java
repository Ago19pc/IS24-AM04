package main.java.Server.Listener;

import main.java.Server.Messages.GeneralMessage;
public class PlayersOrderListener implements Listener {
    public PlayersOrderListener(){}

    @Override
    public void update(GeneralMessage data) {
        data.printData();
    }
}
