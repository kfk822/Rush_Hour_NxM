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
            File file = new File( "maps\\" + fileName + ".json");
            Object object = new JSONParser().parse(new FileReader(file));
            JSONObject jsonObject = (JSONObject) object;
            JSONObject rawMap = (JSONObject) jsonObject.get("Map");
            int numberOfRows = ((Long) rawMap.get("NumberOfRows")).intValue();
            int numberOfColumns = ((Long) rawMap.get("NumberOfColumns")).intValue();
            JSONArray rawObstacles = (JSONArray) rawMap.get("Obstacles");
            JSONObject rawHeroCarPlayer = (JSONObject) rawMap.get("HeroCarPlayer");
            JSONObject rawHeroCarAI = (JSONObject) rawMap.get("HeroCarAI");
            JSONObject rawExitPlayer = (JSONObject) rawMap.get("ExitPlayer");
            JSONObject rawExitAI = (JSONObject) rawMap.get("ExitAI");
            List<VehicleVO> obstacles = new ArrayList<>();
            for (Object objectObstacle : rawObstacles) {
                JSONObject rawObstacle = (JSONObject) objectObstacle;
                obstacles.add(VehicleVO.builder()
                        .withHorizontalPosition(((Long) rawObstacle.get("HorizontalPosition")).intValue())
                        .withVerticalPosition(((Long)  rawObstacle.get("VerticalPosition")).intValue())
                        .withOrientation(Orientation.valueOf((String) rawObstacle.get("Orientation")))
                        .withVehicleLength(((Long) rawObstacle.get("Length")).intValue())
                        .build());
            }
            VehicleVO heroCarPlayer = VehicleVO.builder()
                    .withHorizontalPosition(((Long) rawHeroCarPlayer.get("HorizontalPosition")).intValue())
                    .withVerticalPosition(((Long)  rawHeroCarPlayer.get("VerticalPosition")).intValue())
                    .withOrientation(Orientation.valueOf((String) rawHeroCarPlayer.get("Orientation")))
                    .withVehicleLength(((Long)  rawHeroCarPlayer.get("Length")).intValue())
                    .build();
            VehicleVO heroCarAI = VehicleVO.builder()
                    .withHorizontalPosition(((Long)  rawHeroCarAI.get("HorizontalPosition")).intValue())
                    .withVerticalPosition(((Long)  rawHeroCarAI.get("VerticalPosition")).intValue())
                    .withOrientation(Orientation.valueOf((String) rawHeroCarAI.get("Orientation")))
                    .withVehicleLength(((Long)  rawHeroCarAI.get("Length")).intValue())
                    .build();
            ExitVO exitPlayer = ExitVO.builder()
                    .withHorizontalPosition(((Long)  rawExitPlayer.get("HorizontalPosition")).intValue())
                    .withVerticalPosition(((Long)  rawExitPlayer.get("VerticalPosition")).intValue())
                    .build();
            ExitVO exitAI = ExitVO.builder()
                    .withHorizontalPosition(((Long)  rawExitAI.get("HorizontalPosition")).intValue())
                    .withVerticalPosition(((Long)  rawExitAI.get("VerticalPosition")).intValue())
                    .build();
            return MapVO.builder()
                    .withNumberOfRows(numberOfRows)
                    .withNumberOfColumns(numberOfColumns)
                    .withObstacles(obstacles)
                    .withHeroCarPlayer(heroCarPlayer)
                    .withHeroCarAI(heroCarAI)
                    .withExitPlayer(exitPlayer)
                    .withExitAI(exitAI)
                    .build();
        } catch (IOException | ParseException e) {
            throw new JSONMapLoaderException(e.getMessage());
        }
    }
}
