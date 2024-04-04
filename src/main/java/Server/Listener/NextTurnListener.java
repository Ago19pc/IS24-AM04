package Server.Listener;

import Server.Messages.GeneralMessage;
public class NextTurnListener implements Listener {
    public NextTurnListener(){}

    @Override
    public void update(GeneralMessage data) {
        data.printData();
    }
}
