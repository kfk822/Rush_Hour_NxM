package rush.hour.service.map.validator;

import rush.hour.model.MapVO;
import rush.hour.model.Orientation;
import rush.hour.model.VehicleVO;

import java.util.ArrayList;
import java.util.List;

public class OccupiedMapGenerator {
    public boolean[][] generateOccupiedMap(MapVO map, VehicleVO excludedVehicle) {
        boolean[][] occupiedMap = new boolean[map.getNumberOfRows()][map.getNumberOfColumns()];

        generateEmptyMap(occupiedMap, map.getNumberOfRows(), map.getNumberOfColumns());

        List<VehicleVO> vehicles = new ArrayList<>(map.getObstacles());
        vehicles.add(map.getHeroCarPlayer());
        vehicles.add(map.getHeroCarAI());

        if (excludedVehicle != null) {
            vehicles.remove(excludedVehicle);
        }

        fillMap(occupiedMap, vehicles);


        return occupiedMap;
    }

    private void generateEmptyMap(boolean[][] occupiedMap, int numberOfRows, int numberOfColumns) {
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                occupiedMap[i][j] = isBorder(i, j, numberOfRows, numberOfColumns);
            }
        }
    }

    private void fillMap(boolean[][] occupiedMap, List<VehicleVO> vehicles) {
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
    }

    private boolean isBorder(int row, int column, int numberOfRows, int numberOfColumns) {
        return row == 0 ||
                column == 0 ||
                row == numberOfRows - 1 ||
                column == numberOfColumns - 1;
    }
}
