package rush.hour.model;

public class GameState {
    State state;

    public void setState(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }
}
