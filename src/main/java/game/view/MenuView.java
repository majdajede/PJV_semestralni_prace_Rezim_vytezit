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

    public Scene getScene() {
        return scene;
    }
}