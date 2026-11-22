package rush.hour.service.move;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rush.hour.model.ExitVO;
import rush.hour.model.MapVO;
import rush.hour.model.MoveVO;
import rush.hour.model.Orientation;
import rush.hour.model.VehicleVO;
import rush.hour.service.exceptions.MoveMakerException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MoveMakerTest {

    private MoveMaker moveMaker;
    private MapVO map;
    private VehicleVO playerCar;
    private VehicleVO aiCar;
    private VehicleVO obstacle;

    @BeforeEach
    void setUp() {
        moveMaker = new MoveMaker();

        playerCar = new VehicleVO.Builder()
                .withVerticalPosition(2)
                .withHorizontalPosition(3)
                .withVehicleLength(2)
                .withOrientation(Orientation.HORIZONTAL)
                .build();

        aiCar = new VehicleVO.Builder()
                .withVerticalPosition(4)
                .withHorizontalPosition(1)
                .withVehicleLength(2)
                .withOrientation(Orientation.VERTICAL)
                .build();

        obstacle = new VehicleVO.Builder()
                .withVerticalPosition(1)
                .withHorizontalPosition(5)
                .withVehicleLength(3)
                .withOrientation(Orientation.HORIZONTAL)
                .build();

        ExitVO playerExit = new ExitVO.Builder()
                .withVerticalPosition(2)
                .withHorizontalPosition(6)
                .build();

        ExitVO aiExit = new ExitVO.Builder()
                .withVerticalPosition(5)
                .withHorizontalPosition(1)
                .build();

        map = new MapVO.Builder()
                .withNumberOfRows(6)
                .withNumberOfColumns(7)
                .withHeroCarPlayer(playerCar)
                .withHeroCarAI(aiCar)
                .withExitPlayer(playerExit)
                .withExitAI(aiExit)
                .withObstacles(List.of(obstacle))
                .build();
    }

    @Test
    void testMakeMoveShouldReturnMoveVOWhenPlayerCarPositionIsValid() {
        MoveVO result = moveMaker.makeMove("b3->b5", map);

        assertEquals(playerCar, result.getVehicle());
        assertEquals(2, result.getTargetRow());
        assertEquals(5, result.getTargetColumn());
    }

    @Test
    void testMakeMoveShouldReturnMoveVOWhenAiCarPositionIsValid() {
        MoveVO result = moveMaker.makeMove("d1->b1", map);

        assertEquals(aiCar, result.getVehicle());
        assertEquals(2, result.getTargetRow());
        assertEquals(1, result.getTargetColumn());
    }

    @Test
    void testMakeMoveShouldReturnMoveVOWhenObstaclePositionIsValid() {
        MoveVO result = moveMaker.makeMove("a5->a3", map);

        assertEquals(obstacle, result.getVehicle());
        assertEquals(1, result.getTargetRow());
        assertEquals(3, result.getTargetColumn());
    }

    @Test
    void testMakeMoveShouldThrowExceptionWhenRawMoveIsNull() {
        MoveMakerException exception = assertThrows(MoveMakerException.class,
                () -> moveMaker.makeMove(null, map));

        assertEquals("Move cannot be null!", exception.getMessage());
    }

    @Test
    void testMakeMoveShouldThrowExceptionWhenMoveFormatIsInvalid() {
        assertThrows(MoveMakerException.class, () -> moveMaker.makeMove("b3b5", map));
        assertThrows(MoveMakerException.class, () -> moveMaker.makeMove("b->b5", map));
        assertThrows(MoveMakerException.class, () -> moveMaker.makeMove("b3->b", map));
    }

    @Test
    void testMakeMoveShouldThrowExceptionWhenCoordinatesAreNotNumbers() {
        MoveMakerException exception = assertThrows(MoveMakerException.class,
                () -> moveMaker.makeMove("bx->b5", map));

        assertEquals("Invalid move coordinates!", exception.getMessage());
    }

    @Test
    void testMakeMoveShouldThrowExceptionWhenNoVehicleAtGivenHeadPosition() {
        MoveMakerException exception = assertThrows(MoveMakerException.class,
                () -> moveMaker.makeMove("c3->c5", map));

        assertEquals("Invalid vehicle head position!", exception.getMessage());
    }
}