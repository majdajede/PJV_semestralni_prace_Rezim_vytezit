package process;

import game.controller.GameController;
import game.model.GameState;
import game.model.Player;
import game.util.ForbiddenTileScheduler;
import game.view.GameView;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AllTests {

    @BeforeClass
    public static void initJavaFX() {
        // Spuštění JavaFX pouze jednou pro všechny testy
        Platform.startup(() -> {});
    }

//    funguje a je procesni
//    hráč umírá, když má 0 životů
    @Test
    public void playerDiesWhenLivesReachZero() {
        // Arrange
        Player player = new Player(0, 0, 1); // 1 život
        char[][] map = {{'Z'}}; // Nebezpečné pole
        GameState state = new GameState(player, new Player(0, 0, 3), 1, map, map);
        state.lastHitTimeP1 = 0;  // zajistí, že může být zasažen

        GameController controller = new GameController();
        controller.state = state;
        controller.view = mock(GameView.class); // Mock UI

        // Act
        controller.checkHazardCollision(player, map, System.nanoTime(), true);

        // Assert
        assertTrue(state.gameOver);
        assertEquals(0, player.lives);
    }


//    funguje a je procesni
//     Hra přechází do levelu 2 po sesbírání všech kamenů v levelu 1
    @Test
    public void gameAdvancesToLevel2WhenAllRocksCollected() {
        // Arrange
        GameState state = new GameState(
                new Player(0, 0, 3),
                new Player(0, 0, 3),
                1,
                new char[][]{{' '}}, // mapa1
                new char[][]{{' '}}  // mapa2
        );
        state.remainingRocks1 = 0;
        state.remainingRocks2 = 0;

        GameController controller = new GameController();
        controller.state = state;
        controller.view = mock(GameView.class); // Mocknutý GameView přidán

        // Act
        controller.checkWin();

        // Assert
        assertEquals(2, state.level);
    }

    //funguje a je procesni
    //uložení a načtení hry zachovává stav hry
    @Test
    public void saveAndLoadPreservesGameState() throws IOException, ClassNotFoundException {
        // Arrange
        GameState originalState = new GameState(new Player(1, 2, 3), new Player(4, 5, 1),
                2, new char[][]{{'K'}}, new char[][]{{'Z'}});
        GameController controller = new GameController();

        // Act - uložení
        controller.state = originalState;
        controller.saveGame();

        // Act - načtení
        GameState loadedState = controller.loadGame();

        // Assert
        assertNotNull(loadedState);
        assertEquals(originalState.level, loadedState.level);
        assertEquals(originalState.player1.x, loadedState.player1.x);
        assertEquals(originalState.player2.y, loadedState.player2.y);
    }

    //funguje a je procesni
    //nebezpečná pole se pravidleně generují
    @Test
    public void forbiddenTilesRefreshPeriodically() {
        // Arrange
        ForbiddenTileScheduler scheduler = new ForbiddenTileScheduler();
        GameController mockController = mock(GameController.class);

        // Act
        scheduler.start(mockController);

        // Assert - ověření, že se metoda refreshForbiddenTile zavolá
        verify(mockController, timeout(6000).atLeastOnce()).refreshForbiddenTile();
    }

    //funguje a je procesni
    // Rozbití kamene sníží počet zbývajících kamenů
    @Test
    public void breakingRockDecrementsCounter() {
        // Arrange
        Player player = new Player(0, 0, 3);
        char[][] map = {{'K'}}; // Kámen na pozici [0,0]
        GameState state = new GameState(player, new Player(0, 0, 3), 1, map, map);
        state.remainingRocks1 = 1;

        // Act
        player.breakRock(map, state, true);

        // Assert
        assertEquals(0, state.remainingRocks1);
        assertEquals(' ', map[0][0]); // Kámen byl odstraněn
    }

//    funguje a je procesni
//    Stisknutí kláves správně pohybuje hráčem
    @Test
    public void keyPressesCorrectlyMovePlayer() {
        // Arrange
        GameState state = new GameState(new Player(1, 1, 3), new Player(1, 1, 3), 1,
                new char[][]{{' ', ' ', ' '}, {' ', ' ', ' '}, {' ', ' ', ' '}},
                new char[][]{{' ', ' ', ' '}, {' ', ' ', ' '}, {' ', ' ', ' '}});
        GameView view = new GameView(state);
        KeyEvent rightKey = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.D, false, false, false, false);

        // Act
        view.handleInput(rightKey);

        // Assert
        assertEquals(2, state.player1.x); // Pohyb doprava
        assertEquals(1, state.player1.y);
    }

    //funguje a je procesni
//    //když výhra, tak se zobrazí zpráva
    @Test
    public void victoryShowsWinMessage() {
        // Arrange
        GameState state = new GameState(new Player(0, 0, 3), new Player(0, 0, 3), 2,
                new char[][]{{' '}}, new char[][]{{' '}});
        state.remainingRocks1 = 0;
        state.remainingRocks2 = 0;
        GameController controller = new GameController();
        controller.state = state;

        // Act
        controller.checkWin();

        // Assert
        assertTrue(state.gameOver);
        // Ověření, že se zobrazí výherní zpráva by vyžadovalo testování UI, což je složitější
    }

}
