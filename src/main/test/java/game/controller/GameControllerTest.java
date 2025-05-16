package game.controller;

import game.model.GameState;
import game.model.Player;
import game.view.GameView;
import javafx.application.Platform;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class GameControllerTest {

    @BeforeClass
    public static void initJavaFX() {
        // Spuštění JavaFX vláken – musí být jednou
        Platform.startup(() -> {});
    }

    @Test
    public void checkWin_AdvancesToLevel2() {
        // Arrange
        GameController controller = new GameController();

        GameState state = new GameState(
                new Player(0, 0, 3),
                new Player(0, 0, 3),
                1,
                new char[][]{{' '}},
                new char[][]{{' '}}
        );
        state.remainingRocks1 = 0;
        state.remainingRocks2 = 0;
        controller.state = state;

        // Mock view (jinak NullPointerException při updateMap)
        GameView mockView = mock(GameView.class);
        controller.view = mockView;

        // Act
        controller.checkWin();

        // Assert
        assertEquals(2, controller.state.level);
        verify(mockView).updateMap(any(), any());
    }

    @Test
    public void checkHazardCollision_ReducesLives() {
        // Arrange
        GameController controller = new GameController();

        Player player = new Player(0, 0, 3);
        char[][] map = {{'Z'}};

        GameState state = new GameState(player, new Player(0, 0, 3), 1, map, map);
        state.lastHitTimeP1 = 0;  // aby mohl být zraněn
        controller.state = state;

        GameView mockView = mock(GameView.class);
        controller.view = mockView;

        // Act
        controller.checkHazardCollision(player, map, 1_000_000_000L, true);

        // Assert
        assertEquals(2, player.lives);
        verify(mockView).updateMap(any(), any());
    }

    @Test
    public void loadGame_ReturnsGameState_WhenSaveExists() throws IOException, ClassNotFoundException {
        // Arrange
        GameController controller = new GameController();
        GameState originalState = new GameState(
                new Player(1, 1, 3),
                new Player(1, 1, 3),
                1,
                new char[][]{{' '}},
                new char[][]{{' '}}
        );

        // Uložíme GameState do souboru
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("save.ser"))) {
            out.writeObject(originalState);
        }

        // Act
        GameState loaded = controller.loadGame();

        // Assert
        assertNotNull(loaded);
        assertEquals(1, loaded.level);
    }
}
