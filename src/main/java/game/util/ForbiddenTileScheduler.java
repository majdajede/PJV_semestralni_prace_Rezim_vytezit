package game.util;

import game.controller.GameController;

import java.util.concurrent.*;
import java.util.logging.Logger;


public class ForbiddenTileScheduler {
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    Logger log = Logger.getLogger(ForbiddenTileScheduler.class.getName());

    /**
     * Starts the scheduler to periodically refresh forbidden tiles.
     * This method schedules a task to run at a fixed rate of 5 seconds.
     * The task logs a message and invokes the `refreshForbiddenTile` method
     * on the provided `GameController` instance.
     *
     * @param controller The `GameController` instance responsible for managing the game state.
     */
    public void start(GameController controller) {
        scheduler.scheduleAtFixedRate(() -> {
            log.info("Aktualizace nebezpečných políček");
            controller.refreshForbiddenTile();
        }, 5, 5, TimeUnit.SECONDS);
    }
    }