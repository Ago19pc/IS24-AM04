package main.java.Listener;

import main.java.Messages.GeneralMessage;
public class DrawCardListener implements Listener {
    public DrawCardListener(){}

    @Override
    public void update(GeneralMessage data) {
        data.printData();
    }
}
