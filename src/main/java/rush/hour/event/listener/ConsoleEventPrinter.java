package rush.hour.event.listener;

import rush.hour.event.GameEvent;
import rush.hour.event.GameEventListener;

public class ConsoleEventPrinter implements GameEventListener {

    @Override
    public void onEvent(GameEvent event) {
        System.out.println(event.message());
    }
}
