package game.model;

import java.io.Serializable;

public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    public Player player1;
    public Player player2;
    public int level;
    public char[][] map1;
    public char[][] map2;
    public int remainingRocks1;
    public int remainingRocks2;
    public long lastHitTimeP1 = 0;
    public long lastHitTimeP2 = 0;
    public boolean gameOver = false;

    public GameState(Player p1, Player p2, int level, char[][] map1, char[][] map2) {
        this.player1 = p1;
        this.player2 = p2;
        this.level = level;
        this.map1 = map1;
        this.map2 = map2;
        this.remainingRocks1 = countRocks(map1);
        this.remainingRocks2 = countRocks(map2);
        this.lastHitTimeP1 = 0;
        this.lastHitTimeP2 = 0;
    }

    int countRocks(char[][] map) {
        int count = 0;
        for (char[] row : map) {
            for (char c : row) {
                if (c == 'K') count++;
            }
        }
        return count;
    }
}
