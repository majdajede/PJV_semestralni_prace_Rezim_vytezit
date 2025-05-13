package game;

import game.controller.GameController;
import javafx.application.Application;
import javafx.stage.Stage;
import game.controller.MenuController;

import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Main extends Application {
    private static boolean logsEnabled = true;
    Logger log = Logger.getLogger(GameController.class.getName());
    @Override
    public void start(Stage primaryStage) {

        if (!logsEnabled) {
            disableLogging();
        }
        log.info("Aplikace spuštěna");
        new MenuController().show(primaryStage);
    }

    public static void main(String[] args) {
        for (String arg : args) {
            if (arg.equalsIgnoreCase("--nologs")) {
                logsEnabled = false;
                break;
            }
        }
        launch(args);
    }


    private static void disableLogging() {
        LogManager.getLogManager().reset();
    }
}