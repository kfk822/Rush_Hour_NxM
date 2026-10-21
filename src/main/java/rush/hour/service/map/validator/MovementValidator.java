package rush.hour.service.map.validator;

import rush.hour.model.MapVO;
import rush.hour.model.MoveVO;
import rush.hour.model.Orientation;
import rush.hour.service.exceptions.MovementValidatorException;

import java.awt.*;

public class MovementValidator {
    OccupiedMapGenerator occupiedMapGenerator;

    public MovementValidator() {
        occupiedMapGenerator = new OccupiedMapGenerator();
    }

    public void validateMove(MapVO map, MoveVO move) throws MovementValidatorException {
        boolean[][] occupiedMap = occupiedMapGenerator.generateOccupiedMap(map, move.getVehicle());

        int pointA;
        int pointB;

        if (move.getVehicle().getOrientation() == Orientation.HORIZONTAL) {
            if (move.getTargetRow() != move.getVehicle().getVerticalPosition()) {
                throw new MovementValidatorException("Move not possible");
            }
            if (move.getVehicle().getHorizontalPosition() > move.getTargetColumn()) {
                pointA = move.getTargetColumn() - move.getVehicle().getVehicleLength() + 1;
                pointB = move.getVehicle().getHorizontalPosition();
            } else {
                pointA = move.getVehicle().getHorizontalPosition() - move.getVehicle().getVehicleLength() + 1;
                pointB = move.getTargetColumn();
            }
            if (pointA < 0 || pointB > map.getNumberOfColumns()) {
                throw new MovementValidatorException("Move not possible");
            }
            while (pointA != pointB + 1) {
                if (occupiedMap[move.getTargetRow()][pointA]) {
                    throw new MovementValidatorException("Move not possible");
                }
                pointA++;
            }
        } else {
            if (move.getTargetColumn() != move.getVehicle().getHorizontalPosition()) {
                throw new MovementValidatorException("Move not possible");
            }
            if (move.getVehicle().getVerticalPosition() > move.getTargetRow()) {
                pointA = move.getTargetRow() - move.getVehicle().getVehicleLength() + 1;
                pointB = move.getVehicle().getVerticalPosition();
            } else {
                pointA = move.getVehicle().getVerticalPosition() - move.getVehicle().getVehicleLength() + 1;
                pointB = move.getTargetRow();
            }
            if (pointA < 0 || pointB > map.getNumberOfRows()) {
                throw new MovementValidatorException("Move not possible");
            }
            while (pointA != pointB + 1) {
                if (occupiedMap[pointA][move.getVehicle().getHorizontalPosition()]) {
                    throw new MovementValidatorException("Move not possible");
                }
                pointA++;
            }
        }

    }
}
