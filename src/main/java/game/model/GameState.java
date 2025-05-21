package game.model;

import java.io.Serializable;

public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    public Player player1;
    public Player player2;
    public int level;
    public transient char[][] map1;
    public transient char[][] map2;
    public transient int remainingRocks1;
    public transient int remainingRocks2;
    public long lastHitTimeP1 = 0;
    public long lastHitTimeP2 = 0;
    public boolean gameOver = false;


    /**
     * Constructs a new `GameState` instance.
     * Initializes the game state with the given players, level, and maps.
     * Also calculates the initial number of rocks remaining on each map.
     *
     * @param p1    The first player in the game.
     * @param p2    The second player in the game.
     * @param level The current level of the game.
     * @param map1  The 2D character array representing the first player's map.
     * @param map2  The 2D character array representing the second player's map.
     */
    public GameState(Player p1, Player p2, int level, char[][] map1, char[][] map2) {
        this.player1 = p1;
        this.player2 = p2;
        this.level = level;
        this.map1 = map1;
        this.map2 = map2;
        this.remainingRocks1 = countRocks(map1);
        this.remainingRocks2 = countRocks(map2);
    }

    /**
     * Counts the number of rocks on the given map.
     * This method iterates through the 2D character array and counts the occurrences
     * of the character 'K', which represents a rock.
     *
     * @param map The 2D character array representing the game map.
     * @return The total number of rocks found on the map.
     */
    public int countRocks(char[][] map) {
        int count = 0;
        for (char[] row : map) {
            for (char c : row) {
                if (c == 'K') count++;
            }
        }
        return count;
    }
}
