package rush.hour.event.listener;

import rush.hour.event.GameEvent;
import rush.hour.event.GameEventListener;
import rush.hour.event.GameEventType;

public class ErrorCounter implements GameEventListener {
    private int errorCount = 0;

    @Override
    public void onEvent(GameEvent event){
        if(event.type() == GameEventType.ERROR){
            errorCount++;
        }
    }

    public int getErrorCount(){
        return errorCount;
    }
}
