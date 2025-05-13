package game.controller;

import game.model.GameState;
import game.model.Player;
import game.view.GameView;
import javafx.application.Platform;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GameControllerTest {

    @BeforeClass
    public static void initJavaFX() {
        // Inicializace JavaFX prostředí
        Platform.startup(() -> {});
    }

    @Test
    public void checkWin_AdvancesToLevel2() throws IOException {
        // Arrange
        GameController controller = new GameController();
        GameState state = new GameState(new Player(0, 0, 3), new Player(0, 0, 3), 1,
                new char[][]{{' '}}, new char[][]{{' '}});
        state.remainingRocks1 = 0;
        state.remainingRocks2 = 0;
        controller.state = state;

        // Použití jednoduché implementace GameView
        controller.view = new TestGameView();

        // Act
        controller.checkWin();

        // Assert
        assertEquals(2, controller.state.level);
    }

    @Test
    public void checkHazardCollision_ReducesLives() {
        // Arrange
        GameController controller = new GameController();
        Player player = new Player(0, 0, 3);

        char[][] map = {{'Z'}};
        GameState state = new GameState(player, player, 1, map, map);
        controller.state = state;

        // Použití jednoduché implementace GameView
        controller.view = new TestGameView();

        // Act
        controller.checkHazardCollision(player, map, 1_000_000_000L, true);

        // Assert
        assertEquals(2, player.lives);
    }

    @Test
    public void startGame_LoadsSavedGame() throws IOException, ClassNotFoundException {
        // Arrange
        GameController controller = new GameController();
        File file = new File("testSave.dat");
        file.createNewFile();

        GameState state = new GameState(new Player(0, 0, 3), new Player(0, 0, 3), 1,
                new char[][]{{' '}}, new char[][]{{' '}});
        controller.state = state;

        // Act
        GameState result = controller.loadGame();

        // Assert
        assertNotNull(result);
    }

    // Jednoduchá implementace GameView pro testy
    private static class TestGameView extends GameView {
        public TestGameView() {
            super(null);
        }

        @Override
        public void updateMap(char[][] map1, char[][] map2) {
            // Testovací implementace
        }
    }
}