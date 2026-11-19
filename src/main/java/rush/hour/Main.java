package rush.hour;

import rush.hour.gameengine.GameEngine;
import rush.hour.gameengine.RushHour;

public class Main {
    public static void main(String[] args) {
        RushHour rushHour = new RushHour();

        GameEngine gameEngine = new GameEngine(rushHour);

        gameEngine.run();
    }
}
