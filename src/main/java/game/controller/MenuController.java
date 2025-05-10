package game.controller;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MenuController {
    public void show(Stage stage) {
        show(stage, null); // Původní metoda volá novou s null zprávou
    }

    public void show(Stage stage, String message) {
        Button startButton = new Button("Start Game");
        startButton.setOnAction(e -> new GameController().startGame(stage));

        VBox root = new VBox(20);
        if (message != null) {
            Label label = new Label(message);
            root.getChildren().add(label);
        }
        root.getChildren().add(startButton);

        Scene scene = new Scene(root, 300, 200);
        stage.setScene(scene);
        stage.show();
    }

    public void onStartButtonClicked(Stage stage) {
        new GameController().startGame(stage);
    }
}
