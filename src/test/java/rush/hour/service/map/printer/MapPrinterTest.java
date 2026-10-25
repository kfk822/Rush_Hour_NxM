package rush.hour.service.map.printer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import rush.hour.model.ExitVO;
import rush.hour.model.FileCellTypes;
import rush.hour.model.MapVO;
import rush.hour.model.Orientation;
import rush.hour.model.VehicleVO;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
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
    @DisplayName("Should print grid headers, walls, horizontal player car, vertical AI car, obstacles and exits")
    void testPrintMapShouldRenderAllElementsCorrectly() {
        VehicleVO playerCar = new VehicleVO.Builder()
                .withVerticalPosition(2)
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

        VehicleVO obstacle = new VehicleVO.Builder()
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

        MapVO map = new MapVO.Builder()
                .withNumberOfRows(6)
                .withNumberOfColumns(7)
                .withHeroCarPlayer(playerCar)
                .withHeroCarAI(aiCar)
                .withObstacles(List.of(obstacle))
                .withExitPlayer(playerExit)
                .withExitAI(aiExit)
                .build();

        mapPrinter.printMap(map);

        String output = outputStreamCaptor.toString();

        assertTrue(output.contains("1"));
        assertTrue(output.contains("a"));

        assertTrue(output.contains(String.valueOf(FileCellTypes.WALL.getHead())));

        assertTrue(output.contains(String.valueOf(FileCellTypes.HORIZONTAL_PLAYER.getHead())));
        assertTrue(output.contains(String.valueOf(FileCellTypes.HORIZONTAL_PLAYER.getBody())));

        assertTrue(output.contains(String.valueOf(FileCellTypes.VERTICAL_AI.getHead())));
        assertTrue(output.contains(String.valueOf(FileCellTypes.VERTICAL_AI.getBody())));

        assertTrue(output.contains(String.valueOf(FileCellTypes.HORIZONTAL_OBSTACLE.getHead())));
        assertTrue(output.contains(String.valueOf(FileCellTypes.HORIZONTAL_OBSTACLE.getBody())));

        assertTrue(output.contains(String.valueOf(FileCellTypes.PLAYER_EXIT.getHead())));
        assertTrue(output.contains(String.valueOf(FileCellTypes.AI_EXIT.getHead())));
    }

    @Test
    @DisplayName("Should cover remaining branches when player is vertical, AI is horizontal, and obstacle is vertical")
    void testPrintMapShouldCoverAlternativeOrientations() {
        VehicleVO verticalPlayer = new VehicleVO.Builder()
                .withVerticalPosition(3)
                .withHorizontalPosition(4)
                .withVehicleLength(2)
                .withOrientation(Orientation.VERTICAL)
                .build();

        VehicleVO horizontalAi = new VehicleVO.Builder()
                .withVerticalPosition(4)
                .withHorizontalPosition(3)
                .withVehicleLength(2)
                .withOrientation(Orientation.HORIZONTAL)
                .build();

        VehicleVO verticalObstacle = new VehicleVO.Builder()
                .withVerticalPosition(3)
                .withHorizontalPosition(2)
                .withVehicleLength(2)
                .withOrientation(Orientation.VERTICAL)
                .build();

        ExitVO playerExit = new ExitVO.Builder()
                .withVerticalPosition(1)
                .withHorizontalPosition(6)
                .build();

        ExitVO aiExit = new ExitVO.Builder()
                .withVerticalPosition(5)
                .withHorizontalPosition(2)
                .build();

        MapVO map = new MapVO.Builder()
                .withNumberOfRows(6)
                .withNumberOfColumns(7)
                .withHeroCarPlayer(verticalPlayer)
                .withHeroCarAI(horizontalAi)
                .withObstacles(List.of(verticalObstacle))
                .withExitPlayer(playerExit)
                .withExitAI(aiExit)
                .build();

        mapPrinter.printMap(map);

        String output = outputStreamCaptor.toString();

        assertTrue(output.contains(String.valueOf(FileCellTypes.VERTICAL_PLAYER.getHead())));
        assertTrue(output.contains(String.valueOf(FileCellTypes.VERTICAL_PLAYER.getBody())));

        assertTrue(output.contains(String.valueOf(FileCellTypes.HORIZONTAL_AI.getHead())));
        assertTrue(output.contains(String.valueOf(FileCellTypes.HORIZONTAL_AI.getBody())));

        assertTrue(output.contains(String.valueOf(FileCellTypes.VERTICAL_OBSTACLE.getHead())));
        assertTrue(output.contains(String.valueOf(FileCellTypes.VERTICAL_OBSTACLE.getBody())));
    }
}