package rush.hour.service.map.json.loader;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rush.hour.model.ExitVO;
import rush.hour.model.MapVO;
import rush.hour.model.Orientation;
import rush.hour.model.VehicleVO;
import rush.hour.service.exceptions.JSONMapLoaderException;
import rush.hour.service.exceptions.JSONMapSaverException;
import rush.hour.service.map.json.saver.JSONMapSaver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JSONMapLoaderTest {

    private static final String VALID_FILE_NAME = "test_load_valid_map";
    private static final String INVALID_JSON_FILE_NAME = "test_load_invalid_json";
    private JSONMapLoader jsonMapLoader;
    private JSONMapSaver jsonMapSaver;
    private File validFile;
    private File invalidJsonFile;

    @BeforeEach
    void setUp() {
        jsonMapLoader = new JSONMapLoader();
        jsonMapSaver = new JSONMapSaver();
        new File("maps").mkdirs();
        validFile = new File("maps\\" + VALID_FILE_NAME + ".json");
        invalidJsonFile = new File("maps\\" + INVALID_JSON_FILE_NAME + ".json");
    }

    @AfterEach
    void tearDown() {
        if (validFile.exists()) {
            validFile.delete();
        }
        if (invalidJsonFile.exists()) {
            invalidJsonFile.delete();
        }
    }

    @Test
    void testLoadMapShouldReturnMatchingMapVOWhenJsonFileIsValid() throws JSONMapSaverException, JSONMapLoaderException {
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

        MapVO expectedMap = MapVO.builder()
                .withNumberOfRows(6)
                .withNumberOfColumns(7)
                .withObstacles(List.of(obstacle))
                .withHeroCarPlayer(playerCar)
                .withHeroCarAI(aiCar)
                .withExitPlayer(playerExit)
                .withExitAI(aiExit)
                .build();

        jsonMapSaver.saveMap(expectedMap, VALID_FILE_NAME);

        MapVO loadedMap = jsonMapLoader.loadMap(VALID_FILE_NAME);

        assertEquals(expectedMap, loadedMap);
    }

    @Test
    void testLoadMapShouldThrowExceptionWhenFileDoesNotExist() {
        JSONMapLoaderException exception = assertThrows(JSONMapLoaderException.class,
                () -> jsonMapLoader.loadMap("non_existent_map_file"));

        assertEquals("Map not found!", exception.getMessage());
    }

    @Test
    void testLoadMapShouldThrowExceptionWhenJsonIsMalformed() throws IOException {
        Files.writeString(invalidJsonFile.toPath(), "{ invalid json content");

        JSONMapLoaderException exception = assertThrows(JSONMapLoaderException.class,
                () -> jsonMapLoader.loadMap(INVALID_JSON_FILE_NAME));

        assertEquals("Invalid map!", exception.getMessage());
    }

    @Test
    void testLoadMapShouldThrowExceptionWhenJsonStructureIsInvalid() throws IOException {
        Files.writeString(invalidJsonFile.toPath(), "{}");

        JSONMapLoaderException exception = assertThrows(JSONMapLoaderException.class,
                () -> jsonMapLoader.loadMap(INVALID_JSON_FILE_NAME));

        assertEquals("Invalid map!", exception.getMessage());
    }
}