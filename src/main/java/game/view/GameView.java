package game.view;

import game.model.*;
import javafx.scene.Scene;
import javafx.scene.canvas.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;


public class GameView {
    private Scene scene;
    private Canvas canvas1 = new Canvas(250, 250);
    private Canvas canvas2 = new Canvas(250, 250);
    private GameState state;

    public GameView(GameState state) {
        this.state = state;
        HBox root = new HBox(canvas1, canvas2);
        this.scene = new Scene(root);
        draw();
        scene.setOnKeyPressed(this::handleInput);
    }

    public Scene getScene() {
        return scene;
    }

    public void updateMap(char[][] map) {
        state.currentMap = map;
        draw();
    }

    private void draw() {
        drawMap(canvas1.getGraphicsContext2D(), state.player1);
        drawMap(canvas2.getGraphicsContext2D(), state.player2);
    }

    private void drawMap(GraphicsContext gc, Player player) {
        gc.clearRect(0, 0, 250, 250);
        for (int i = 0; i < state.currentMap.length; i++) {
            for (int j = 0; j < state.currentMap[i].length; j++) {
                char c = state.currentMap[i][j];
                if (c == 'K') gc.setFill(Color.GRAY);
                else if (c == 'Z') gc.setFill(Color.RED);
                else gc.setFill(Color.WHITE);
                gc.fillRect(j * 20, i * 20, 18, 18);
            }
        }
        gc.setFill(Color.BLUE);
        gc.fillOval(player.x * 20, player.y * 20, 18, 18);
    }

    private void handleInput(KeyEvent e) {
        switch (e.getCode()) {
            case W -> state.player1.setDirection(0, -1);
            case S -> state.player1.setDirection(0, 1);
            case A -> state.player1.setDirection(-1, 0);
            case D -> state.player1.setDirection(1, 0);
            case B -> state.player1.breakRock(state.currentMap);

            case UP -> state.player2.setDirection(0, -1);
            case DOWN -> state.player2.setDirection(0, 1);
            case LEFT -> state.player2.setDirection(-1, 0);
            case RIGHT -> state.player2.setDirection(1, 0);
            case M -> state.player2.breakRock(state.currentMap);
        }

        state.player1.move(state.currentMap);
        state.player2.move(state.currentMap);
        draw();
    }

}