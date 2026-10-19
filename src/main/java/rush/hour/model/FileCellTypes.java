package rush.hour.model;

public enum FileCellTypes {
    HORIZONTAL_PLAYER('P', 'p'),
    VERTICAL_PLAYER('Q','q'),
    PLAYER_EXIT('E'),

    HORIZONTAL_AI('A', 'a'),
    VERTICAL_AI('B', 'b'),
    AI_EXIT('X'),

    HORIZONTAL_OBSTACLE('H', 'h'),
    VERTICAL_OBSTACLE('V', 'v'),

    EMPTY_CELL('.'),
    WALL('#');

    private final char head;
    private final char body;

    FileCellTypes(char head, char body){
        this.head = head;
        this.body = body;
    }
    FileCellTypes(char head){
        this.head = head;
        this.body = '\0';
    }

    public char getHead(){
        return head;
    }
    public char getBody() { return body;}

}
