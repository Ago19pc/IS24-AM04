package main.java.Server.Listener;

import main.java.Server.Messages.GeneralMessage;
public class EndGamePhaseListener implements Listener {
    public EndGamePhaseListener(){}

    @Override
    public void update(GeneralMessage data) {
        data.printData();
    }
}
