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
    private final Random random;

    public EnemyAI(Random random) {
        chosenMoves = new ArrayList<>();
        this.random = random;
    }

    public MoveVO makeMove(MapVO map) {
        List<VehicleVO> vehicles = getAllVehicles(map);

        MoveVO chosenMove;

        do {
            int randomVehicleIndex = random.nextInt(vehicles.size());
            VehicleVO chosenVehicle = vehicles.get(randomVehicleIndex);

            // Move along the vehicle's orientation
            int targetRow = chosenVehicle.getVerticalPosition();
            int targetColumn = chosenVehicle.getHorizontalPosition();

            // Exclude the first and last rows/columns since they are walls
            if (chosenVehicle.getOrientation() == Orientation.HORIZONTAL) {
                targetColumn = random.nextInt(map.getNumberOfColumns() - 2) + 1;
            } else {
                targetRow = random.nextInt(map.getNumberOfRows() - 2) + 1;
            }

            chosenMove = new MoveVO.Builder()
                    .withVehicle(chosenVehicle)
                    .withTargetColumn(targetColumn)
                    .withTargetRow(targetRow)
                    .build();

        } while (isInvalidMove(chosenMove));

        chosenMoves.add(chosenMove);

        return chosenMove;

    }

    public void nextTurn() {
        chosenMoves = new ArrayList<>();
    }

    private List<VehicleVO> getAllVehicles(MapVO map) {
        List<VehicleVO> vehicles = new ArrayList<>(map.getObstacles());
        vehicles.add(map.getHeroCarAI());
        vehicles.add(map.getHeroCarPlayer());

        return vehicles;
    }

    private boolean isInvalidMove(MoveVO move) {
        boolean isStationary = move.getTargetRow() == move.getVehicle().getVerticalPosition() &&
                move.getTargetColumn() == move.getVehicle().getHorizontalPosition();

        return chosenMoves.contains(move) || isStationary;

    }

}
