package game.model;

import java.io.Serializable;

public class Player implements Serializable {
    private static final long serialVersionUID = 1L;

    public int x, y;
    public int lives;

    public Player(int x, int y, int lives) {
        this.x = x;
        this.y = y;
        this.lives = lives;
    }

    public void move(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }
}