package game.controller;

import javafx.animation.AnimationTimer;
import javafx.stage.Stage;
import game.map.MapManager;
import game.model.*;
import game.util.ForbiddenTileScheduler;
import game.view.GameView;

import java.io.*;

public class GameController {
    private GameView view;
    private GameState state;
    private ForbiddenTileScheduler scheduler;

    public void startGame(Stage stage) {
        try {
            GameState loaded = loadGame();
            if (loaded != null) {
                this.state = loaded;
                state.remainingRocks1 = countRocks(state.map1);
                state.remainingRocks2 = countRocks(state.map2);
            } else {
                char[][] map1 = MapManager.generateForbiddenTile(MapManager.loadMap("level1.json"));
                char[][] map2 = MapManager.generateForbiddenTile(MapManager.loadMap("level1.json"));

                Player p1 = new Player(5, 5, 3);
                Player p2 = new Player(5, 5, 3);
                state = new GameState(p1, p2, 1, map1, map2);
            }

            view = new GameView(state, this);
            scheduler = new ForbiddenTileScheduler();
            scheduler.start(this);

            startCollisionChecker();
            stage.setScene(view.getScene());
        } catch (Exception e) {
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
    private void returnToMenu() {
        javafx.application.Platform.runLater(() -> {
            new MenuController().show(new Stage());
        });
    }

    private void checkHazardCollision(Player player, char[][] map, long now, boolean isPlayer1) {
        if (map[player.y][player.x] == 'Z') {
            long last = isPlayer1 ? state.lastHitTimeP1 : state.lastHitTimeP2;
            if (now - last > 500_000_000) {
                player.lives--;
                if (isPlayer1) state.lastHitTimeP1 = now;
                else state.lastHitTimeP2 = now;
                view.updateMap(state.map1, state.map2);

                if (player.lives <= 0 && !state.gameOver) {
                    state.gameOver = true;
                    System.out.println("Player " + (isPlayer1 ? "1" : "2") + " died. Returning to menu...");
                    returnToMenu();
                    javafx.application.Platform.runLater(() -> {
                        ((Stage) view.getScene().getWindow()).close();
                    });
                }
            }
        }
    }

    private void checkWin() {
        if (state.remainingRocks1 <= 0 && state.remainingRocks2 <= 0) {
            if (state.level == 1) {
                state.level = 2;
                try {
                    saveGame();
                    char[][] map1 = MapManager.generateForbiddenTile(MapManager.loadMap("level2.json"));
                    char[][] map2 = MapManager.generateForbiddenTile(MapManager.loadMap("level2.json"));
                    Player p1 = new Player(5, 5, 3);
                    Player p2 = new Player(5, 5, 3);
                    state.map1 = map1;
                    state.map2 = map2;
                    state.player1 = p1;
                    state.player2 = p2;
                    state.remainingRocks1 = countRocks(map1);
                    state.remainingRocks2 = countRocks(map2);

                    //musime znova vycentrovat hrace
                    state.player1.x = 5;
                    state.player1.y = 5;
                    state.player2.x = 5;
                    state.player2.y = 5;

                    view.updateMap(map1, map2);
                } catch (IOException e) {
                    e.printStackTrace();
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
}
