package main.java.Listener;

import main.java.Messages.GeneralMessage;
public class TimeoutListener implements Listener {
    public TimeoutListener(){}

    @Override
    public void update(GeneralMessage data) {
        data.printData();
    }

}
