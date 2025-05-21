package game.view;

import game.controller.MenuController;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class MenuViewTest {

    @BeforeClass
    public static void initToolkit() {
        // Inicializace JavaFX toolkitu
        Platform.startup(() -> {});
    }

    @Test
    public void menuView_ShowsMessageWhenProvided() {
        MenuController mockController = new MenuController(); // Použití skutečné instance
        MenuView view = new MenuView(mockController, "Test Message");

        Pane root = (Pane) view.getScene().getRoot();

        assertTrue(root.getChildrenUnmodifiable()
                .stream()
                .anyMatch(node -> node instanceof Label && ((Label) node).getText().equals("Test Message")));
    }
}