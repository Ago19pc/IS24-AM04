package main.java.Listener;

import main.java.Messages.GeneralMessage;
public class PlayersOrderListener implements Listener {
    public PlayersOrderListener(){}

    @Override
    public void update(GeneralMessage data) {
        data.printData();
    }
}
