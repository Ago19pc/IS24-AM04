package main.java.Client;

import main.java.Enums.EventType;
import main.java.Messages.GeneralMessage;

public class ColorListener implements Listener {
    public ColorListener(EventManager eventManager) {
        eventManager.subscribe(EventType.SET_COLOR, this);
    }

    @Override
    public void update(GeneralMessage data) {
        data.printData();
    }
}
