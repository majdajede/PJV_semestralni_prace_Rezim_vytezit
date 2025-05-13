package game.view;

import game.model.GameState;
import game.model.Player;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.junit.Test;

import static javafx.beans.binding.Bindings.when;

public class GameViewTest {

    @Test
    public void handleInput_MovesPlayer() {
        // Arrange
        GameState mockState = mock(GameState.class);
        Player mockPlayer = mock(Player.class);
        when(mockState.player1).thenReturn(mockPlayer);

        GameView view = new GameView(mockState);
        KeyEvent mockEvent = mock(KeyEvent.class);
        when(mockEvent.getCode()).thenReturn(KeyCode.W);

        // Act
        view.handleInput(mockEvent);

        // Assert
        verify(mockPlayer).setDirection(0, -1);
        verify(mockPlayer).move(any());
    }
}
