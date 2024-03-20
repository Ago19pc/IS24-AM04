package main.java.Client;

import main.java.Enums.EventType;
import main.java.Messages.GeneralMessage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager {
    private final Map<EventType, List<Listener>> listeners = new HashMap<>();
    public void subscribe(EventType eventType, Listener listener) {
        listeners.get(eventType).add(listener);
    }
    public void unsubscribe(EventType eventType, Listener listener) {
        listeners.get(eventType).remove(listener);
    }
    public void notify(EventType eventType, GeneralMessage data) {
        for (Listener listener : listeners.get(eventType)) {
            listener.update(data);
        }
    }

}
