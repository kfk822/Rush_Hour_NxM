package rush.hour.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapVO {
    private final int numberOfRows;
    private final int numberOfColumns;
    private final List<VehicleVO> obstacles;
    private final VehicleVO heroCarPlayer;
    private final ExitVO exitPlayer;
    private final VehicleVO heroCarAI;
    private final ExitVO exitAI;

    private MapVO(Builder builder){
        numberOfRows = builder.numberOfRows;
        numberOfColumns = builder.numberOfColumns;
        obstacles = List.copyOf(builder.obstacles);
        heroCarPlayer = builder.heroCarPlayer;
        exitPlayer = builder.exitPlayer;
        heroCarAI = builder.heroCarAI;
        exitAI = builder.exitAI;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    public List<VehicleVO> getObstacles() {
        return List.copyOf(obstacles);
    }

    public VehicleVO getHeroCarPlayer() {
        return heroCarPlayer;
    }

    public VehicleVO getHeroCarAI() {
        return heroCarAI;
    }

    public ExitVO getExitPlayer(){return exitPlayer;}

    public ExitVO getExitAI(){return exitAI;}

    public boolean isOccupied(int verticalPosition, int horizontalPosition){
        List<VehicleVO> vehicles = new ArrayList<>(obstacles);
        vehicles.add(heroCarAI);
        vehicles.add(heroCarPlayer);

        for(VehicleVO vehicle : vehicles){
            int vehicleLength = vehicle.getVehicleLength();
            int vehicleVerticalPosition = vehicle.getVerticalPosition();
            int vehicleHorizontalPosition = vehicle.getHorizontalPosition();
            if(vehicle.getOrientation() == Orientation.VERTICAL){
                if(horizontalPosition == vehicleHorizontalPosition && verticalPosition > (vehicleVerticalPosition - vehicleLength) && verticalPosition <= vehicleVerticalPosition){
                    return true;
                }
            }
            else{
                if(verticalPosition == vehicleVerticalPosition && horizontalPosition > (vehicleHorizontalPosition - vehicleLength) && horizontalPosition <= vehicleHorizontalPosition){
                    return true;
                }
            }
        }

        return false;
    }
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()){
            return false;
        }

        MapVO mapVO = (MapVO) o;

        return numberOfRows == mapVO.numberOfRows &&
                numberOfColumns == mapVO.numberOfColumns &&
                Objects.equals(obstacles, mapVO.obstacles) &&
                Objects.equals(heroCarPlayer, mapVO.heroCarPlayer) &&
                Objects.equals(heroCarAI, mapVO.heroCarAI) &&
                Objects.equals(exitAI, mapVO.exitAI) &&
                Objects.equals(exitPlayer, mapVO.exitPlayer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfRows, numberOfColumns, obstacles, heroCarPlayer, heroCarAI,exitAI,exitPlayer);
    }

    @Override
    public String toString(){
        String map = "";
        map+= "numberOfRows: " + numberOfRows;
        map+= "\nnumberOfColumns: " + numberOfColumns;
        map+= "\nobstacles: ";
        for(VehicleVO obstacle : obstacles){
            map+= "\n" + obstacle;
        }
        map+= "\nheroCarPlayer" + heroCarPlayer;
        map+= "\nexitPlayer" + exitPlayer;
        map+= "\nheroCarAI" + heroCarAI;
        map+= "\nexitAI" + exitAI;
        return map;
    }

    public MapVO withValue(int verticalPosition, int horizontalPosition, VehicleVO vehicle){
        Builder result = builder()
                .withNumberOfRows(numberOfRows)
                .withNumberOfColumns(numberOfColumns)
                .withObstacles(obstacles)
                .withHeroCarAI(heroCarAI)
                .withExitAI(exitAI)
                .withHeroCarPlayer(heroCarPlayer)
                .withExitPlayer(exitPlayer);

        if (heroCarAI.equals(vehicle)){
            VehicleVO newHeroCarAI = heroCarAI.withValue(verticalPosition, horizontalPosition);
            result.withHeroCarAI(newHeroCarAI);
        }
        else if(heroCarPlayer.equals(vehicle)){
            VehicleVO newHeroCarPlayer = heroCarPlayer.withValue(verticalPosition, horizontalPosition);
            result.withHeroCarPlayer(newHeroCarPlayer);
        }
        else if(obstacles.contains(vehicle)){
            List<VehicleVO> newObstacles = new ArrayList<>(obstacles);
            int index = newObstacles.indexOf(vehicle);

            VehicleVO newObstacle = vehicle.withValue(verticalPosition,horizontalPosition);
            newObstacles.set(index,newObstacle);
            result.withObstacles(newObstacles);
        }
        else {
            throw new IllegalArgumentException("The specified vehicle does not exist on this map.");
        }

        return result.build();
    }

    public static Builder builder(){
        return new Builder();
    }

    public static final class Builder{
        private int numberOfRows;
        private int numberOfColumns;
        private List<VehicleVO> obstacles;
        private VehicleVO heroCarPlayer;
        private ExitVO exitPlayer;
        private VehicleVO heroCarAI;
        private ExitVO exitAI;

        public Builder withNumberOfRows(int numberOfRows){
            this.numberOfRows = numberOfRows;
            return this;
        }

        public Builder withNumberOfColumns(int numberOfColumns){
            this.numberOfColumns = numberOfColumns;
            return this;
        }

        public Builder withObstacles(List<VehicleVO> obstacles){
            this.obstacles = obstacles;
            return this;
        }

        public Builder withHeroCarPlayer(VehicleVO heroCarPlayer){
            this.heroCarPlayer = heroCarPlayer;
            return this;
        }

        public Builder withHeroCarAI(VehicleVO heroCarAI){
            this.heroCarAI = heroCarAI;
            return this;
        }

        public Builder withExitAI(ExitVO exitAI){
            this.exitAI = exitAI;
            return this;
        }

        public Builder withExitPlayer(ExitVO exitPlayer){
            this.exitPlayer = exitPlayer;
            return this;
        }

        public MapVO build(){
            if(heroCarAI == null || heroCarPlayer == null){
                throw new IllegalStateException("The board must contain a Player and an AI car!");
            }
            if(exitPlayer == null || exitAI == null){
                throw new IllegalStateException("The board must contain a Player and an AI exit!");
            }

            return new MapVO(this);
        }
    }

}
