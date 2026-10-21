package rush.hour.service.map.parser;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import rush.hour.model.MapVO;
import rush.hour.model.Orientation;
import rush.hour.model.VehicleVO;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MapParserTest {

    private MapParser mapParser;

    @Test
    @DisplayName("Should parse cars and aligned exits correctly when input map is valid")
    void testParseMapShouldCreateCompleteMapVOWhenInputIsValid() {
        List<String> rawMap = List.of(
                "########",
                "#......#",
                "#.pP..vE",
                "#.b...V#",
                "#.BhhH.#",
                "##X#####"
        );
        mapParser = new MapParser(rawMap.size(), rawMap.get(0).length());

        MapVO result = mapParser.parseMap(rawMap);

        assertNotNull(result);
        assertEquals(6, result.getNumberOfRows());
        assertEquals(8, result.getNumberOfColumns());

        assertNotNull(result.getHeroCarPlayer());
        assertEquals(2, result.getHeroCarPlayer().getVerticalPosition());
        assertEquals(3, result.getHeroCarPlayer().getHorizontalPosition());
        assertEquals(Orientation.HORIZONTAL, result.getHeroCarPlayer().getOrientation());
        assertEquals(2, result.getHeroCarPlayer().getVehicleLength());

        assertNotNull(result.getExitPlayer());
        assertEquals(2, result.getExitPlayer().getVerticalPosition());
        assertEquals(7, result.getExitPlayer().getHorizontalPosition());

        assertNotNull(result.getHeroCarAI());
        assertEquals(4, result.getHeroCarAI().getVerticalPosition());
        assertEquals(2, result.getHeroCarAI().getHorizontalPosition());
        assertEquals(Orientation.VERTICAL, result.getHeroCarAI().getOrientation());
        assertEquals(2, result.getHeroCarAI().getVehicleLength());

        assertNotNull(result.getExitAI());
        assertEquals(5, result.getExitAI().getVerticalPosition());
        assertEquals(2, result.getExitAI().getHorizontalPosition());

        assertNotNull(result.getObstacles());
        assertEquals(2, result.getObstacles().size());

        VehicleVO horizontalObstacle = result.getObstacles().stream()
                .filter(v -> v.getOrientation() == Orientation.HORIZONTAL)
                .findFirst()
                .orElse(null);
        assertNotNull(horizontalObstacle);
        assertEquals(4, horizontalObstacle.getVerticalPosition());
        assertEquals(5, horizontalObstacle.getHorizontalPosition());
        assertEquals(3, horizontalObstacle.getVehicleLength());

        VehicleVO verticalObstacle = result.getObstacles().stream()
                .filter(v -> v.getOrientation() == Orientation.VERTICAL)
                .findFirst()
                .orElse(null);
        assertNotNull(verticalObstacle);
        assertEquals(3, verticalObstacle.getVerticalPosition());
        assertEquals(6, verticalObstacle.getHorizontalPosition());
        assertEquals(2, verticalObstacle.getVehicleLength());
    }

    @Test
    @DisplayName("Should return empty obstacles list when map contains no neutral obstacles")
    void testParseMapShouldReturnEmptyObstaclesWhenMapHasNoObstacles() {
        List<String> rawMap = List.of(
                "########",
                "#......#",
                "#.pP...E",
                "#.b....#",
                "#.B....#",
                "##X#####"
        );
        mapParser = new MapParser(rawMap.size(), rawMap.get(0).length());

        MapVO result = mapParser.parseMap(rawMap);

        assertNotNull(result);
        assertNotNull(result.getObstacles());
        assertEquals(0, result.getObstacles().size());
    }
}