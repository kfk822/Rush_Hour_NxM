package rush.hour.service.move;

import rush.hour.model.MapVO;
import rush.hour.model.MoveVO;
import rush.hour.model.VehicleVO;
import rush.hour.service.exceptions.MoveMakerException;

import java.util.ArrayList;
import java.util.List;

public class MoveMaker {
    public MoveVO makeMove(String rawMove, MapVO map) {
        String[] positions = rawMove.split("->");
        int vehicleRow = positions[0].charAt(0) - 'a' + 1;
        int vehicleColumn = Integer.parseInt(positions[0].substring(1));
        int targetRow = positions[1].charAt(0) - 'a' + 1;
        int targetColumn = Integer.parseInt(positions[1].substring(1));

        VehicleVO vehicleToMove = null;

        List<VehicleVO> vehicles = new ArrayList<>(map.getObstacles());
        vehicles.add(map.getHeroCarPlayer());
        vehicles.add(map.getHeroCarAI());

        for (VehicleVO vehicle : vehicles) {
            if (vehicleColumn == vehicle.getHorizontalPosition() && vehicleRow == vehicle.getVerticalPosition()) {
                vehicleToMove = vehicle;
            }
        }

        if (vehicleToMove == null) {
            throw new MoveMakerException("Invalid vehicle head position!");
        }

        return new MoveVO.Builder()
                .withTargetColumn(targetColumn)
                .withTargetRow(targetRow)
                .withVehicle(vehicleToMove)
                .build();
    }
}
