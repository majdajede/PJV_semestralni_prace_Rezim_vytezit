package game.model;

import java.io.Serializable;

public class Player implements Serializable {
    private static final long serialVersionUID = 1L;

    public int x, y;
    public int lives;
    public int dx = 0, dy = 0;

    public Player(int x, int y, int lives) {
        this.x = x;
        this.y = y;
        this.lives = lives;
    }

    /**
     * Moves the player to a new position based on the current direction.
     * This method calculates the new position by adding the direction values (`dx` and `dy`)
     * to the current position (`x` and `y`). If the new position is within the map boundaries
     * and does not contain an obstacle ('X'), the player's position is updated.
     *
     * @param map The 2D character array representing the game map.
     */
    public void move(char[][] map) {
        int newX = x + dx;
        int newY = y + dy;

        if (isInsideMap(newX, newY, map) && map[newY][newX] != 'X') {
            x = newX;
            y = newY;
        }
    }

    /**
     * Sets the direction of the player's movement.
     * This method updates the horizontal (`dx`) and vertical (`dy`) direction
     * values, which determine the player's next movement on the map.
     *
     * @param dx The horizontal direction value (-1 for left, 1 for right, 0 for no horizontal movement).
     * @param dy The vertical direction value (-1 for up, 1 for down, 0 for no vertical movement).
     */
    public void setDirection(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * Breaks a rock ('K') at the player's current position.
     * This method checks if the player's current position contains a rock ('K').
     * If a rock is present, it is removed from the map, and the count of remaining rocks
     * is decremented in the game state for the respective player.
     *
     * @param map       The 2D character array representing the game map.
     * @param state     The current game state containing information about the players and rocks.
     * @param isPlayer1 A boolean indicating whether the action is performed by player 1.
     */
    public void breakRock(char[][] map, GameState state, boolean isPlayer1) {
        if (map[y][x] == 'K') {
            map[y][x] = ' ';
            if (isPlayer1) {
                state.remainingRocks1--;
            } else {
                state.remainingRocks2--;
            }
        }
    }

    /**
     * Checks if the given coordinates are within the boundaries of the map.
     * This method verifies that the specified `x` and `y` coordinates are
     * within the valid range of the provided 2D character array (`map`).
     *
     * @param x   The x-coordinate to check.
     * @param y   The y-coordinate to check.
     * @param map The 2D character array representing the game map.
     * @return True if the coordinates are within the map boundaries, false otherwise.
     */
    private boolean isInsideMap(int x, int y, char[][] map) {
        return y >= 0 && y < map.length && x >= 0 && x < map[0].length;
    }
}