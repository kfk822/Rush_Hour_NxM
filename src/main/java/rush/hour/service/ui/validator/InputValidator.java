package rush.hour.service.ui.validator;

import rush.hour.model.Choice;
import rush.hour.model.InputType;

public class InputValidator {

    public boolean isValidInput(String input, InputType inputType) {
        if (input == null || inputType == null){
            return false;
        }

        switch (inputType) {
            case NAME, MAP_FILE_NAME -> {
                return !(input.isEmpty() || input.contains(" "));
            }
            case BEFORE_GAME_CHOICE -> {
                return isBeforeGameChoice(input);
            }
            case IN_GAME_CHOICE -> {
                return isInGameChoice(input) || isValidMoveFormat(input);
            }
            default -> {
                return false;
            }
        }

    }

    private boolean isBeforeGameChoice(String input) {
        return input.equals(Choice.PLAY.getValue()) ||
                input.equals(Choice.LOAD_GAME.getValue()) ||
                input.equals(Choice.SHOW_LEADERBOARD.getValue()) ||
                input.equals(Choice.QUIT_PROGRAM.getValue());
    }

    private boolean isInGameChoice(String input) {
        return input.equals(Choice.SAVE_AND_QUIT_GAME.getValue()) ||
                input.equals(Choice.QUIT_GAME.getValue());
    }

    private boolean isValidMoveFormat(String input) {
        try {
            String[] positions = input.split("->");
            if(positions.length != 2){
                return false;
            }
            char positionCharacter1 = positions[0].charAt(0);
            Integer.parseInt(positions[0].substring(1));
            char positionCharacter2 = positions[1].charAt(0);
            Integer.parseInt(positions[1].substring(1));
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
