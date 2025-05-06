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

        if (isInsideMap(newX, newY, map)) {
            x = newX;
            y = newY;
        }

        dx = 0;
        dy = 0;
    }


    public void setDirection(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public void breakRock(char[][] map) {
        if (isInsideMap(x, y, map) && map[y][x] == 'K') {
            map[y][x] = ' ';
        }
    }



    private boolean isInsideMap(int x, int y, char[][] map) {
        return y >= 0 && y < map.length && x >= 0 && x < map[0].length;
    }
}
