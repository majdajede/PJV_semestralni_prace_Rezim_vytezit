package game.controller;

import game.model.GameState;
import game.model.Player;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;

import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import static org.mockito.Mockito.*;

public class GameControllerTest {

    @Test
    public void checkWin_AdvancesToLevel2() throws IOException {
        // Arrange
        GameController controller = new GameController();
        GameState state = new GameState(new Player(0, 0, 3), new Player(0, 0, 3), 1,
                new char[][]{{' '}}, new char[][]{{' '}});
        state.remainingRocks1 = 0;
        state.remainingRocks2 = 0;
        controller.state = state;

        // Act
        controller.checkWin();

        // Assert
        assertEquals(2, controller.state.level);
    }

    @Test
    public void checkHazardCollision_ReducesLives() {
        // Arrange
        GameController controller = new GameController();
        Player mockPlayer = mock(Player.class);
        mockPlayer.lives = 3;

        char[][] map = {{'Z'}};
        GameState mockState = mock(GameState.class);
        controller.state = mockState;

        // Act
        controller.checkHazardCollision(mockPlayer, map, 1_000_000_000L, true);

        // Assert
        verify(mockPlayer).lives--;
    }

    @Test
    public void startGame_LoadsSavedGame() throws IOException, ClassNotFoundException {
        // Arrange
        GameController controller = new GameController();
        File mockFile = mock(File.class);
        when(mockFile.exists()).thenReturn(true);

        GameState mockState = mock(GameState.class);
        ObjectInputStream mockOIS = mock(ObjectInputStream.class);
        when(mockOIS.readObject()).thenReturn(mockState);

        // Act
        GameState result = controller.loadGame();

        // Assert
        assertNotNull(result);
        verify(mockOIS).readObject();
    }
}
