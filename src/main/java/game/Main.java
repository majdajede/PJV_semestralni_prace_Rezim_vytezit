package game;

import game.controller.GameController;
import javafx.application.Application;
import javafx.stage.Stage;
import game.controller.MenuController;

import java.util.logging.Logger;

public class Main extends Application {
    Logger log = Logger.getLogger(GameController.class.getName());
    @Override
    public void start(Stage primaryStage) {
        log.info("Aplikace spuštěna");
        new MenuController().show(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}