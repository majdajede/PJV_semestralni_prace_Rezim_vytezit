package game.util;

import java.util.concurrent.*;
import game.map.MapManager;
import game.controller.GameController;

public class ForbiddenTileScheduler {
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public void start(GameController controller) {
        scheduler.scheduleAtFixedRate(() -> controller.refreshForbiddenTile(), 5, 5, TimeUnit.SECONDS);
    }

    public void stop() {
        scheduler.shutdown();
    }
}