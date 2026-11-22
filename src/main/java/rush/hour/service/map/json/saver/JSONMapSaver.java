package rush.hour.service.map.json.saver;

import org.json.simple.JSONObject;
import rush.hour.model.ExitVO;
import rush.hour.model.MapVO;
import rush.hour.model.VehicleVO;
import rush.hour.service.exceptions.JSONMapSaverException;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JSONMapSaver {

    public void saveMap(MapVO map, String fileName) throws JSONMapSaverException {
        File file = new File("maps", fileName + ".json");
        try (PrintWriter printWriter = new PrintWriter(file)) {
            printWriter.write(getRawMap(map).toJSONString());
        } catch (IOException e) {
            throw new JSONMapSaverException(e.getMessage());
        }
    }

    private Map<String, Object> getRawVehicleVO(VehicleVO vehicleVO) {
        Map<String, Object> rawVehicleVO = new LinkedHashMap<>(4);
        rawVehicleVO.put("HorizontalPosition", vehicleVO.getHorizontalPosition());
        rawVehicleVO.put("VerticalPosition", vehicleVO.getVerticalPosition());
        rawVehicleVO.put("Orientation", vehicleVO.getOrientation().toString());
        rawVehicleVO.put("Length", vehicleVO.getVehicleLength());
        return rawVehicleVO;

    }

    private Map<String, Integer> getRawExitVO(ExitVO exitVO) {
        Map<String, Integer> rawExitVO = new LinkedHashMap<>(2);
        rawExitVO.put("HorizontalPosition", exitVO.getHorizontalPosition());
        rawExitVO.put("VerticalPosition", exitVO.getVerticalPosition());
        return rawExitVO;
    }

    private JSONObject getRawMap(MapVO map) {

        List<Object> rawObstacles = new ArrayList<>();
        for (VehicleVO obstacle : map.getObstacles()) {
            rawObstacles.add(getRawVehicleVO(obstacle));

        }

        Map<String, Object> rawMap = new LinkedHashMap<>(7);
        rawMap.put("NumberOfRows", map.getNumberOfRows());
        rawMap.put("NumberOfColumns", map.getNumberOfColumns());
        rawMap.put("Obstacles", rawObstacles);
        rawMap.put("HeroCarPlayer", getRawVehicleVO(map.getHeroCarPlayer()));
        rawMap.put("HeroCarAI", getRawVehicleVO(map.getHeroCarAI()));
        rawMap.put("ExitPlayer", getRawExitVO(map.getExitPlayer()));
        rawMap.put("ExitAI", getRawExitVO(map.getExitAI()));

        return new JSONObject(Map.of("Map", rawMap));
    }
}
