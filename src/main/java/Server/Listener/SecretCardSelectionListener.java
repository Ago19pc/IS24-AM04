package Server.Listener;

import Server.Messages.GeneralMessage;
public class SecretCardSelectionListener implements Listener {
    public SecretCardSelectionListener(){}

    @Override
    public void update(GeneralMessage data) {
        data.printData();
    }
}
