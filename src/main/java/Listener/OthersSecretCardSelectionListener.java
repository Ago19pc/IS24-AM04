package main.java.Listener;

import main.java.Messages.GeneralMessage;
public class OthersSecretCardSelectionListener implements Listener {

    public OthersSecretCardSelectionListener(){}

    @Override
    public void update(GeneralMessage data) {
        data.printData();
    }
}
