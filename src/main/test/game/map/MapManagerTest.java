package game.map;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MapManagerTest {

    @Test
    public void generateForbiddenTile_AddsOneZTile() {
        // Arrange
        char[][] map = {{' ', ' '}, {' ', ' '}};

        // Act
        char[][] result = MapManager.generateForbiddenTile(map);

        // Assert
        int zCount = 0;
        for (char[] row : result) {
            for (char c : row) {
                if (c == 'Z') zCount++;
            }
        }
        assertEquals(1, zCount);
    }
}
