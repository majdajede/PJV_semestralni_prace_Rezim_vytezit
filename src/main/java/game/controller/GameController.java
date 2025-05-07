package game.controller;

import javafx.animation.AnimationTimer;
import javafx.stage.Stage;
import game.map.MapManager;
import game.model.*;
import game.util.ForbiddenTileScheduler;
import game.view.GameView;

public class GameController {
    private GameView view;
    private GameState state;
    private ForbiddenTileScheduler scheduler;

    public void startGame(Stage stage) {
        try {
            //char[][] baseMap = MapManager.loadMap("level1.json");
            //char[][] currentMap = MapManager.generateForbiddenTile(baseMap);
            char[][] map1 = MapManager.generateForbiddenTile(MapManager.loadMap("level1.json"));
            char[][] map2 = MapManager.generateForbiddenTile(MapManager.loadMap("level1.json"));

            Player p1 = new Player(5, 5, 3);
            Player p2 = new Player(5, 5, 3);
            state = new GameState(p1, p2, 1, map1, map2);

            view = new GameView(state);
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
            }
        }.start();
    }

    private void checkHazardCollision(Player player, char[][] map, long now, boolean isPlayer1) {
        if (map[player.y][player.x] == 'Z') {
            long last = isPlayer1 ? state.lastHitTimeP1 : state.lastHitTimeP2;
            if (now - last > 500_000_000) { // 0.5 s v nanosekundách
                player.lives--;
                if (isPlayer1) state.lastHitTimeP1 = now;
                else state.lastHitTimeP2 = now;
                view.updateMap(state.map1, state.map2);
            }
        }
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


}
