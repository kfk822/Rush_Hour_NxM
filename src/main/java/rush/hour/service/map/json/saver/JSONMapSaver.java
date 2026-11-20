package rush.hour.service.map.json.saver;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import rush.hour.model.MapVO;
import rush.hour.model.VehicleVO;
import rush.hour.service.exceptions.JSONMapSaverException;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

public class JSONMapSaver {

    public void saveMap(MapVO map, String fileName) throws JSONMapSaverException {
        try {
            JSONObject jsonObject = new JSONObject();

            JSONArray obstacles = new JSONArray();
            for (VehicleVO obstacle : map.getObstacles()) {
                Map rawObstackle = new LinkedHashMap(4);

                rawObstackle.put("HorizontalPosition", obstacle.getHorizontalPosition());
                rawObstackle.put("VerticalPosition", obstacle.getVerticalPosition());
                rawObstackle.put("Orientation", obstacle.getOrientation().toString());
                rawObstackle.put("Length", obstacle.getVehicleLength());

                obstacles.add(rawObstackle);

            }

            Map rawHeroCarPlayer = new LinkedHashMap(4);
            rawHeroCarPlayer.put("HorizontalPosition", map.getHeroCarPlayer().getHorizontalPosition());
            rawHeroCarPlayer.put("VerticalPosition", map.getHeroCarPlayer().getVerticalPosition());
            rawHeroCarPlayer.put("Orientation", map.getHeroCarPlayer().getOrientation().toString());
            rawHeroCarPlayer.put("Length", map.getHeroCarPlayer().getVehicleLength());

            Map rawHeroCarAI = new LinkedHashMap(4);
            rawHeroCarAI.put("HorizontalPosition", map.getHeroCarAI().getHorizontalPosition());
            rawHeroCarAI.put("VerticalPosition", map.getHeroCarAI().getVerticalPosition());
            rawHeroCarAI.put("Orientation", map.getHeroCarAI().getOrientation().toString());
            rawHeroCarAI.put("Length", map.getHeroCarAI().getVehicleLength());

            Map rawExitPlayer = new LinkedHashMap(2);
            rawExitPlayer.put("HorizontalPosition", map.getExitPlayer().getHorizontalPosition());
            rawExitPlayer.put("VerticalPosition", map.getExitPlayer().getVerticalPosition());

            Map rawExitAI = new LinkedHashMap(2);
            rawExitAI.put("HorizontalPosition", map.getExitAI().getHorizontalPosition());
            rawExitAI.put("VerticalPosition", map.getExitAI().getVerticalPosition());

            Map rawMap = new LinkedHashMap(7);
            rawMap.put("NumberOfRows", map.getNumberOfRows());
            rawMap.put("NumberOfColumns", map.getNumberOfColumns());
            rawMap.put("Obstacles", obstacles);
            rawMap.put("HeroCarPlayer", rawHeroCarPlayer);
            rawMap.put("HeroCarAI", rawHeroCarAI);
            rawMap.put("ExitPlayer", rawExitPlayer);
            rawMap.put("ExitAI", rawExitAI);
            jsonObject.put("Map", rawMap);

            File file = new File("maps\\"+fileName + ".json");
            file.createNewFile();
            PrintWriter printWriter = new PrintWriter(file);
            printWriter.write(jsonObject.toJSONString());
            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
            throw new JSONMapSaverException(e.getMessage());
        }
    }
}
