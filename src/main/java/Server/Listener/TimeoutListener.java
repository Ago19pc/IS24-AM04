package Server.Listener;

import Server.Messages.GeneralMessage;
public class TimeoutListener implements Listener {
    public TimeoutListener(){}

    @Override
    public void update(GeneralMessage data) {
        data.printData();
    }

}
