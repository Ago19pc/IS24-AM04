package main.java.Listener;

import main.java.Messages.GeneralMessage;
public class LeaderboardListener implements Listener {
    public LeaderboardListener(){}

    @Override
    public void update(GeneralMessage data) {
        data.printData();
    }
}
