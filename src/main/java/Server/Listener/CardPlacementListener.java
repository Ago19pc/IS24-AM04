package main.java.Server.Listener;

import main.java.Server.Messages.GeneralMessage;
public class CardPlacementListener implements Listener {
    public CardPlacementListener(){}

    @Override
    public void update(GeneralMessage data) {
        data.printData();
    }
}
