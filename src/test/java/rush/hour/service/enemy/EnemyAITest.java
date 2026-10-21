package rush.hour.service.enemy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import rush.hour.model.ExitVO;
import rush.hour.model.MapVO;
import rush.hour.model.MoveVO;
import rush.hour.model.Orientation;
import rush.hour.model.VehicleVO;

import java.util.Collections;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class EnemyAITest {

    private MapVO map;
    private VehicleVO horizontalPlayerCar;
    private VehicleVO verticalAiCar;

    @BeforeEach
    void setUp() {
        horizontalPlayerCar = new VehicleVO.Builder()
                .withVerticalPosition(2)
                .withHorizontalPosition(3)
                .withVehicleLength(2)
                .withOrientation(Orientation.HORIZONTAL)
                .build();

        verticalAiCar = new VehicleVO.Builder()
                .withVerticalPosition(3)
                .withHorizontalPosition(1)
                .withVehicleLength(2)
                .withOrientation(Orientation.VERTICAL)
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
                .withHeroCarPlayer(horizontalPlayerCar)
                .withHeroCarAI(verticalAiCar)
                .withExitPlayer(playerExit)
                .withExitAI(aiExit)
                .withObstacles(Collections.emptyList())
                .build();
    }

    @Test
    @DisplayName("Should generate move with new column and same row when horizontal vehicle is chosen")
    void testMakeMoveShouldGenerateHorizontalMoveWhenHorizontalVehicleIsSelected() {
        EnemyAI enemyAI = new EnemyAI(new StubRandom(1, 4));

        MoveVO result = enemyAI.makeMove(map);

        assertEquals(horizontalPlayerCar, result.getVehicle());
        assertEquals(2, result.getTargetRow());
        assertEquals(5, result.getTargetColumn());
    }

    @Test
    @DisplayName("Should generate move with new row and same column when vertical vehicle is chosen")
    void testMakeMoveShouldGenerateVerticalMoveWhenVerticalVehicleIsSelected() {
        EnemyAI enemyAI = new EnemyAI(new StubRandom(0, 3));

        MoveVO result = enemyAI.makeMove(map);

        assertEquals(verticalAiCar, result.getVehicle());
        assertEquals(4, result.getTargetRow());
        assertEquals(1, result.getTargetColumn());
    }

    @Test
    @DisplayName("Should retry when move was already chosen in current turn and allow it again after nextTurn")
    void testMakeMoveShouldAvoidDuplicateMovesInSameTurnAndResetOnNextTurn() {
        EnemyAI enemyAI = new EnemyAI(new StubRandom(1, 4, 1, 4, 1, 3, 1, 4));

        MoveVO firstMove = enemyAI.makeMove(map);
        MoveVO secondMove = enemyAI.makeMove(map);

        assertNotEquals(firstMove, secondMove);
        assertEquals(5, firstMove.getTargetColumn());
        assertEquals(4, secondMove.getTargetColumn());

        enemyAI.nextTurn();

        MoveVO moveAfterReset = enemyAI.makeMove(map);
        assertEquals(firstMove, moveAfterReset);
    }

    private static class StubRandom extends Random {
        private final int[] values;
        private int index = 0;

        StubRandom(int... values) {
            this.values = values;
        }

        @Override
        public int nextInt(int bound) {
            return values[index++];
        }
    }
}