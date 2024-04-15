package Server.EventManager;

import Server.Listener.Listener;
import Server.Enums.EventType;
import Server.Messages.GeneralMessage;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EventManager {
    private final Map<EventType, List<Listener>> listeners;
    public EventManager(){
        listeners = new HashMap<>();
        for (EventType eventType : EventType.values())
            listeners.put(eventType, new LinkedList<Listener>());
        //listeners.put(EventType.SET_COLOR, new LinkedList<Listener>());
        //listeners.put(EventType.PLAYERSDATA, new LinkedList<Listener>());
    }
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
