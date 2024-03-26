package main.java.Listener;

import main.java.Messages.GeneralMessage;
public class GameOverListener implements Listener {
    public GameOverListener(){}

    @Override
    public void update(GeneralMessage data) {
        data.printData();
    }
}
