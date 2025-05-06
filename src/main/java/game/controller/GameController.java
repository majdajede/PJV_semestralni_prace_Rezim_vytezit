package game.controller;

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
            char[][] baseMap = MapManager.loadMap("level1.json");
            char[][] currentMap = MapManager.generateForbiddenTile(baseMap);
            char[][] map1 = MapManager.generateForbiddenTile(MapManager.loadMap("level1.json"));
            char[][] map2 = MapManager.generateForbiddenTile(MapManager.loadMap("level1.json"));

            Player p1 = new Player(1, 1, 3);
            Player p2 = new Player(3, 3, 3);

            state = new GameState(p1, p2, 1, map1, map2);

            view = new GameView(state);
            scheduler = new ForbiddenTileScheduler();
            scheduler.start(this);

            stage.setScene(view.getScene());
        } catch (Exception e) {
            e.printStackTrace();
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
