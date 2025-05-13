package game.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GameStateTest {

    @Test
    public void countRocks_ReturnsCorrectCount() {
        // Arrange
        char[][] map = {{'K', ' '}, {'X', 'K'}};

        // Act
        int count = new GameState(null, null, 1, map, map).countRocks(map);

        // Assert
        assertEquals(2, count);
    }
}
