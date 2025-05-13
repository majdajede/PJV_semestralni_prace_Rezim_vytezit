package game.view;

import game.controller.MenuController;
import javafx.scene.control.Label;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class MenuViewTest {
    @Test
    public void menuView_ShowsMessageWhenProvided() {
        // Arrange
        MenuController mockController = mock(MenuController.class);

        // Act
        MenuViewTest view = new MenuViewTest(mockController, "Test Message");

        // Assert
        assertTrue(view.getScene().getRoot().getChildrenUnmodifiable()//proč to nevidí getScene?
                .stream().anyMatch(node -> node instanceof Label && ((Label)node).getText().equals("Test Message")));
    }
}
