package main.java.Server.Listener;

import main.java.Server.Messages.GeneralMessage;
public class GameOverListener implements Listener {
    public GameOverListener(){}

    @Override
    public void update(GeneralMessage data) {
        data.printData();
    }
}
