package game.view;

import game.model.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.*;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class GameView {
    private final int TILE_SIZE = 64; // VELIKOST BLOKU promenna v px
    private Canvas canvas1;
    private Canvas canvas2;
    private Scene scene;
    private GameState state;
    private Label livesLabel1;
    private Label livesLabel2;

    public GameView(GameState state) {
        this.state = state;

        int rows = state.map1.length;
        int cols = state.map1[0].length;

        canvas1 = new Canvas(cols * TILE_SIZE, rows * TILE_SIZE);
        canvas2 = new Canvas(cols * TILE_SIZE, rows * TILE_SIZE);

        livesLabel1 = new Label("Lives: " + state.player1.lives);
        livesLabel2 = new Label("Lives: " + state.player2.lives);

        VBox left = new VBox(livesLabel1, canvas1);
        VBox right = new VBox(livesLabel2, canvas2);
        left.setAlignment(Pos.CENTER);
        right.setAlignment(Pos.CENTER);

        HBox root = new HBox(left, right);
        root.setSpacing(20);
        //HBox root = new HBox(canvas1, canvas2);
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
        livesLabel1.setText("Lives: " + state.player1.lives);
        livesLabel2.setText("Lives: " + state.player2.lives);
    }

    private void drawMap(GraphicsContext gc, char[][] map, Player player) {
        gc.clearRect(0, 0, map[0].length * TILE_SIZE, map.length * TILE_SIZE);

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

                gc.fillRect(j * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                gc.setStroke(Color.BLACK);
                gc.strokeRect(j * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }

        gc.setFill(player == state.player1 ? Color.HOTPINK : Color.DEEPPINK); //barvicky
        gc.fillOval(player.x * TILE_SIZE, player.y * TILE_SIZE, TILE_SIZE, TILE_SIZE); // TU JSOU hraci
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
