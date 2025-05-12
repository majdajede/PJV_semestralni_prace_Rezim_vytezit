package game.view;

import game.controller.MenuController;
import javafx.scene.control.Label;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class MenuView {
    @Test
    public void menuView_ShowsMessageWhenProvided() {
        // Arrange
        MenuController mockController = mock(MenuController.class);

        // Act
        MenuView view = new MenuView(mockController, "Test Message");

        // Assert
        assertTrue(view.getScene().getRoot().getChildrenUnmodifiable()//proč to nevidí getScene?
                .stream().anyMatch(node -> node instanceof Label && ((Label)node).getText().equals("Test Message")));
    }
}
