package Server.Listener;

import Server.Messages.GeneralMessage;

public class ReconnectionListener implements Listener {
    public ReconnectionListener(){}

    @Override
    public void update(GeneralMessage data) {
        data.printData();
    }
}
