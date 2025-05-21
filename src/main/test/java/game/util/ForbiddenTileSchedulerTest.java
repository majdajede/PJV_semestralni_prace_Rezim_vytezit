package game.util;

import game.controller.GameController;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class ForbiddenTileSchedulerTest {

    @Test //opravit
    public void start_SchedulesTask() {

        ForbiddenTileScheduler scheduler = new ForbiddenTileScheduler();
        GameController mockController = mock(GameController.class);

        scheduler.start(mockController);

        verify(mockController, timeout(6000)).refreshForbiddenTile();
    }
}
