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
                state = loaded;
            }

            // Vytvoření view
            view = new GameView(state);

            stage.setScene(view.getScene());
            stage.show();

            Platform.runLater(() -> view.forceFocus());
            view.getScene().setOnMouseClicked(e -> view.forceFocus());



            // Spuštění ovládacích vláken
            new Thread(() -> playerControlLoop(state.player1, state.map1, true)).start();
            new Thread(() -> playerControlLoop(state.player2, state.map2, false)).start();

            // Zakázané dlaždice + kolize
            scheduler = new ForbiddenTileScheduler();
            scheduler.start(this);
            startCollisionChecker();

            System.out.println("startGame dokončeno, vše připraveno.");

        } catch (Exception e) {
            log.severe("Chyba při spouštění hry: " + e.getMessage());
            e.printStackTrace();
        }
    }


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

    public GameView getView() {
        return view;
    }

    private void returnToMenu(boolean won) {
        Platform.runLater(() -> {
            new MenuController().show(new Stage(), won ? "You Won!" : "Game Over");
        });
    }

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
                    Platform.runLater(() -> ((Stage) view.getScene().getWindow()).close());
                }
            }
        }
    }

    private int countRocks(char[][] map) {
        int count = 0;
        for (char[] row : map) {
            for (char c : row) {
                if (c == 'K') count++;
            }
        }
        return count;
    }

    public void refreshForbiddenTile() {
        try {
            state.map1 = MapManager.generateForbiddenOnly(state.map1);
            state.map2 = MapManager.generateForbiddenOnly(state.map2);
            view.updateMap(state.map1, state.map2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveGame() throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("save.ser"))) {
            out.writeObject(state);
            log.info("Uložení stavu hry proběhlo úspěšně");
        } catch (IOException e) {
            log.info("Chyba při ukládání stavu hry");
            throw e;
        }
    }

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

    private void playerControlLoop(Player player, char[][] map, boolean isPlayer1) {
        long delay = 80;
        long lastBreakTime = 0; // ← každé vlákno má vlastní

        while (!state.gameOver) {
            Set<KeyCode> keys = isPlayer1 ? view.pressedKeys1 : view.pressedKeys2;

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

                long now = System.currentTimeMillis();

                if (isPlayer1 && keys.contains(KeyCode.B) && now - lastBreakTime > 200) {
                    player.breakRock(map, state, true);
                    lastBreakTime = now;
                }

                if (!isPlayer1 && keys.contains(KeyCode.M) && now - lastBreakTime > 200) {
                    player.breakRock(map, state, false);
                    lastBreakTime = now;
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
