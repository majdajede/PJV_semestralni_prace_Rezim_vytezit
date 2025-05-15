package game.controller;

import javafx.animation.AnimationTimer;
import javafx.stage.Stage;
import game.map.MapManager;
import game.model.*;
import game.util.ForbiddenTileScheduler;
import game.view.GameView;

import java.io.*;
import java.util.logging.Logger;

public class GameController {
    public GameView view;
    public GameState state;
    private ForbiddenTileScheduler scheduler;
    Logger log = Logger.getLogger(GameController.class.getName());

    /**
     * This method initializes and starts the game.
     * It first attempts to load a saved game state. If no saved state is available,
     * it creates a new game with default settings. Then, it sets up the game view,
     * starts the forbidden tile scheduler, and initializes the collision checker.
     *
     * @param stage The main application window where the game will be displayed.
     */
    public void startGame(Stage stage) {
        try {
            log.info("Spouštím novou hru");
            GameState loaded = loadGame();
            if (loaded == null) {
                log.info("Vytvářím novou hru");
                this.state = loaded;
                state.remainingRocks1 = countRocks(state.map1);
                state.remainingRocks2 = countRocks(state.map2);
            } else {
                log.info("Načten uložený stav hry");
                char[][] map1 = MapManager.generateForbiddenTile(MapManager.loadMap("level1.json"));
                char[][] map2 = MapManager.generateForbiddenTile(MapManager.loadMap("level1.json"));

                Player p1 = new Player(5, 5, 3);
                Player p2 = new Player(5, 5, 3);
                state = new GameState(p1, p2, 1, map1, map2);
            }

            view = new GameView(state);
            scheduler = new ForbiddenTileScheduler();
            scheduler.start(this);

            startCollisionChecker();
            stage.setScene(view.getScene());
        } catch (Exception e) {
            log.info("Chyba při spouštění hry");
        }
    }

    /**
     * Starts the collision checker using an `AnimationTimer`.
     * This method continuously checks for hazard collisions for both players
     * and evaluates win conditions during the game loop.
     */
    private void startCollisionChecker() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                checkHazardCollision(state.player1, state.map1, now, true);
                checkHazardCollision(state.player2, state.map2, now, false);
                checkWin();
            }
        }.start();
    }

    /**
     * Returns the current game view.
     * This method is used to access the game view from other classes.
     *
     * @return The current `GameView` object.
     */
    public GameView getView() { //změna
        return view;
    }

    /**
     * Returns the user to the main menu.
     * This method runs on the JavaFX application thread and displays the menu
     * with a message indicating whether the game was won or lost.
     *
     * @param won A boolean indicating if the game was won (true) or lost (false).
     */
    private void returnToMenu(boolean won) {

        javafx.application.Platform.runLater(() -> {
            new MenuController().show(new Stage(), won ? "You Won!" : "Game Over");
        });
    }

    /**
     * Checks for hazard collisions for the given player on the specified map.
     * If the player steps on a hazard tile ('Z'), their lives are reduced,
     * and the game state is updated accordingly. If the player's lives reach zero,
     * the game ends, and the main menu is displayed.
     *
     * @param player    The player to check for collisions.
     * @param map       The map on which the collision is checked.
     * @param now       The current time in nanoseconds, used for collision timing.
     * @param isPlayer1 A boolean indicating if the player is Player 1 (true) or Player 2 (false).
     */
    public void checkHazardCollision(Player player, char[][] map, long now, boolean isPlayer1) {
        if (map[player.y][player.x] == 'Z') {
            long last = isPlayer1 ? state.lastHitTimeP1 : state.lastHitTimeP2;
            if (now - last > 500_000_000) {
                player.lives--;
                log.info("Hráč " + (isPlayer1 ? "1" : "2") + " ztratil život. Zbývající životy: " + player.lives);
                if (isPlayer1) state.lastHitTimeP1 = now;
                else state.lastHitTimeP2 = now;
                view.updateMap(state.map1, state.map2);

                if (player.lives <= 0 && !state.gameOver) {
                    state.gameOver = true;
                    log.info("Hráč " + (isPlayer1 ? "1" : "2") + " zemřel. Konec hry.");
                    returnToMenu(false);
                    javafx.application.Platform.runLater(() -> {
                        ((Stage) view.getScene().getWindow()).close();
                    });
                }
            }
        }
    }

    /**
     * Checks the win condition for the game.
     * If all rocks are collected by both players, the game progresses to the next level
     * or ends if the final level is completed. In the case of progression, the game state
     * is updated, including loading new maps, resetting player positions, and saving the game.
     * If the game ends, the main menu is displayed with a victory message.
     */
    public void checkWin() {
        if (state.remainingRocks1 <= 0 && state.remainingRocks2 <= 0) {
            if (state.level == 1) {
                state.level = 2;
                try {
                    saveGame();

                    char[][] map1 = MapManager.generateForbiddenTile(MapManager.loadMap("level2.json"));
                    char[][] map2 = MapManager.generateForbiddenTile(MapManager.loadMap("level2.json"));
                    state.map1 = map1;
                    state.map2 = map2;

                    state.player1.x = 5;
                    state.player1.y = 5;
                    state.player2.x = 5;
                    state.player2.y = 5;

                    state.remainingRocks1 = countRocks(map1);
                    state.remainingRocks2 = countRocks(map2);

                    view.updateMap(map1, map2);
                } catch (IOException e) {

                    e.printStackTrace();
                }
            } else {
                if (!state.gameOver) {
                    state.gameOver = true;
                    returnToMenu(true);
                    javafx.application.Platform.runLater(() -> {
                        ((Stage) view.getScene().getWindow()).close();
                    });
                }
            }
        }
    }

/**
 * Counts the number of rocks ('K') in the given map.
 * This method iterates through each cell in the 2D character array
 * and increments the count for every occurrence of the rock character.
 *
 * @param map A 2D character array representing the game map.
 * @return The total number of rocks ('K') found in the map.
 */
    private int countRocks(char[][] map) {
        int count = 0;
        for (char[] row : map) {
            for (char c : row) {
                if (c == 'K') count++;
            }
        }
        return count;
    }

    /**
     * Refreshes the forbidden tiles on both maps.
     * This method updates the forbidden tiles by generating new ones
     * for both player maps and then updates the game view to reflect the changes.
     * If an exception occurs during the process, it is caught and printed to the console.
     */
    public void refreshForbiddenTile() {
        try {
            state.map1 = MapManager.generateForbiddenOnly(state.map1);
            state.map2 = MapManager.generateForbiddenOnly(state.map2);

            view.updateMap(state.map1, state.map2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the current game state to a file.
     * This method serializes the `GameState` object and writes it to a file named "save.ser".
     * If the save operation is successful, a log message is recorded.
     * If an error occurs during the save process, it is logged and rethrown.
     *
     * @throws IOException If an I/O error occurs while saving the game state.
     */
    public void saveGame() throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("save.ser"))) {
            out.writeObject(state);
            log.info("Uložení stavu hry proběhlo úspěšně");
        } catch(IOException e){
            log.info("Chyba při ukládání stavu hry");
            throw e;
        }
    }

    /**
     * Loads the saved game state from a file named "save.ser".
     * This method attempts to deserialize the `GameState` object from the file.
     * If the file does not exist or an error occurs during deserialization,
     * the method returns null.
     *
     * @return The loaded `GameState` object, or null if the file does not exist
     *         or an error occurs during loading.
     */
    public GameState loadGame() {
        File f = new File("save.ser");
        if (!f.exists()) return null;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(f))) {
            return (GameState) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
