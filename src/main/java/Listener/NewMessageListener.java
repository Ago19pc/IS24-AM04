package main.java.Listener;

import main.java.Messages.GeneralMessage;

public class NewMessageListener implements Listener {
    public NewMessageListener(){}

    @Override
    public void update(GeneralMessage data) {
        data.printData();
    }
}
