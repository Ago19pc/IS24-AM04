package main.java.Server.Listener;

import main.java.Server.Messages.GeneralMessage;
public class DrawCardListener implements Listener {
    public DrawCardListener(){}

    @Override
    public void update(GeneralMessage data) {
        data.printData();
    }
}
