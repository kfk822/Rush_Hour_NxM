package rush.hour.service.ui.printer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rush.hour.model.State;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ConsolePrinterTest {

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private ConsolePrinter consolePrinter;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
        consolePrinter = new ConsolePrinter();
    }

    @AfterEach
    void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    void testPrintGameStateChoicesShouldPrintMenuWhenStateIsBeforeGame() {
        consolePrinter.printGameStateChoices(State.BEFORE_GAME);

        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("What do you want to do?"));
        assertTrue(output.contains("(1) Play game."));
        assertTrue(output.contains("(2) Load previous game. [NOT AVAILABLE!]"));
        assertTrue(output.contains("(3) Show leaderboard. [NOT AVAILABLE!]"));
    }

    @Test
    void testPrintGameStateChoicesShouldPrintPromptWhenStateIsInGame() {
        consolePrinter.printGameStateChoices(State.IN_GAME);

        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("Make your move!"));
        assertTrue(output.contains("Move format [(Vehicle head position)->(New Vehicle head position)]: a1->a2"));
        assertTrue(output.contains("(quit) Quit the game."));
    }

    @Test
    void testPrintMessageShouldPrintGivenString() {
        consolePrinter.printMessage("Hello Rush Hour!");

        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("Hello Rush Hour!"));
    }
}