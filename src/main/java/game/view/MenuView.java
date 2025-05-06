package game.view;

import game.controller.MenuController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class MenuView {
    private final Scene scene;

    public MenuView(MenuController controller) {
        Button startButton = new Button("Start Game");
        startButton.setOnAction(e -> controller.onStartButtonClicked((Stage) startButton.getScene().getWindow()));

        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().add(startButton);

        this.scene = new Scene(layout, 300, 200);
    }

    public Scene getScene() {
        return scene;
    }
}