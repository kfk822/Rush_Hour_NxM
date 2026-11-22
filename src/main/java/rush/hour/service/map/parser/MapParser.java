package rush.hour.service.map.parser;

import rush.hour.model.CellTypes;
import rush.hour.model.ExitVO;
import rush.hour.model.MapVO;
import rush.hour.model.Orientation;
import rush.hour.model.VehicleVO;

import java.util.ArrayList;
import java.util.List;

public class MapParser {
    private final int numberOfRows;
    private final int numberOfColumns;
    private final List<Character> horizontalVehicles;

    public MapParser(int numberOfRows, int numberOFColumns) {
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOFColumns;
        this.horizontalVehicles = new ArrayList<>();

        fillSymbols();
    }

    public MapVO parseMap(List<String> rawMap) {
        VehicleVO heroCarPlayer = null;
        VehicleVO heroCarAI = null;
        List<VehicleVO> obstacles = new ArrayList<>();
        ExitVO exitPlayer = null;
        ExitVO exitAI = null;

        for (int row = 0; row < numberOfRows; row++) {
            for (int column = 0; column < numberOfColumns; column++) {
                char currentElement = rawMap.get(row).charAt(column);
                if (currentElement == CellTypes.VERTICAL_OBSTACLE.getSymbol() ||
                        currentElement == CellTypes.HORIZONTAL_OBSTACLE.getSymbol()) {
                    obstacles.add(createVehicle(rawMap, row, column));
                } else if (currentElement == CellTypes.VERTICAL_PLAYER.getSymbol() ||
                        currentElement == CellTypes.HORIZONTAL_PLAYER.getSymbol()) {
                    heroCarPlayer = createVehicle(rawMap, row, column);
                } else if (currentElement == CellTypes.VERTICAL_AI.getSymbol() ||
                        currentElement == CellTypes.HORIZONTAL_AI.getSymbol()) {
                    heroCarAI = createVehicle(rawMap, row, column);
                } else if (currentElement == CellTypes.AI_EXIT.getSymbol()) {
                    exitAI = createExit(row, column);
                } else if (currentElement == CellTypes.PLAYER_EXIT.getSymbol()) {
                    exitPlayer = createExit(row, column);
                }
            }
        }

        return MapVO.builder()
                .withNumberOfRows(numberOfRows)
                .withNumberOfColumns(numberOfColumns)
                .withObstacles(obstacles)
                .withHeroCarPlayer(heroCarPlayer)
                .withExitPlayer(exitPlayer)
                .withHeroCarAI(heroCarAI)
                .withExitAI(exitAI)
                .build();
    }

    private VehicleVO createVehicle(List<String> rawMap, int row, int column) {
        int length;
        Orientation orientation;

        char vehicleHead = rawMap.get(row).charAt(column);
        char vehicleBody;

        if (horizontalVehicles.contains(vehicleHead)) {
            orientation = Orientation.HORIZONTAL;
            vehicleBody = rawMap.get(row).charAt(column - 2);
        } else {
            orientation = Orientation.VERTICAL;
            vehicleBody = rawMap.get(row - 2).charAt(column);
        }

        if (vehicleBody == Character.toLowerCase(vehicleHead)) {
            length = 3;
        } else {
            length = 2;
        }

        return VehicleVO.builder()
                .withHorizontalPosition(column)
                .withVerticalPosition(row)
                .withVehicleLength(length)
                .withOrientation(orientation)
                .build();

    }

    private ExitVO createExit(int row, int column) {
        return ExitVO.builder()
                .withHorizontalPosition(column)
                .withVerticalPosition(row)
                .build();
    }

    private void fillSymbols() {
        horizontalVehicles.add(CellTypes.HORIZONTAL_PLAYER.getSymbol());
        horizontalVehicles.add(CellTypes.HORIZONTAL_AI.getSymbol());
        horizontalVehicles.add(CellTypes.HORIZONTAL_OBSTACLE.getSymbol());
    }

}
