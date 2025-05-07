package game.model;

import java.io.Serializable;

public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    public Player player1;
    public Player player2;
    public int level;
    public char[][] map1;
    public char[][] map2;
    public long lastHitTimeP1 = 0;
    public long lastHitTimeP2 = 0;

    public GameState(Player p1, Player p2, int level, char[][] map1, char[][] map2) {
        this.player1 = p1;
        this.player2 = p2;
        this.level = level;
        this.map1 = map1;
        this.map2 = map2;
    }
}
