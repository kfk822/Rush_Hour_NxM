package rush.hour.event.listener;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import rush.hour.event.GameEvent;
import rush.hour.event.GameEventType;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ErrorCounterTest {

    ErrorCounter errorCounter;

    @BeforeEach
    void setUp() {
        errorCounter = new ErrorCounter();
    }

    @Test
    @DisplayName("Error counter should return 0 when no errors occurred")
    void testGetErrorCountShouldReturnZeroWhenNoErrorsOccurred() {
        assertEquals(0, errorCounter.getErrorCount());
    }

    @Test
    @DisplayName("Should only increment count for error events when different event types are received")
    void testOnEventShouldOnlyCountErrorsWhenMultipleEventTypesReceived() {
        GameEvent debugEvent = new GameEvent(GameEventType.DEBUG, "Debug event");
        GameEvent infoEvent = new GameEvent(GameEventType.INFO, "Info event");
        GameEvent warnEvent = new GameEvent(GameEventType.WARN, "Warn event");
        GameEvent errorEvent = new GameEvent(GameEventType.ERROR, "Error event");

        errorCounter.onEvent(debugEvent);
        errorCounter.onEvent(infoEvent);
        errorCounter.onEvent(warnEvent);
        errorCounter.onEvent(errorEvent);

        assertEquals(1, errorCounter.getErrorCount());
    }

    @Test
    @DisplayName("Error counter should count multiple errors")
    void testOnEventShouldCountMultipleErrorsWhenMultipleErrorsReceived() {
        GameEvent errorEvent = new GameEvent(GameEventType.ERROR, "Error event");

        errorCounter.onEvent(errorEvent);
        errorCounter.onEvent(errorEvent);
        errorCounter.onEvent(errorEvent);
        errorCounter.onEvent(errorEvent);

        assertEquals(4, errorCounter.getErrorCount());
    }

}
