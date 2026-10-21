package rush.hour.service.enemy;

import rush.hour.model.MapVO;
import rush.hour.model.MoveVO;
import rush.hour.model.Orientation;
import rush.hour.model.VehicleVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnemyAI {
    private List<MoveVO> chosenMoves;
    Random r;

    public EnemyAI(Random r) {
        chosenMoves = new ArrayList<>();
        this.r = r;
    }

    public MoveVO makeMove(MapVO map) {
        List<VehicleVO> vehicles = new ArrayList<>(map.getObstacles());
        vehicles.add(map.getHeroCarAI());
        vehicles.add(map.getHeroCarPlayer());

        MoveVO chosenMove;

        do {

            VehicleVO chosenVehicle = vehicles.get(r.nextInt(vehicles.size()));
            int targetRow;
            int targetColumn;

            if (chosenVehicle.getOrientation() == Orientation.HORIZONTAL) {
                targetRow = chosenVehicle.getVerticalPosition();
                targetColumn = r.nextInt(map.getNumberOfColumns()) + 1;
            } else {
                targetRow = r.nextInt(map.getNumberOfRows()) + 1;
                targetColumn = chosenVehicle.getHorizontalPosition();
            }

            chosenMove = new MoveVO.Builder()
                    .withVehicle(chosenVehicle)
                    .withTargetColumn(targetColumn)
                    .withTargetRow(targetRow)
                    .build();

        } while (chosenMoves.contains(chosenMove) ||
                chosenMove.getTargetRow() == chosenMove.getVehicle().getVerticalPosition() &&
                        chosenMove.getTargetColumn() == chosenMove.getVehicle().getHorizontalPosition());

        chosenMoves.add(chosenMove);

        return chosenMove;

    }

    public void nextTurn() {
        chosenMoves = new ArrayList<>();
    }

}
