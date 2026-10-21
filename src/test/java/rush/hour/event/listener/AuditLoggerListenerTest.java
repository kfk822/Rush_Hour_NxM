package rush.hour.event.listener;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import rush.hour.event.GameEvent;
import rush.hour.event.GameEventType;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuditLoggerListenerTest {

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private AuditLoggerListener auditLoggerListener;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
        auditLoggerListener = new AuditLoggerListener();
    }

    @AfterEach
    void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    @DisplayName("Should capture error event details when error event occurs")
    void testOnEventShouldLogErrorMessageWhenErrorEventOccurs() {

        GameEvent errorEvent = new GameEvent(GameEventType.ERROR, "Invalid coordinate detected");

        auditLoggerListener.onEvent(errorEvent);

        String printedOutput = outputStreamCaptor.toString();
        assertTrue(printedOutput.contains("ERROR"));
        assertTrue(printedOutput.contains("Invalid coordinate detected"));
    }

}
