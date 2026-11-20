package rush.hour.service.ui.printer;

import rush.hour.model.State;

public class ConsolePrinter {
    public void printGameStateChoices(State state) {
        if (state == State.BEFORE_GAME) {
            System.out.println("What do you want to do?");
            System.out.println("(1) Play game.");
            System.out.println("(2) Load previous game.");
            System.out.println("(3) Show leaderboard.");
            System.out.println("(4) Quit the game.");
        }
        if (state == State.IN_GAME) {
            System.out.println("Make your move!");
            System.out.println("Move format [(Vehicle head position)->(New Vehicle head position)]: a1->a2");
            System.out.println("(1) Save the game and quit");
            System.out.println("(2) Quit the game.");
        }

    }

    public void printMessage(String message) {
        System.out.println(message);
    }

}
