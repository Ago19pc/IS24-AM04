package main.java.Listener;

import main.java.Messages.GeneralMessage;
public class OthersStartingCardListener implements Listener {
    public OthersStartingCardListener(){}

    @Override
    public void update(GeneralMessage data) {
        data.printData();
    }
}
