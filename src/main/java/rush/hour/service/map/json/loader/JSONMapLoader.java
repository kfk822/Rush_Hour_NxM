package rush.hour.service.map.json.loader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import rush.hour.model.ExitVO;
import rush.hour.model.MapVO;
import rush.hour.model.Orientation;
import rush.hour.model.VehicleVO;
import rush.hour.service.exceptions.JSONMapLoaderException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JSONMapLoader {
    public MapVO loadMap(String fileName) throws JSONMapLoaderException {
        try {
            JSONObject rawMap = parseMap(fileName);

            int numberOfRows = getInt(rawMap, "NumberOfRows");
            int numberOfColumns = getInt(rawMap, "NumberOfColumns");

            JSONArray rawObstacles = getJSONArray(rawMap, "Obstacles");
            List<VehicleVO> obstacles = new ArrayList<>();

            for (Object objectObstacle : rawObstacles) {
                JSONObject rawObstacle = (JSONObject) objectObstacle;

                obstacles.add(getVehicleVO(rawObstacle));
            }

            VehicleVO heroCarPlayer = getVehicleVO(rawMap.get("HeroCarPlayer"));

            VehicleVO heroCarAI = getVehicleVO(rawMap.get("HeroCarAI"));

            ExitVO exitPlayer = getExitVO(rawMap.get("ExitPlayer"));

            ExitVO exitAI = getExitVO(rawMap.get("ExitAI"));

            return MapVO.builder()
                    .withNumberOfRows(numberOfRows)
                    .withNumberOfColumns(numberOfColumns)
                    .withObstacles(obstacles)
                    .withHeroCarPlayer(heroCarPlayer)
                    .withHeroCarAI(heroCarAI)
                    .withExitPlayer(exitPlayer)
                    .withExitAI(exitAI)
                    .build();
        } catch (IOException e) {
            throw new JSONMapLoaderException("Map not found!");
        } catch (ParseException | IllegalArgumentException | IllegalStateException
                 | NullPointerException | ClassCastException e) {
            throw new JSONMapLoaderException("Invalid map!");
        }
    }

    private JSONObject parseMap(String fileName) throws IOException, ParseException {
        File file = new File("maps", fileName + ".json");
        try (FileReader reader = new FileReader(file)) {
            JSONObject root = (JSONObject) new JSONParser().parse(reader);
            return (JSONObject) root.get("Map");
        }

    }

    private VehicleVO getVehicleVO(Object object) {
        JSONObject jsonObject = (JSONObject) object;
        return VehicleVO.builder()
                .withHorizontalPosition(getInt(jsonObject, "HorizontalPosition"))
                .withVerticalPosition(getInt(jsonObject, "VerticalPosition"))
                .withOrientation(Orientation.valueOf((String) jsonObject.get("Orientation")))
                .withVehicleLength(getInt(jsonObject, "Length"))
                .build();
    }

    private ExitVO getExitVO(Object object) {
        JSONObject jsonObject = (JSONObject) object;
        return ExitVO.builder()
                .withHorizontalPosition(getInt(jsonObject, "HorizontalPosition"))
                .withVerticalPosition(getInt(jsonObject, "VerticalPosition"))
                .build();
    }

    private int getInt(JSONObject jsonObject, String name) {
        return ((Long) jsonObject.get(name)).intValue();
    }

    private JSONArray getJSONArray(JSONObject jsonObject, String name) {
        return (JSONArray) jsonObject.get(name);
    }
}
