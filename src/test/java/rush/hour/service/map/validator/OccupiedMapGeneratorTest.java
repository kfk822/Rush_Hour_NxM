package rush.hour.service.map.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import rush.hour.model.ExitVO;
import rush.hour.model.MapVO;
import rush.hour.model.Orientation;
import rush.hour.model.VehicleVO;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OccupiedMapGeneratorTest {

    private OccupiedMapGenerator underTest;
    private VehicleVO playerCar;
    private VehicleVO aiCar;
    private VehicleVO horizontalObstacle;
    private ExitVO playerExit;
    private ExitVO aiExit;
    private MapVO map;

    @BeforeEach
    void setUp() {
        underTest = new OccupiedMapGenerator();

        playerCar = new VehicleVO.Builder()
                .withVerticalPosition(2)
                .withHorizontalPosition(3)
                .withVehicleLength(2)
                .withOrientation(Orientation.HORIZONTAL)
                .build();

        aiCar = new VehicleVO.Builder()
                .withVerticalPosition(4)
                .withHorizontalPosition(2)
                .withVehicleLength(2)
                .withOrientation(Orientation.VERTICAL)
                .build();

        playerExit = new ExitVO.Builder()
                .withVerticalPosition(2)
                .withHorizontalPosition(7)
                .build();

        aiExit = new ExitVO.Builder()
                .withVerticalPosition(6)
                .withHorizontalPosition(2)
                .build();

        horizontalObstacle = new VehicleVO.Builder()
                .withVerticalPosition(1)
                .withHorizontalPosition(5)
                .withVehicleLength(3)
                .withOrientation(Orientation.HORIZONTAL)
                .build();

        map = new MapVO.Builder()
                .withNumberOfRows(6)
                .withNumberOfColumns(7)
                .withHeroCarPlayer(playerCar)
                .withHeroCarAI(aiCar)
                .withExitPlayer(playerExit)
                .withExitAI(aiExit)
                .withObstacles(List.of(horizontalObstacle))
                .build();
    }

    @Test
    @DisplayName("Should mark borders and all vehicles as occupied when excluded vehicle is null")
    void testGenerateOccupiedMapShouldMarkWallsAndAllVehiclesWhenExcludedVehicleIsNull() {
        boolean[][] result = underTest.generateOccupiedMap(map, null);

        assertTrue(result[0][0]);
        assertTrue(result[0][6]);
        assertTrue(result[5][0]);
        assertTrue(result[5][6]);

        assertFalse(result[1][1]);

        assertTrue(result[2][2]);
        assertTrue(result[2][3]);

        assertTrue(result[3][2]);
        assertTrue(result[4][2]);

        assertTrue(result[1][3]);
        assertTrue(result[1][4]);
        assertTrue(result[1][5]);
    }

    @Test
    @DisplayName("Should leave excluded vehicle positions free when a vehicle is excluded")
    void testGenerateOccupiedMapShouldNotMarkExcludedVehicleWhenExcludedVehicleIsProvided() {
        boolean[][] result = underTest.generateOccupiedMap(map, playerCar);

        assertFalse(result[2][2]);
        assertFalse(result[2][3]);

        assertTrue(result[3][2]);
        assertTrue(result[4][2]);
        assertTrue(result[1][3]);
        assertTrue(result[1][4]);
        assertTrue(result[1][5]);
    }
}