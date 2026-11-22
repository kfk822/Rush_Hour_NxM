package rush.hour.model;

public enum CellTypes {
    HORIZONTAL_PLAYER('P'),
    VERTICAL_PLAYER('Q'),
    PLAYER_EXIT('E'),

    HORIZONTAL_AI('A'),
    VERTICAL_AI('B'),
    AI_EXIT('X'),

    HORIZONTAL_OBSTACLE('H'),
    VERTICAL_OBSTACLE('V'),

    EMPTY_CELL('.'),
    WALL('#');

    private final char symbol;

    CellTypes(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }

}
