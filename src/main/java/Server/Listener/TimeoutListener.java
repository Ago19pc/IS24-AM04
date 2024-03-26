package main.java.Server.Listener;

import main.java.Server.Messages.GeneralMessage;
public class TimeoutListener implements Listener {
    public TimeoutListener(){}

    @Override
    public void update(GeneralMessage data) {
        data.printData();
    }

}
