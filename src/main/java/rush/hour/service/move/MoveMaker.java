package rush.hour.service.move;

import rush.hour.model.MapVO;
import rush.hour.model.MoveVO;
import rush.hour.model.VehicleVO;
import rush.hour.service.exceptions.MoveMakerException;

import java.util.ArrayList;
import java.util.List;

public class MoveMaker {

    public MoveVO makeMove(String rawMove, MapVO map) {
        if (rawMove == null) {
            throw new MoveMakerException("Move cannot be null!");
        }

        String[] positions = rawMove.split("->");
        if (positions.length != 2 || positions[0].length() < 2 || positions[1].length() < 2) {
            throw new MoveMakerException("Invalid move format!");
        }

        int vehicleRow;
        int vehicleColumn;
        int targetRow;
        int targetColumn;

        try {
            vehicleRow = parseRow(positions[0].charAt(0));
            vehicleColumn = Integer.parseInt(positions[0].substring(1));
            targetRow = parseRow(positions[1].charAt(0));
            targetColumn = Integer.parseInt(positions[1].substring(1));
        } catch (NumberFormatException e) {
            throw new MoveMakerException("Invalid move coordinates!");
        }

        List<VehicleVO> vehicles = getAllVehicles(map);
        VehicleVO vehicleToMove = findVehicleToMove(vehicles, vehicleColumn, vehicleRow);

        if (vehicleToMove == null) {
            throw new MoveMakerException("Invalid vehicle head position!");
        }

        return new MoveVO.Builder()
                .withTargetColumn(targetColumn)
                .withTargetRow(targetRow)
                .withVehicle(vehicleToMove)
                .build();
    }

    private int parseRow(char character) {
        return character - 'a' + 1;
    }

    private List<VehicleVO> getAllVehicles(MapVO map) {
        List<VehicleVO> vehicles = new ArrayList<>(map.getObstacles());
        vehicles.add(map.getHeroCarPlayer());
        vehicles.add(map.getHeroCarAI());
        return vehicles;
    }

    private VehicleVO findVehicleToMove(List<VehicleVO> vehicles, int vehicleColumn, int vehicleRow) {
        for (VehicleVO vehicle : vehicles) {
            if (vehicleColumn == vehicle.getHorizontalPosition() && vehicleRow == vehicle.getVerticalPosition()) {
                return vehicle;
            }
        }
        return null;
    }
}