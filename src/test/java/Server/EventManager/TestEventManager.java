package Server.EventManager;

import Server.Enums.EventType;
import Server.Listener.ColorListener;
import Server.Listener.DrawCardListener;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestEventManager {
    @Test
    public void testSubscribe() {
        EventManager eventManager = new EventManager();
        ColorListener colorListener = new ColorListener();
        eventManager.subscribe(EventType.SET_COLOR, colorListener);
        assertEquals(1, eventManager.getListenersByEventType(EventType.SET_COLOR).size());
        assertTrue(eventManager.getListenersByEventType(EventType.SET_COLOR).contains(colorListener));
    }
    @Test
    public void testUnsubscribe() {
        EventManager eventManager = new EventManager();
        ColorListener colorListener = new ColorListener();
        DrawCardListener drawCardListener = new DrawCardListener();
        eventManager.subscribe(EventType.DRAWCARD, drawCardListener);
        eventManager.subscribe(EventType.SET_COLOR, colorListener);
        eventManager.unsubscribe(EventType.SET_COLOR, colorListener);
        assertEquals(0, eventManager.getListenersByEventType(EventType.SET_COLOR).size());
        assertTrue(eventManager.getListenersByEventType(EventType.DRAWCARD).contains(drawCardListener));
    }
}
