package game.view;

import game.model.*;
import javafx.scene.Scene;
import javafx.scene.canvas.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;


public class GameView {
    private final int TILE_SIZE = 20;
    private Canvas canvas1;
    private Canvas canvas2;
    private Scene scene;
    private GameState state;

    public GameView(GameState state) {
        this.state = state;

        int rows = state.map1.length;
        int cols = state.map1[0].length;

        canvas1 = new Canvas(cols * TILE_SIZE, rows * TILE_SIZE);
        canvas2 = new Canvas(cols * TILE_SIZE, rows * TILE_SIZE);

        HBox root = new HBox(canvas1, canvas2);
        this.scene = new Scene(root);
        draw();

        scene.setOnKeyPressed(this::handleInput);
    }

    public Scene getScene() {
        return scene;
    }

    public void updateMap(char[][] map1, char[][] map2) {
        state.map1 = map1;
        state.map2 = map2;
        draw();
    }


    private void draw() {
        drawMap(canvas1.getGraphicsContext2D(), state.map1, state.player1);
        drawMap(canvas2.getGraphicsContext2D(), state.map2, state.player2);
    }

    private void drawMap(GraphicsContext gc, char[][] map, Player player) {
        gc.clearRect(0, 0, 250, 250);

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                char c = map[i][j];
                switch (c) {
                    case 'K' -> gc.setFill(Color.TURQUOISE);
                    case 'Z' -> gc.setFill(Color.RED);
                    case 'X' -> gc.setFill(Color.BLACK);
                    case ' ' -> gc.setFill(Color.WHITE);
                    default -> gc.setFill(Color.LIGHTGRAY);
                }
                gc.fillRect(j * 20, i * 20, 20, 20);
                gc.setStroke(Color.BLACK);
                gc.strokeRect(j * 20, i * 20, 20, 20); // mřížka
            }
        }

        gc.setFill(player == state.player1 ? Color.HOTPINK : Color.DEEPPINK);
        gc.fillOval(player.x * 20 + 2, player.y * 20 + 2, 16, 16); // hráč uprostřed políčka
    }

    private void handleInput(KeyEvent e) {
        switch (e.getCode()) {
            case W -> state.player1.setDirection(0, -1);
            case S -> state.player1.setDirection(0, 1);
            case A -> state.player1.setDirection(-1, 0);
            case D -> state.player1.setDirection(1, 0);
            case B -> state.player1.breakRock(state.map1);

            case UP -> state.player2.setDirection(0, -1);
            case DOWN -> state.player2.setDirection(0, 1);
            case LEFT -> state.player2.setDirection(-1, 0);
            case RIGHT -> state.player2.setDirection(1, 0);
            case M -> state.player2.breakRock(state.map2);
        }

        state.player1.move(state.map1);
        state.player2.move(state.map2);
        draw();

    }


}