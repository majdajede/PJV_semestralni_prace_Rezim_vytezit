package game.util;

import game.controller.GameController;

import java.util.concurrent.*;
import java.util.logging.Logger;


public class ForbiddenTileScheduler {
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    Logger log = Logger.getLogger(ForbiddenTileScheduler.class.getName());

    public void start(GameController controller) {
        scheduler.scheduleAtFixedRate(() -> {
            log.info("Aktualizace nebezpečných políček");
            controller.refreshForbiddenTile();
        }, 5, 5, TimeUnit.SECONDS);
    }
    }