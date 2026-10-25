package rush.hour.service.ui.validator;

import org.junit.jupiter.api.Test;
import rush.hour.model.InputType;
import rush.hour.model.State;
import rush.hour.service.ui.printer.ConsolePrinter;
import rush.hour.service.ui.reader.ConsoleReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InputValidatorTest {
    @Test
    void testGetValidInputShouldReturnUsernameWhenImmediatelyValid() {
        StubConsoleReader reader = new StubConsoleReader("Player1");
        SpyConsolePrinter printer = new SpyConsolePrinter();
        InputValidator inputValidator = new InputValidator(reader, printer);

        String result = inputValidator.getValidInput(State.BEFORE_GAME, InputType.NAME);

        assertEquals("Player1", result);
        assertTrue(printer.messages.contains("What is your username?"));
    }

    @Test
    void testGetValidInputShouldRetryWhenUsernameIsEmptyAtFirst() {
        StubConsoleReader reader = new StubConsoleReader("", "ValidUser");
        SpyConsolePrinter printer = new SpyConsolePrinter();
        InputValidator inputValidator = new InputValidator(reader, printer);

        String result = inputValidator.getValidInput(State.BEFORE_GAME, InputType.NAME);

        assertEquals("ValidUser", result);
        assertTrue(printer.messages.contains("Input can't be empty!"));
        assertEquals(2, reader.readCount);
    }

    @Test
    void testGetValidInputShouldReturnChoiceWhenNumberIsInRange() {
        StubConsoleReader reader = new StubConsoleReader("1");
        SpyConsolePrinter printer = new SpyConsolePrinter();
        InputValidator inputValidator = new InputValidator(reader, printer);

        String result = inputValidator.getValidInput(State.BEFORE_GAME, InputType.BEFORE_GAME_CHOICE);

        assertEquals("1", result);
        assertTrue(printer.printedStates.contains(State.BEFORE_GAME));
    }

    @Test
    void testGetValidInputShouldRetryWhenChoiceIsInvalidNumberOrOutOfRange() {
        StubConsoleReader reader = new StubConsoleReader("abc", "4", "2");
        SpyConsolePrinter printer = new SpyConsolePrinter();
        InputValidator inputValidator = new InputValidator(reader, printer);

        String result = inputValidator.getValidInput(State.BEFORE_GAME, InputType.BEFORE_GAME_CHOICE);

        assertEquals("2", result);
        long errorCount = printer.messages.stream().filter("Invalid choice!"::equals).count();
        assertEquals(2, errorCount);
        assertEquals(3, reader.readCount);
    }

    @Test
    void testGetValidInputShouldReturnQuitWhenQuitIsEntered() {
        StubConsoleReader reader = new StubConsoleReader("quit");
        SpyConsolePrinter printer = new SpyConsolePrinter();
        InputValidator inputValidator = new InputValidator(reader, printer);

        String result = inputValidator.getValidInput(State.IN_GAME, InputType.IN_GAME_CHOICE);

        assertEquals("quit", result);
    }

    @Test
    void testGetValidInputShouldReturnValidMoveWhenFormatIsCorrect() {
        StubConsoleReader reader = new StubConsoleReader("a1->a3");
        SpyConsolePrinter printer = new SpyConsolePrinter();
        InputValidator inputValidator = new InputValidator(reader, printer);

        String result = inputValidator.getValidInput(State.IN_GAME, InputType.IN_GAME_CHOICE);

        assertEquals("a1->a3", result);
    }

    @Test
    void testGetValidInputShouldRetryWhenMoveFormatIsInvalid() {
        StubConsoleReader reader = new StubConsoleReader("a1-a3", "A1->a3", "a->b", "b2->b5");
        SpyConsolePrinter printer = new SpyConsolePrinter();
        InputValidator inputValidator = new InputValidator(reader, printer);

        String result = inputValidator.getValidInput(State.IN_GAME, InputType.IN_GAME_CHOICE);

        assertEquals("b2->b5", result);
        long errorCount = printer.messages.stream().filter("Invalid choice!"::equals).count();
        assertEquals(3, errorCount);
        assertEquals(4, reader.readCount);
    }

    private static class StubConsoleReader implements ConsoleReader {
        private final List<String> inputs;
        private int readCount = 0;

        StubConsoleReader(String... inputs) {
            this.inputs = new ArrayList<>(Arrays.asList(inputs));
        }

        @Override
        public String read() {
            readCount++;
            return inputs.remove(0);
        }
    }

    private static class SpyConsolePrinter extends ConsolePrinter {
        final List<String> messages = new ArrayList<>();
        final List<State> printedStates = new ArrayList<>();

        @Override
        public void printMessage(String message) {
            messages.add(message);
        }

        @Override
        public void printGameStateChoices(State state) {
            printedStates.add(state);
        }
    }
}