package Server.Listener;

import Server.Messages.GeneralMessage;
public class GameOverListener implements Listener {
    public GameOverListener(){}

    @Override
    public void update(GeneralMessage data) {
        data.printData();
    }
}
