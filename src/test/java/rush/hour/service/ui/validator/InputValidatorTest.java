package rush.hour.service.ui.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rush.hour.model.Choice;
import rush.hour.model.InputType;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InputValidatorTest {

    private InputValidator inputValidator;

    @BeforeEach
    void setUp() {
        inputValidator = new InputValidator();
    }

    @Test
    void testIsValidInputShouldReturnFalseWhenInputIsNull() {
        assertFalse(inputValidator.isValidInput(null, InputType.NAME));
    }

    @Test
    void testIsValidInputShouldReturnFalseWhenInputTypeIsNull() {
        assertFalse(inputValidator.isValidInput("Player1", null));
    }

    @Test
    void testIsValidInputShouldReturnTrueWhenNameIsValid() {
        assertTrue(inputValidator.isValidInput("Player1", InputType.NAME));
    }

    @Test
    void testIsValidInputShouldReturnFalseWhenNameIsEmptyOrContainsSpace() {
        assertFalse(inputValidator.isValidInput("", InputType.NAME));
        assertFalse(inputValidator.isValidInput(" ", InputType.NAME));
        assertFalse(inputValidator.isValidInput("Player 1", InputType.NAME));
    }

    @Test
    void testIsValidInputShouldReturnTrueWhenMapFileNameIsValid() {
        assertTrue(inputValidator.isValidInput("saved_map", InputType.MAP_FILE_NAME));
    }

    @Test
    void testIsValidInputShouldReturnFalseWhenMapFileNameIsEmptyOrContainsSpace() {
        assertFalse(inputValidator.isValidInput("", InputType.MAP_FILE_NAME));
        assertFalse(inputValidator.isValidInput(" ", InputType.MAP_FILE_NAME));
        assertFalse(inputValidator.isValidInput("saved map", InputType.MAP_FILE_NAME));
    }

    @Test
    void testIsValidInputShouldReturnTrueWhenBeforeGameChoiceIsValid() {
        assertTrue(inputValidator.isValidInput(Choice.PLAY.getValue(), InputType.BEFORE_GAME_CHOICE));
        assertTrue(inputValidator.isValidInput(Choice.LOAD_GAME.getValue(), InputType.BEFORE_GAME_CHOICE));
        assertTrue(inputValidator.isValidInput(Choice.SHOW_LEADERBOARD.getValue(), InputType.BEFORE_GAME_CHOICE));
        assertTrue(inputValidator.isValidInput(Choice.QUIT_PROGRAM.getValue(), InputType.BEFORE_GAME_CHOICE));
    }

    @Test
    void testIsValidInputShouldReturnFalseWhenBeforeGameChoiceIsInvalid() {
        assertFalse(inputValidator.isValidInput("", InputType.BEFORE_GAME_CHOICE));
        assertFalse(inputValidator.isValidInput("0", InputType.BEFORE_GAME_CHOICE));
        assertFalse(inputValidator.isValidInput("5", InputType.BEFORE_GAME_CHOICE));
        assertFalse(inputValidator.isValidInput("abc", InputType.BEFORE_GAME_CHOICE));
    }

    @Test
    void testIsValidInputShouldReturnTrueWhenInGameChoiceIsValidMenuOption() {
        assertTrue(inputValidator.isValidInput(Choice.SAVE_AND_QUIT_GAME.getValue(), InputType.IN_GAME_CHOICE));
        assertTrue(inputValidator.isValidInput(Choice.QUIT_GAME.getValue(), InputType.IN_GAME_CHOICE));
    }

    @Test
    void testIsValidInputShouldReturnTrueWhenInGameMoveFormatIsValid() {
        assertTrue(inputValidator.isValidInput("a1->a3", InputType.IN_GAME_CHOICE));
        assertTrue(inputValidator.isValidInput("z10->b2", InputType.IN_GAME_CHOICE));
    }

    @Test
    void testIsValidInputShouldReturnFalseWhenInGameChoiceOrMoveFormatIsInvalid() {
        assertFalse(inputValidator.isValidInput("", InputType.IN_GAME_CHOICE));
        assertFalse(inputValidator.isValidInput("3", InputType.IN_GAME_CHOICE));
        assertFalse(inputValidator.isValidInput("a1-a3", InputType.IN_GAME_CHOICE));
        assertFalse(inputValidator.isValidInput("a1->a2->a3", InputType.IN_GAME_CHOICE));
        assertFalse(inputValidator.isValidInput("a1->", InputType.IN_GAME_CHOICE));
        assertFalse(inputValidator.isValidInput("->a1", InputType.IN_GAME_CHOICE));
        assertFalse(inputValidator.isValidInput("A1->a3", InputType.IN_GAME_CHOICE));
        assertFalse(inputValidator.isValidInput("{1->a3", InputType.IN_GAME_CHOICE));
        assertFalse(inputValidator.isValidInput("a1->A3", InputType.IN_GAME_CHOICE));
        assertFalse(inputValidator.isValidInput("a1->{3", InputType.IN_GAME_CHOICE));
        assertFalse(inputValidator.isValidInput("a->b", InputType.IN_GAME_CHOICE));
        assertFalse(inputValidator.isValidInput("a1->bb", InputType.IN_GAME_CHOICE));
    }
}