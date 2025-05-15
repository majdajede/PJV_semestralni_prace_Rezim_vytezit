package game.view;

import game.controller.MenuController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MenuView {
    private final Scene scene;

    /**
     * Constructs a new `MenuView` instance for the game menu.
     * This constructor initializes the menu layout, including the title,
     * an optional status message, and a start button. The layout is styled
     * with custom fonts, colors, and alignment to enhance the user interface.
     *
     * @param controller The `MenuController` responsible for handling menu actions.
     * @param message    An optional status message to display below the title.
     *                   If null or empty, no message is shown.
     */
    public MenuView(MenuController controller, String message) {
        Label title = new Label("Režim Vytěžit");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label statusLabel = new Label(message == null ? "" : message);
        statusLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: red;");

        Button startButton = new Button("Start Game");
        startButton.setOnAction(e -> controller.onStartButtonClicked((Stage) startButton.getScene().getWindow()));

        //chci aby tlačítko také mělo design
        startButton.setStyle(
                "-fx-background-color: #540b0e; " +
                        "-fx-text-fill: white; "
        );

        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(title);
        if (message != null && !message.isEmpty()) {
            layout.getChildren().add(statusLabel);
        }
        layout.getChildren().add(startButton);

        layout.setBackground(new Background(new BackgroundFill(Color.BISQUE, null, null))); // světle hnědé pozadí

        this.scene = new Scene(layout, 400, 300); // zvětšeno pro lepší zobrazení
    }

    /**
     * Returns the current scene of the menu.
     * This method provides access to the `Scene` object, which represents
     * the graphical user interface of the menu, including its layout and components.
     *
     * @return The `Scene` object representing the menu's user interface.
     */
    public Scene getScene() {
        return scene;
    }
}