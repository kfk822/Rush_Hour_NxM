package rush.hour.GameEngine;

public class GameEngine {
    Game game;

    public GameEngine(Game game){
        this.game = game;
    }

    public void run(){
        game.init();

        while (game.getIsRunning()){
            game.processInput();

            game.update();

            game.render();
        }

        game.terminate();
    }
}
