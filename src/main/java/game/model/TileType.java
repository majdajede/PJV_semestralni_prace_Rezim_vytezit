package game.model;

public enum TileType {
    EMPTY(' '),
    ROCK('K'),
    PLAYER('P'),
    FORBIDDEN('Z');

    public final char symbol;

    TileType(char symbol) {
        this.symbol = symbol;
    }

    public static TileType fromChar(char c) {
        for (TileType t : values()) {
            if (t.symbol == c) return t;
        }
        return EMPTY;
    }
}