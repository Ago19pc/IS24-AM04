package main.java.Server.Listener;

import main.java.Server.Messages.GeneralMessage;
public class OthersStartingCardListener implements Listener {
    public OthersStartingCardListener(){}

    @Override
    public void update(GeneralMessage data) {
        data.printData();
    }
}
