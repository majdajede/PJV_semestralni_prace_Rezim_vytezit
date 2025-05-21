package game.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PlayerTest {

    @Test
    public void move_UpdatesPositionWhenValid() {

        Player player = new Player(1, 1, 3);
        char[][] map = {{' ', ' ', ' '}, {' ', ' ', ' '}, {' ', ' ', ' '}};
        player.setDirection(1, 0);

        player.move(map);

        assertEquals(2, player.x);
        assertEquals(1, player.y);
    }

    @Test
    public void breakRock_DecrementsCounter() {

        Player player = new Player(0, 0, 3);
        char[][] map = {{'K'}};
        GameState mockState = mock(GameState.class);

        player.breakRock(map, mockState, true);

        verify(mockState).remainingRocks1--;
    }
}
