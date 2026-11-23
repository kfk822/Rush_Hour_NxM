package rush.hour.model;

public enum Choice {
    PLAY(1),
    LOAD_GAME(2),
    SHOW_LEADERBOARD(3),
    QUIT_PROGRAM(4),
    SAVE_AND_QUIT_GAME(1),
    QUIT_GAME(2);

    private final int choice;

    Choice(int choice){
        this.choice = choice;
    }

    public String getValue(){
        return String.valueOf(choice);
    }
}
