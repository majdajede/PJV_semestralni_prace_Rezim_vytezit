package game;

import javafx.application.Application;
import javafx.stage.Stage;
import game.controller.MenuController;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        new MenuController().show(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}