package Server.Listener;

import Server.Messages.GeneralMessage;
public class OthersStartingCardListener implements Listener {
    public OthersStartingCardListener(){}

    @Override
    public void update(GeneralMessage data) {
        data.printData();
    }
}
