package game.util;

import game.controller.GameController;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class ForbiddenTileSchedulerTest {

    @Test
    public void start_SchedulesTask() {
        // Arrange
        ForbiddenTileScheduler scheduler = new ForbiddenTileScheduler();
        GameController mockController = mock(GameController.class);

        // Act
        scheduler.start(mockController);

        // Assert
        verify(mockController, timeout(6000)).refreshForbiddenTile();
    }
}
