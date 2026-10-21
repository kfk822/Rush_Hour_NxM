package rush.hour.service.map.validator;

import rush.hour.model.MapVO;
import rush.hour.model.Orientation;
import rush.hour.model.VehicleVO;

import java.util.ArrayList;
import java.util.List;

public class OccupiedMapGenerator {
    public boolean[][] generateOccupiedMap(MapVO map, VehicleVO excludedVehicle) {
        boolean[][] occupiedMap = new boolean[map.getNumberOfRows()][map.getNumberOfColumns()];

        for (int i = 0; i < map.getNumberOfRows(); i++) {
            for (int j = 0; j < map.getNumberOfColumns(); j++) {
                if (i == 0 || i == map.getNumberOfRows() - 1 ||
                        j == 0 || j == map.getNumberOfColumns() - 1) {
                    occupiedMap[i][j] = true;
                } else {
                    occupiedMap[i][j] = false;
                }
            }
        }

        List<VehicleVO> vehicles = new ArrayList<>(map.getObstacles());
        vehicles.add(map.getHeroCarPlayer());
        vehicles.add(map.getHeroCarAI());
        if(excludedVehicle != null) {
            vehicles.remove(excludedVehicle);
        }
        for (VehicleVO vehicle : vehicles) {
            int length = vehicle.getVehicleLength();
            int horizontalPosition = vehicle.getHorizontalPosition();
            int verticalPosition = vehicle.getVerticalPosition();
            if (vehicle.getOrientation() == Orientation.HORIZONTAL) {
                for (int i = 0; i < length; i++) {
                    occupiedMap[verticalPosition][horizontalPosition - i] = true;
                }
            } else {
                for (int i = 0; i < length; i++) {
                    occupiedMap[verticalPosition - i][horizontalPosition] = true;
                }

            }
        }

        return occupiedMap;
    }
}
