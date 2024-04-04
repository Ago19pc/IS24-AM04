package Server.Listener;

import Server.Messages.GeneralMessage;
public class StartingCardListener implements  Listener {
    public StartingCardListener(){}

    @Override
    public void update(GeneralMessage data) {
        data.printData();
    }
}
