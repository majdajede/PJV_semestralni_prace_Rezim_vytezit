package game.controller;

import game.view.MenuView;
import javafx.stage.Stage;

public class MenuController {

    /**
     * Displays the menu view on the given stage.
     * This method sets the scene for the stage using the `MenuView` class
     * and displays it. No message is shown in the menu view.
     *
     * @param stage The JavaFX stage where the menu view will be displayed.
     */
    public void show(Stage stage) {
        show(stage, null);
    }

    /**
     * Displays the menu view on the given stage with a message.
     * This method sets the scene for the stage using the `MenuView` class
     * and displays it. A message can be shown in the menu view.
     *
     * @param stage   The JavaFX stage where the menu view will be displayed.
     * @param message The message to be displayed in the menu view.
     */
    public void show(Stage stage, String message) {

        MenuView view = new MenuView(this, message);
        stage.setScene(view.getScene());
        stage.show();
    }

    /**
     * Handles the action when the start button is clicked.
     * This method creates a new instance of `GameController` and starts the game.
     *
     * @param stage The JavaFX stage where the game will be displayed.
     */
    public void onStartButtonClicked(Stage stage) {

        new GameController().startGame(stage);
    }
}
