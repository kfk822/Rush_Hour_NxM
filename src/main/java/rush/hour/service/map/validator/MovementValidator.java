package rush.hour.service.map.validator;

import rush.hour.model.MapVO;
import rush.hour.model.MoveVO;
import rush.hour.model.Orientation;
import rush.hour.model.VehicleVO;
import rush.hour.service.exceptions.MovementValidatorException;

public class MovementValidator {
    OccupiedMapGenerator occupiedMapGenerator;

    public MovementValidator() {
        occupiedMapGenerator = new OccupiedMapGenerator();
    }

    public void validateMove(MapVO map, MoveVO move) throws MovementValidatorException {
        boolean[][] occupiedMap = occupiedMapGenerator.generateOccupiedMap(map, move.getVehicle());

        int startPoint;
        int endPoint;

        VehicleVO vehicle = move.getVehicle();
        int targetRow = move.getTargetRow();
        int targetColumn = move.getTargetColumn();

        if (vehicle.getOrientation() == Orientation.HORIZONTAL) {
            // Forward move: check from the vehicle's current front to the target position.
            // Backward move: check from the vehicle's new rear to its current front.
            if (isVehicleMovesForward(vehicle.getHorizontalPosition(), targetColumn)) {
                startPoint = vehicle.getHorizontalPosition();
                endPoint = targetColumn;
            } else {
                startPoint = targetColumn - vehicle.getVehicleLength() + 1;
                endPoint = vehicle.getHorizontalPosition();
            }

            if (!isAlignedWithTarget(vehicle.getVerticalPosition(), targetRow) ||
                    isOutOfBounds(startPoint, endPoint, map.getNumberOfColumns()) ||
                    !isMoveLineFree(vehicle.getVerticalPosition(), targetRow, startPoint, endPoint, occupiedMap)) {
                throw new MovementValidatorException("Move not possible");
            }

        } else {
            // Forward move: check from the vehicle's current front to the target position.
            // Backward move: check from the vehicle's new rear to its current front.
            if (isVehicleMovesForward(vehicle.getVerticalPosition(), targetRow)) {
                startPoint = vehicle.getVerticalPosition();
                endPoint = targetRow;
            } else {
                startPoint = targetRow - vehicle.getVehicleLength() + 1;
                endPoint = vehicle.getVerticalPosition();
            }

            if (!isAlignedWithTarget(vehicle.getHorizontalPosition(), targetColumn) ||
                    isOutOfBounds(startPoint, endPoint, map.getNumberOfRows()) ||
                    !isMoveLineFree(startPoint, endPoint, vehicle.getHorizontalPosition(), targetColumn, occupiedMap)) {
                throw new MovementValidatorException("Move not possible");
            }
        }

    }

    private boolean isAlignedWithTarget(int vehiclePosition, int target) throws MovementValidatorException {
        return vehiclePosition == target;
    }

    private boolean isVehicleMovesForward(int vehiclePosition, int target) {
        return vehiclePosition < target;
    }

    private boolean isOutOfBounds(int startpoint, int endpoint, int mapSize) {
        return startpoint < 0 || endpoint >= mapSize;
    }

    private boolean isMoveLineFree(int startRow, int endRow, int startColumn, int endColumn, boolean[][] occupiedMap) {


        while (startColumn != endColumn || startRow != endRow) {
            if (occupiedMap[startRow][startColumn]) {
                return false;
            }

            //Check if start equals end if not increment it
            startColumn = startColumn == endColumn ? startColumn : startColumn + 1;
            startRow = startRow == endRow ? startRow : startRow + 1;
        }
        return !occupiedMap[endRow][endColumn];
    }

}
