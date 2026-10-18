package rush.hour.event.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rush.hour.event.GameEvent;
import rush.hour.event.GameEventListener;
import rush.hour.event.GameEventType;

public class AuditLoggerListener implements GameEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditLoggerListener.class);

    @Override
    public void onEvent(GameEvent event){
        if(event.type() == GameEventType.ERROR){
            LOGGER.error(event.message());
        }
        else if(event.type() == GameEventType.WARN){
            LOGGER.warn(event.message());
        }
        else if(event.type() == GameEventType.INFO){
            LOGGER.info(event.message());
        }
        else{
            LOGGER.debug(event.message());
        }
    }
}
