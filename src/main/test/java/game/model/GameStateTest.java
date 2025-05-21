package game.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GameStateTest {

    @Test
    public void countRocks_ReturnsCorrectCount() {
        char[][] map = {{'K', ' '}, {'X', 'K'}};

        int count = new GameState(null, null, 1, map, map).countRocks(map);

        assertEquals(2, count);
    }
}
