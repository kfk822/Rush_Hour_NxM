package rush.hour.service.map.printer;

import rush.hour.model.*;

public class MapPrinter implements MapPrinterInterface {

    @Override
    public void printMap(MapVO map) {
        char[][] cellMap = getEmptyMap(map.getNumberOfRows(), map.getNumberOfColumns());

        for (VehicleVO obstacle : map.getObstacles()) {
            addVehicle(cellMap, obstacle, VehicleType.OBSTACLE);
        }

        addVehicle(cellMap, map.getHeroCarAI(), VehicleType.AI);

        addVehicle(cellMap, map.getHeroCarPlayer(), VehicleType.PLAYER);

        addExit(cellMap, map.getExitAI(), ExitType.AI);

        addExit(cellMap, map.getExitPlayer(), ExitType.PLAYER);

        // Print column headers
        for (int i = -1; i < map.getNumberOfColumns() - 1; i++) {
            if (i <= 0) {
                System.out.printf("%3c", ' ');
            } else {
                System.out.printf("%3d", i);
            }
        }
        System.out.println();
        for (int i = 0; i < map.getNumberOfRows(); i++) {
            //Print row headers
            if (i == 0 || i == map.getNumberOfRows() - 1) {
                System.out.printf("%3c", ' ');
            } else {
                System.out.printf("%3c", 'a' + i - 1);
            }

            for (int j = 0; j < map.getNumberOfColumns(); j++) {
                System.out.printf("%3c", cellMap[i][j]);
            }
            System.out.println();
        }

    }

    private char[][] getEmptyMap(int row, int column) {
        char[][] map = new char[row][column];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                if (i == 0 || j == 0 || i == row - 1 || j == column - 1) {
                    map[i][j] = CellTypes.WALL.getSymbol();
                } else {
                    map[i][j] = CellTypes.EMPTY_CELL.getSymbol();
                }
            }
        }

        return map;
    }

    private void addVehicle(char[][] map, VehicleVO vehicle, VehicleType vehicleType) {
        int row = vehicle.getVerticalPosition();
        int column = vehicle.getHorizontalPosition();
        char head;
        char body;
        if (vehicle.getOrientation() == Orientation.HORIZONTAL) {
            if (vehicleType == VehicleType.AI) {
                head = CellTypes.HORIZONTAL_AI.getSymbol();
            } else if (vehicleType == VehicleType.PLAYER) {
                head = CellTypes.HORIZONTAL_PLAYER.getSymbol();
            } else {
                head = CellTypes.HORIZONTAL_OBSTACLE.getSymbol();
            }
            map[row][column] = head;
            body = Character.toLowerCase(head);
            for (int i = 1; i < vehicle.getVehicleLength(); i++) {
                map[row][column - i] = body;
            }
        } else {
            if (vehicleType == VehicleType.AI) {
                head = CellTypes.VERTICAL_AI.getSymbol();
            } else if (vehicleType == VehicleType.PLAYER) {
                head = CellTypes.VERTICAL_PLAYER.getSymbol();
            } else {
                head = CellTypes.VERTICAL_OBSTACLE.getSymbol();
            }
            map[row][column] = head;
            body = Character.toLowerCase(head);
            for (int i = 1; i < vehicle.getVehicleLength(); i++) {
                map[row - i][column] = body;
            }
        }
    }

    private void addExit(char[][] map, ExitVO exit, ExitType exitType) {
        if (exitType == ExitType.AI) {
            map[exit.getVerticalPosition()][exit.getHorizontalPosition()] = CellTypes.AI_EXIT.getSymbol();
        } else {
            map[exit.getVerticalPosition()][exit.getHorizontalPosition()] = CellTypes.PLAYER_EXIT.getSymbol();
        }

    }
}
