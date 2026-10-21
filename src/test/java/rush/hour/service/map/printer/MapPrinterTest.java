package rush.hour.service.map.printer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import rush.hour.model.ExitVO;
import rush.hour.model.MapVO;
import rush.hour.model.Orientation;
import rush.hour.model.VehicleVO;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MapPrinterTest {

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private MapPrinter mapPrinter;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
        mapPrinter = new MapPrinter();
    }

    @AfterEach
    void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    @DisplayName("Should correctly print grid with walls, player, AI, and exits when map contains no obstacles")
    void testPrintMapShouldRenderGridWithCarsAndExitsWhenNoObstaclesPresent() {
        VehicleVO playerCar = new VehicleVO.Builder()
                .withVerticalPosition(1)
                .withHorizontalPosition(3)
                .withVehicleLength(2)
                .withOrientation(Orientation.HORIZONTAL)
                .build();

        VehicleVO aiCar = new VehicleVO.Builder()
                .withVerticalPosition(3)
                .withHorizontalPosition(2)
                .withVehicleLength(2)
                .withOrientation(Orientation.VERTICAL)
                .build();

        ExitVO playerExit = new ExitVO.Builder()
                .withVerticalPosition(1)
                .withHorizontalPosition(5)
                .build();

        ExitVO aiExit = new ExitVO.Builder()
                .withVerticalPosition(4)
                .withHorizontalPosition(2)
                .build();

        MapVO map = new MapVO.Builder()
                .withNumberOfRows(5)
                .withNumberOfColumns(6)
                .withHeroCarPlayer(playerCar)
                .withHeroCarAI(aiCar)
                .withObstacles(Collections.emptyList())
                .withExitPlayer(playerExit)
                .withExitAI(aiExit)
                .build();

        mapPrinter.printMap(map);

        String printedOutput = outputStreamCaptor.toString();

        assertTrue(printedOutput.contains("######"));
        assertTrue(printedOutput.contains("pP"));
        assertTrue(printedOutput.contains("E"));
        assertTrue(printedOutput.contains("B"));
        assertTrue(printedOutput.contains("X"));
    }

    @Test
    @DisplayName("Should render obstacles correctly when obstacles are present on the map")
    void testPrintMapShouldRenderObstaclesWhenObstaclesExist() {
        VehicleVO playerCar = new VehicleVO.Builder()
                .withVerticalPosition(1)
                .withHorizontalPosition(3)
                .withVehicleLength(2)
                .withOrientation(Orientation.HORIZONTAL)
                .build();

        VehicleVO aiCar = new VehicleVO.Builder()
                .withVerticalPosition(4)
                .withHorizontalPosition(1)
                .withVehicleLength(2)
                .withOrientation(Orientation.VERTICAL)
                .build();

        ExitVO playerExit = new ExitVO.Builder()
                .withVerticalPosition(1)
                .withHorizontalPosition(6)
                .build();

        ExitVO aiExit = new ExitVO.Builder()
                .withVerticalPosition(5)
                .withHorizontalPosition(1)
                .build();

        VehicleVO horizontalObstacle = new VehicleVO.Builder()
                .withVerticalPosition(3)
                .withHorizontalPosition(4)
                .withVehicleLength(3)
                .withOrientation(Orientation.HORIZONTAL)
                .build();

        MapVO map = new MapVO.Builder()
                .withNumberOfRows(6)
                .withNumberOfColumns(7)
                .withHeroCarPlayer(playerCar)
                .withHeroCarAI(aiCar)
                .withObstacles(List.of(horizontalObstacle))
                .withExitPlayer(playerExit)
                .withExitAI(aiExit)
                .build();

        mapPrinter.printMap(map);

        String printedOutput = outputStreamCaptor.toString();
        assertTrue(printedOutput.contains("hhH"));
    }
}