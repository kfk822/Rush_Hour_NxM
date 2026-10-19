package rush.hour.config;

public class GameConfig {
    private static class Holder{
        private static final GameConfig INSTANCE = new GameConfig();
    }
    private final int numberOfRows = 6;
    private final int numberOfColumns = 12;
    private final int wallSize = 2;
    private final String baseMapPath = "map/map.txt";

    private GameConfig(){

    }

    public static GameConfig getInstance(){
        return  Holder.INSTANCE;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    public int getWallSize() {
        return wallSize;
    }

    public String getBaseMapPath() {
        return baseMapPath;
    }
}
