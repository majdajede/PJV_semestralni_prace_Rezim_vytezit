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

    public void move(char[][] map) {
        int newX = x + dx;
        int newY = y + dy;
        if (isInsideMap(newX, newY, map) && map[newY][newX] != 'K') {
            x = newX;
            y = newY;
        }
    }

    public void setDirection(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public void breakRock(char[][] map) {
        int targetX = x + dx;
        int targetY = y + dy;
        if (isInsideMap(targetX, targetY, map) && map[targetY][targetX] == 'K') {
            map[targetY][targetX] = ' ';
        }
    }

    private boolean isInsideMap(int x, int y, char[][] map) {
        return y >= 0 && y < map.length && x >= 0 && x < map[0].length;
    }
}
