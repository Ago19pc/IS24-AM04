package Server.Listener;

import Server.Messages.GeneralMessage;
public class LeaderboardListener implements Listener {
    public LeaderboardListener(){}

    @Override
    public void update(GeneralMessage data) {
        data.printData();
    }
}
