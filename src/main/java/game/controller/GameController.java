package game.controller;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import game.map.MapManager;
import game.model.*;
import game.util.ForbiddenTileScheduler;
import game.view.GameView;

import java.io.*;
import java.util.Set;
import java.util.logging.Logger;

public class GameController {
    public GameView view;
    public GameState state;
    private ForbiddenTileScheduler scheduler;
    Logger log = Logger.getLogger(GameController.class.getName());

    /**
     * Starts a new game. If a saved game state exists, it loads it; otherwise, it creates a new game.
     * Sets up the game scene, initializes player controls, the forbidden tile scheduler, and collision checking.
     *
     * @param stage The main application window where the game scene will be set.
     */
    public void startGame(Stage stage) {
        try {
            log.info("Spouštím novou hru");

            GameState loaded = loadGame();
            if (loaded == null) {
                log.info("Vytvářím novou hru");
                char[][] map1 = MapManager.generateForbiddenTile(MapManager.loadMap("level1.json"));
                char[][] map2 = MapManager.generateForbiddenTile(MapManager.loadMap("level1.json"));
                Player p1 = new Player(5, 5, 3);
                Player p2 = new Player(5, 5, 3);
                state = new GameState(p1, p2, 1, map1, map2);
            } else {
                log.info("Načten uložený stav hry");

                // Tohle přepíše staré serializované mapy novými
                int lvl = loaded.level;
                char[][] map1 = MapManager.generateForbiddenTile(MapManager.loadMap("level" + lvl + ".json"));
                char[][] map2 = MapManager.generateForbiddenTile(MapManager.loadMap("level" + lvl + ".json"));

                loaded.map1 = map1;
                loaded.map2 = map2;
                loaded.remainingRocks1 = loaded.countRocks(map1);
                loaded.remainingRocks2 = loaded.countRocks(map2);

                state = loaded;
            }

            view = new GameView(state);
            stage.setScene(view.getScene());
            stage.show();

            Platform.runLater(() -> view.forceFocus());
            view.getScene().setOnMouseClicked(e -> view.forceFocus());

            new Thread(() -> playerControlLoop(state.player1, true)).start();
            new Thread(() -> playerControlLoop(state.player2, false)).start();


            scheduler = new ForbiddenTileScheduler();
            scheduler.start(this);
            startCollisionChecker();

            System.out.println("startGame dokončeno, vše připraveno.");

        } catch (Exception e) {
            log.severe("Chyba při spouštění hry: " + e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * Starts the collision checker for the game.
     * This method uses an `AnimationTimer` to continuously check for hazard collisions
     * for both players and to verify win conditions. The timer runs on each frame update.
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
     * Retrieves the current game view.
     * This method returns the `GameView` instance associated with the game controller.
     *
     * @return The current `GameView` instance.
     */
    public GameView getView() {
        return view;
    }

    /**
     * Returns to the main menu after the game ends.
     * This method is called when the game is over, either by winning or losing.
     * It creates a new instance of `MenuController` and shows the menu stage with a message.
     *
     * @param won Indicates whether the player won or lost the game.
     */
    private void returnToMenu(boolean won) {
        Platform.runLater(() -> {
            new MenuController().show(new Stage(), won ? "You Won!" : "Game Over");
        });
    }

    /**
     * Checks for hazard collisions for a player.
     * This method checks if the player has collided with a hazard on the map.
     * If a collision occurs, it updates the player's lives and checks for game over conditions.
     *
     * @param player   The player to check for collisions.
     * @param map      The map where the player is located.
     * @param now      The current time in nanoseconds.
     * @param isPlayer1 Indicates whether the player is Player 1 or Player 2.
     */
    public void checkHazardCollision(Player player, char[][] map, long now, boolean isPlayer1) {
        synchronized (player) {
            if (player.y >= 0 && player.y < map.length &&
                    player.x >= 0 && player.x < map[0].length &&
                    map[player.y][player.x] == 'Z') {

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
                        Platform.runLater(() -> ((Stage) view.getScene().getWindow()).close());
                    }
                }
            }
        }
    }

    /**
     * Checks if the game is won.
     * This method checks if both players have no remaining rocks. If so, it updates the level
     * and loads the next level's map. If the game is over, it returns to the main menu.
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
                    new File("save.ser").delete();
                    returnToMenu(true);
                    Platform.runLater(() -> ((Stage) view.getScene().getWindow()).close());
                }
            }
        }
    }

    /**
     * Counts the number of rocks on the map.
     * This method iterates through the map and counts the occurrences of the character 'K',
     * which represents a rock. It returns the total count of rocks found on the map.
     *
     * @param map The 2D character array representing the game map.
     * @return The number of rocks found on the map.
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
     * Refreshes the forbidden tile on both maps.
     * This method generates a new forbidden tile on both maps and updates the game view.
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
     * This method serializes the current game state and writes it to a file named "save.ser".
     *
     * @throws IOException If an error occurs during file writing.
     */
    public void saveGame() throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("save.ser"))) {
            out.writeObject(state);
            log.info("Uložení stavu hry proběhlo úspěšně");
        } catch (IOException e) {
            log.info("Chyba při ukládání stavu hry");
            throw e;
        }
    }

    /**
     * Loads the game state from a file.
     * This method deserializes the game state from a file named "save.ser".
     * If the file does not exist, it returns null.
     *
     * @return The loaded game state, or null if the file does not exist.
     */
    public GameState loadGame() {
        File f = new File("save.ser");
        if (!f.exists()) return null;

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(f))) {
            GameState loaded = (GameState) in.readObject();

            // POZOR: musíme načíst nové mapy podle aktuálního levelu
            loaded.map1 = MapManager.generateForbiddenTile(MapManager.loadMap("level" + loaded.level + ".json"));
            loaded.map2 = MapManager.generateForbiddenTile(MapManager.loadMap("level" + loaded.level + ".json"));
            loaded.remainingRocks1 = loaded.countRocks(loaded.map1);
            loaded.remainingRocks2 = loaded.countRocks(loaded.map2);

            return loaded;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Handles the player's control loop.
     * This method continuously checks for player input and updates the player's position
     * on the map based on the pressed keys. It synchronizes access to the player object
     * to ensure thread safety and updates the game view accordingly.
     *
     * @param player    The player whose controls are being handled.
     * @param isPlayer1 A boolean indicating whether the player is Player 1 or Player 2.
     */
    private void playerControlLoop(Player player, boolean isPlayer1) {
        long delay = 80;

        while (!state.gameOver) {
            Set<KeyCode> keys = isPlayer1 ? view.pressedKeys1 : view.pressedKeys2;
            char[][] map = isPlayer1 ? state.map1 : state.map2;

            synchronized (player) {
                if (!keys.isEmpty()) {
                    if (isPlayer1) {
                        if (keys.contains(KeyCode.W)) player.setDirection(0, -1);
                        else if (keys.contains(KeyCode.S)) player.setDirection(0, 1);
                        else if (keys.contains(KeyCode.A)) player.setDirection(-1, 0);
                        else if (keys.contains(KeyCode.D)) player.setDirection(1, 0);
                        else player.setDirection(0, 0);
                    } else {
                        if (keys.contains(KeyCode.UP)) player.setDirection(0, -1);
                        else if (keys.contains(KeyCode.DOWN)) player.setDirection(0, 1);
                        else if (keys.contains(KeyCode.LEFT)) player.setDirection(-1, 0);
                        else if (keys.contains(KeyCode.RIGHT)) player.setDirection(1, 0);
                        else player.setDirection(0, 0);
                    }

                    player.move(map);
                }
            }

            Platform.runLater(() -> view.updateMap(state.map1, state.map2));

            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }






}
