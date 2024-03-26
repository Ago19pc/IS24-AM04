package main.java.Server.Listener;

import main.java.Server.Messages.GeneralMessage;

public class ReconnectionListener implements Listener {
    public ReconnectionListener(){}

    @Override
    public void update(GeneralMessage data) {
        data.printData();
    }
}
