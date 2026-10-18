package rush.hour.service.map.parser;

import rush.hour.model.ExitVO;
import rush.hour.model.MapVO;
import rush.hour.model.Orientation;
import rush.hour.model.VehicleVO;

import java.util.ArrayList;
import java.util.List;

public class MapParser {
    private final int numberOfRows;
    private final int numberOfColumns;

    public MapParser(int numberOfRows, int numberOFColumns){
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOFColumns;
    }

    public MapVO parseMap(List<String> rawMap){
        VehicleVO heroCarPlayer = null;
        VehicleVO heroCarAI = null;
        List<VehicleVO> obstacles = new ArrayList<>();
        ExitVO exitPlayer = null;
        ExitVO exitAI = null;

        for(int row = 0; row < rawMap.size(); row++){
            for(int column=0; column < rawMap.get(row).length(); column++){
                char currentElement = rawMap.get(row).charAt(column);
                if(currentElement == FileCellTypes.VERTICAL_OBSTACLE.getHead() ||
                        currentElement == FileCellTypes.HORIZONTAL_OBSTACLE.getHead()){
                    obstacles.add(createVehicle(rawMap, row, column));
                }
                else if(currentElement == FileCellTypes.VERTICAL_PLAYER.getHead() ||
                        currentElement == FileCellTypes.HORIZONTAL_PLAYER.getHead()){
                    heroCarPlayer = createVehicle(rawMap, row, column);
                }
                else if(currentElement == FileCellTypes.VERTICAL_AI.getHead() ||
                    currentElement == FileCellTypes.HORIZONTAL_AI.getHead()){
                    heroCarAI = createVehicle(rawMap, row, column);
                }
                else if(currentElement == FileCellTypes.AI_EXIT.getHead()){
                    exitAI = createExit(row, column);
                }
                else if(currentElement == FileCellTypes.PLAYER_EXIT.getHead()){
                    exitPlayer = createExit(row,column);
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

    private VehicleVO createVehicle(List<String> rawMap, int row, int column){
        int length;
        Orientation orientation;

        char vehicleHead = rawMap.get(row).charAt(column);
        char vehicleBody;

        if(vehicleHead == FileCellTypes.HORIZONTAL_AI.getHead() ||
            vehicleHead == FileCellTypes.HORIZONTAL_PLAYER.getHead() ||
            vehicleHead == FileCellTypes.HORIZONTAL_OBSTACLE.getHead()){
            orientation = Orientation.HORIZONTAL;
        }
        else{
            orientation = Orientation.VERTICAL;
        }

        if(orientation == Orientation.HORIZONTAL){
            vehicleBody = rawMap.get(row).charAt(column -2);
        }
        else{
            vehicleBody = rawMap.get(row-2).charAt(column);
        }

        if(vehicleBody == Character.toLowerCase(vehicleHead)){
            length = 3;
        }
        else {
            length = 2;
        }

        return VehicleVO.builder()
                .withHorizontalPosition(column)
                .withVerticalPosition(row)
                .withVehicleLength(length)
                .withOrientation(orientation)
                .build();

    }

    private ExitVO createExit(int row, int column){
        return ExitVO.builder()
                .withHorizontalPosition(column)
                .withVerticalPosition(row)
                .build();
    }
}
