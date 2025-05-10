package game.controller;

import game.view.MenuView;
import javafx.stage.Stage;

public class MenuController {
    public void show(Stage stage) {
        show(stage, null);
    }

    public void show(Stage stage, String message) {
        MenuView view = new MenuView(this, message);
        stage.setScene(view.getScene());
        stage.show();
    }

    public void onStartButtonClicked(Stage stage) {
        new GameController().startGame(stage);
    }
}
