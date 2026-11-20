package rush.hour.service.map.json.saver;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rush.hour.model.ExitVO;
import rush.hour.model.MapVO;
import rush.hour.model.Orientation;
import rush.hour.model.VehicleVO;
import rush.hour.service.exceptions.JSONMapSaverException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JSONMapSaverTest {

    private static final String TEST_FILE_NAME = "test_save_map";
    private JSONMapSaver jsonMapSaver;
    private File testFile;
    private MapVO map;

    @BeforeEach
    void setUp() {
        jsonMapSaver = new JSONMapSaver();
        new File("maps").mkdirs();
        testFile = new File("maps\\" + TEST_FILE_NAME + ".json");

        VehicleVO playerCar = VehicleVO.builder()
                .withHorizontalPosition(3)
                .withVerticalPosition(2)
                .withOrientation(Orientation.HORIZONTAL)
                .withVehicleLength(2)
                .build();

        VehicleVO aiCar = VehicleVO.builder()
                .withHorizontalPosition(1)
                .withVerticalPosition(4)
                .withOrientation(Orientation.VERTICAL)
                .withVehicleLength(2)
                .build();

        VehicleVO obstacle = VehicleVO.builder()
                .withHorizontalPosition(5)
                .withVerticalPosition(1)
                .withOrientation(Orientation.HORIZONTAL)
                .withVehicleLength(3)
                .build();

        ExitVO playerExit = ExitVO.builder()
                .withHorizontalPosition(6)
                .withVerticalPosition(2)
                .build();

        ExitVO aiExit = ExitVO.builder()
                .withHorizontalPosition(1)
                .withVerticalPosition(5)
                .build();

        map = MapVO.builder()
                .withNumberOfRows(6)
                .withNumberOfColumns(7)
                .withObstacles(List.of(obstacle))
                .withHeroCarPlayer(playerCar)
                .withHeroCarAI(aiCar)
                .withExitPlayer(playerExit)
                .withExitAI(aiExit)
                .build();
    }

    @AfterEach
    void tearDown() {
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    @Test
    void testSaveMapShouldCreateValidJsonFileWhenMapIsValid() throws JSONMapSaverException, IOException {
        jsonMapSaver.saveMap(map, TEST_FILE_NAME);

        assertTrue(testFile.exists());
        String content = Files.readString(testFile.toPath());
        assertTrue(content.contains("\"NumberOfRows\":6"));
        assertTrue(content.contains("\"NumberOfColumns\":7"));
        assertTrue(content.contains("\"HeroCarPlayer\""));
        assertTrue(content.contains("\"HeroCarAI\""));
        assertTrue(content.contains("\"Obstacles\""));
        assertTrue(content.contains("\"ExitPlayer\""));
        assertTrue(content.contains("\"ExitAI\""));
    }

    @Test
    void testSaveMapShouldThrowExceptionWhenFilePathIsInvalid() {
        assertThrows(JSONMapSaverException.class,
                () -> jsonMapSaver.saveMap(map, "nonexistent_folder\\invalid_map"));
    }
}