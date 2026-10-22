package rush.hour.GameEngine;

public interface Game {
    public void init();
    public void processInput();
    public void update();
    public void render();
    public void terminate();
    public boolean getIsRunning();
}
