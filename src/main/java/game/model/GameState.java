package game.model;

import java.io.Serializable;

public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    public Player player1;
    public Player player2;
    public int level;
    public char[][] currentMap;

    public GameState(Player p1, Player p2, int level, char[][] map) {
        this.player1 = p1;
        this.player2 = p2;
        this.level = level;
        this.currentMap = map;
    }
}