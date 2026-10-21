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

public class ConsoleEventPrinterTest {

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private ConsoleEventPrinter consoleEventPrinter;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
        consoleEventPrinter = new ConsoleEventPrinter();
    }

    @AfterEach
    void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    @DisplayName("Should print event message to standard output when event is received")
    void testOnEventShouldPrintMessageWhenEventReceived() {
        GameEvent event = new GameEvent(GameEventType.DEBUG, "Map parsing completed");

        consoleEventPrinter.onEvent(event);

        String printedOutput = outputStreamCaptor.toString();
        assertTrue(printedOutput.contains("Map parsing completed"));
    }
}
