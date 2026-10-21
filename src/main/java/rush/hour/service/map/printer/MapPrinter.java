package rush.hour.service.map.printer;

import rush.hour.model.*;

public class MapPrinter implements MapPrinterInterface {

    @Override
    public void printMap(MapVO map) {
        char[][] cellMap = getEmptyMap(map.getNumberOfRows(), map.getNumberOfColumns());

        //Adding vehicles
        for (VehicleVO obstacle : map.getObstacles()) {
            addVehicle(cellMap, obstacle, VehicleType.OBSTACLE);
        }

        addVehicle(cellMap, map.getHeroCarAI(), VehicleType.AI);

        addVehicle(cellMap, map.getHeroCarPlayer(), VehicleType.PLAYER);

        //Adding exits
        addExit(cellMap, map.getExitAI(), ExitType.AI);

        addExit(cellMap, map.getExitPlayer(), ExitType.PLAYER);

        for (int i = 0; i < map.getNumberOfRows(); i++) {
            System.out.println(cellMap[i]);
        }

    }

    private char[][] getEmptyMap(int row, int column) {
        char[][] map = new char[row][column];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                if (i == 0 || j == 0 || i == row - 1 || j == column - 1) {
                    map[i][j] = FileCellTypes.WALL.getHead();
                } else {
                    map[i][j] = FileCellTypes.EMPTY_CELL.getHead();
                }
            }
        }

        return map;
    }

    private void addVehicle(char[][] map, VehicleVO vehicle, VehicleType vehicleType) {
        int obstacleRow = vehicle.getVerticalPosition();
        int obstacleColumn = vehicle.getHorizontalPosition();
        char head;
        char body;
        if (vehicle.getOrientation() == Orientation.HORIZONTAL) {
            if (vehicleType == VehicleType.AI) {
                head = FileCellTypes.HORIZONTAL_AI.getHead();
                body = FileCellTypes.HORIZONTAL_AI.getBody();
            } else if (vehicleType == VehicleType.PLAYER) {
                head = FileCellTypes.HORIZONTAL_PLAYER.getHead();
                body = FileCellTypes.HORIZONTAL_PLAYER.getBody();
            } else {
                head = FileCellTypes.HORIZONTAL_OBSTACLE.getHead();
                body = FileCellTypes.HORIZONTAL_OBSTACLE.getBody();
            }
            map[obstacleRow][obstacleColumn] = head;
            for (int i = 1; i < vehicle.getVehicleLength(); i++) {
                map[obstacleRow][obstacleColumn - i] = body;
            }
        } else {
            if (vehicleType == VehicleType.AI) {
                head = FileCellTypes.VERTICAL_AI.getHead();
                body = FileCellTypes.VERTICAL_AI.getBody();
            } else if (vehicleType == VehicleType.PLAYER) {
                head = FileCellTypes.VERTICAL_PLAYER.getHead();
                body = FileCellTypes.VERTICAL_PLAYER.getBody();
            } else {
                head = FileCellTypes.VERTICAL_OBSTACLE.getHead();
                body = FileCellTypes.VERTICAL_OBSTACLE.getBody();
            }
            map[obstacleRow][obstacleColumn] = head;
            for (int i = 1; i < vehicle.getVehicleLength(); i++) {
                map[obstacleRow - i][obstacleColumn] = body;
            }
        }
    }

    private void addExit(char[][] map, ExitVO exit, ExitType exitType) {
        if (exitType == ExitType.AI) {
            map[exit.getVerticalPosition()][exit.getHorizontalPosition()] = FileCellTypes.AI_EXIT.getHead();
        } else {
            map[exit.getVerticalPosition()][exit.getHorizontalPosition()] = FileCellTypes.PLAYER_EXIT.getHead();
        }

    }
}
