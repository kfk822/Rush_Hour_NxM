package rush.hour.service.map.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import rush.hour.model.ExitVO;
import rush.hour.model.MapVO;
import rush.hour.model.MoveVO;
import rush.hour.model.Orientation;
import rush.hour.model.VehicleVO;
import rush.hour.service.exceptions.MovementValidatorException;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MovementValidatorTest {

    private MovementValidator movementValidator;
    private MapVO map;
    private VehicleVO horizontalCar;
    private VehicleVO verticalCar;
    private ExitVO playerExit;
    private ExitVO aiExit;

    @BeforeEach
    void setUp() {
        movementValidator = new MovementValidator();

        horizontalCar = new VehicleVO.Builder()
                .withVerticalPosition(2)
                .withHorizontalPosition(3)
                .withVehicleLength(2)
                .withOrientation(Orientation.HORIZONTAL)
                .build();

        verticalCar = new VehicleVO.Builder()
                .withVerticalPosition(4)
                .withHorizontalPosition(1)
                .withVehicleLength(2)
                .withOrientation(Orientation.VERTICAL)
                .build();

        playerExit = new ExitVO.Builder()
                .withVerticalPosition(2)
                .withHorizontalPosition(6)
                .build();

        aiExit = new ExitVO.Builder()
                .withVerticalPosition(5)
                .withHorizontalPosition(1)
                .build();

        map = new MapVO.Builder()
                .withNumberOfRows(6)
                .withNumberOfColumns(7)
                .withHeroCarPlayer(horizontalCar)
                .withHeroCarAI(verticalCar)
                .withExitPlayer(playerExit)
                .withExitAI(aiExit)
                .withObstacles(Collections.emptyList())
                .build();
    }

    @Test
    @DisplayName("Should allow horizontal vehicle to move right and left when path is completely clear")
    void testValidateMoveShouldNotThrowExceptionWhenHorizontalMoveIsClear() {
        MoveVO moveRight = new MoveVO.Builder()
                .withVehicle(horizontalCar)
                .withTargetRow(2)
                .withTargetColumn(5)
                .build();

        MoveVO moveLeft = new MoveVO.Builder()
                .withVehicle(horizontalCar)
                .withTargetRow(2)
                .withTargetColumn(2)
                .build();

        assertDoesNotThrow(() -> movementValidator.validateMove(map, moveRight));
        assertDoesNotThrow(() -> movementValidator.validateMove(map, moveLeft));
    }

    @Test
    @DisplayName("Should allow vertical vehicle to move down and up when path is completely clear")
    void testValidateMoveShouldNotThrowExceptionWhenVerticalMoveIsClear() {
        MoveVO moveDown = new MoveVO.Builder()
                .withVehicle(verticalCar)
                .withTargetRow(4)
                .withTargetColumn(1)
                .build();

        MoveVO moveUp = new MoveVO.Builder()
                .withVehicle(verticalCar)
                .withTargetRow(2)
                .withTargetColumn(1)
                .build();

        assertDoesNotThrow(() -> movementValidator.validateMove(map, moveDown));
        assertDoesNotThrow(() -> movementValidator.validateMove(map, moveUp));
    }

    @Test
    @DisplayName("Should throw MovementValidatorException when horizontal vehicle tries to change row")
    void testValidateMoveShouldThrowExceptionWhenHorizontalVehicleMovesVertically() {
        MoveVO invalidMove = new MoveVO.Builder()
                .withVehicle(horizontalCar)
                .withTargetRow(3)
                .withTargetColumn(3)
                .build();

        assertThrows(MovementValidatorException.class,
                () -> movementValidator.validateMove(map, invalidMove));
    }

    @Test
    @DisplayName("Should throw MovementValidatorException when vertical vehicle tries to change column")
    void testValidateMoveShouldThrowExceptionWhenVerticalVehicleMovesHorizontally() {
        MoveVO invalidMove = new MoveVO.Builder()
                .withVehicle(verticalCar)
                .withTargetRow(4)
                .withTargetColumn(4)
                .build();

        assertThrows(MovementValidatorException.class,
                () -> movementValidator.validateMove(map, invalidMove));
    }

    @Test
    @DisplayName("Should throw MovementValidatorException when horizontal path is blocked by an obstacle")
    void testValidateMoveShouldThrowExceptionWhenHorizontalPathIsBlocked() {
        VehicleVO obstacle = new VehicleVO.Builder()
                .withVerticalPosition(2)
                .withHorizontalPosition(4)
                .withVehicleLength(2)
                .withOrientation(Orientation.VERTICAL)
                .build();

        MapVO blockedMap = new MapVO.Builder()
                .withNumberOfRows(6)
                .withNumberOfColumns(7)
                .withHeroCarPlayer(horizontalCar)
                .withHeroCarAI(verticalCar)
                .withExitPlayer(playerExit)
                .withExitAI(aiExit)
                .withObstacles(List.of(obstacle))
                .build();

        MoveVO blockedMove = new MoveVO.Builder()
                .withVehicle(horizontalCar)
                .withTargetRow(2)
                .withTargetColumn(5)
                .build();

        assertThrows(MovementValidatorException.class,
                () -> movementValidator.validateMove(blockedMap, blockedMove));
    }

    @Test
    @DisplayName("Should throw MovementValidatorException when vertical path is blocked by an obstacle")
    void testValidateMoveShouldThrowExceptionWhenVerticalPathIsBlocked() {
        VehicleVO obstacle = new VehicleVO.Builder()
                .withVerticalPosition(2)
                .withHorizontalPosition(2)
                .withVehicleLength(2)
                .withOrientation(Orientation.HORIZONTAL)
                .build();

        MapVO blockedMap = new MapVO.Builder()
                .withNumberOfRows(6)
                .withNumberOfColumns(7)
                .withHeroCarPlayer(horizontalCar)
                .withHeroCarAI(verticalCar)
                .withExitPlayer(playerExit)
                .withExitAI(aiExit)
                .withObstacles(List.of(obstacle))
                .build();

        MoveVO blockedMove = new MoveVO.Builder()
                .withVehicle(verticalCar)
                .withTargetRow(2)
                .withTargetColumn(1)
                .build();

        assertThrows(MovementValidatorException.class,
                () -> movementValidator.validateMove(blockedMap, blockedMove));
    }

    @Test
    @DisplayName("Should throw MovementValidatorException when target position is out of map bounds")
    void testValidateMoveShouldThrowExceptionWhenTargetPositionIsOutOfBounds() {
        MoveVO outOfBoundsHorizontal = new MoveVO.Builder()
                .withVehicle(horizontalCar)
                .withTargetRow(2)
                .withTargetColumn(6)
                .build();

        MoveVO outOfBoundsVertical = new MoveVO.Builder()
                .withVehicle(verticalCar)
                .withTargetRow(5)
                .withTargetColumn(1)
                .build();

        assertThrows(MovementValidatorException.class,
                () -> movementValidator.validateMove(map, outOfBoundsHorizontal));
        assertThrows(MovementValidatorException.class,
                () -> movementValidator.validateMove(map, outOfBoundsVertical));
    }
}