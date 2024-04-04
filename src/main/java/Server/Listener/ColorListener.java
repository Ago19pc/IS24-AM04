package Server.Listener;

import Server.Messages.GeneralMessage;

public class ColorListener implements Listener {
    public  ColorListener(){}

    @Override
    public void update(GeneralMessage data) {
        data.printData();
    }
}
