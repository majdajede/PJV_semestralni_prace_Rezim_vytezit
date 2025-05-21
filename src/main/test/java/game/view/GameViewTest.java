package game.view;

import game.model.GameState;
import game.model.Player;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import javafx.application.Platform;

public class GameViewTest {

    @BeforeClass
    public static void initJavaFX() {
        Platform.startup(() -> {});
    }

    @Test
    public void handleInput_MovesPlayer() {
        Player mockPlayer1 = mock(Player.class);
        Player mockPlayer2 = mock(Player.class); // i když ho nepoužijeme, potřebujeme ho

        GameState state = new GameState(
                mockPlayer1,
                mockPlayer2,
                1,
                new char[][]{{' '}},
                new char[][]{{' '}}
        );

        GameView view = new GameView(state);

        KeyEvent mockEvent = mock(KeyEvent.class);
        when(mockEvent.getCode()).thenReturn(KeyCode.W);

        view.handleInput(mockEvent);

        verify(mockPlayer1).setDirection(0, -1);
        verify(mockPlayer1).move(any());
    }
}
