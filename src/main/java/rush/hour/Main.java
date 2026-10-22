package rush.hour;

import rush.hour.GameEngine.GameEngine;
import rush.hour.GameEngine.RushHour;

public class Main {
    public static void main(String[] args) {
        RushHour rushHour = new RushHour();

        GameEngine gameEngine = new GameEngine(rushHour);

        gameEngine.run();
    }
}
