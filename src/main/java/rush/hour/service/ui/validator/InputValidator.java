package rush.hour.service.ui.validator;

import rush.hour.model.InputType;
import rush.hour.model.State;
import rush.hour.service.ui.printer.ConsolePrinter;
import rush.hour.service.ui.reader.ConsoleReader;

public class InputValidator {
    ConsoleReader consoleReader;
    ConsolePrinter consolePrinter;

    public InputValidator(ConsoleReader consoleReader, ConsolePrinter consolePrinter) {
        this.consoleReader = consoleReader;
        this.consolePrinter = consolePrinter;
    }

    public String getValidInput(State state, InputType inputType) {
        boolean validInput = false;
        String input = "";
        do {
            switch (inputType) {
                case NAME:
                    consolePrinter.printMessage("What is your username?");
                    input = consoleReader.read();
                    if (input.isEmpty()) {
                        consolePrinter.printMessage("Input can't be empty!");
                    } else {
                        validInput = true;
                    }
                    break;
                case BEFORE_GAME_CHOICE:
                    consolePrinter.printGameStateChoices(state);
                    input = consoleReader.read();
                    int choice;
                    try {
                        choice = Integer.parseInt(input);
                        if (choice > 0 && choice < 4) {
                            validInput = true;
                        } else {
                            consolePrinter.printMessage("Invalid choice!");
                        }
                    } catch (NumberFormatException e) {
                        consolePrinter.printMessage("Invalid choice!");
                    }
                    break;
                case IN_GAME_CHOICE:
                    consolePrinter.printGameStateChoices(state);
                    input = consoleReader.read();
                    if (input.equals("quit") || isValidMoveFormat(input)) {
                        validInput = true;
                    } else {
                        consolePrinter.printMessage("Invalid choice!");
                    }
                default:
                    break;
            }
        } while (!validInput);

        return input;
    }

    private boolean isValidMoveFormat(String input) {
        try {
            String[] positions = input.split("->");
            char positionCharacter1 = positions[0].charAt(0);
            int positionNumber1 = Integer.parseInt(positions[0].substring(1));
            char positionCharacter2 = positions[1].charAt(0);
            int positionNumber2 = Integer.parseInt(positions[1].substring(1));
            if (positionCharacter1 >= 'a' &&
                    positionCharacter1 <= 'z' &&
                    positionCharacter2 >= 'a' &&
                    positionCharacter2 <= 'z') {
                return true;
            }
        } catch (Exception e) {
            return false;
        }

        return false;
    }
}
