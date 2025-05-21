package game.view;

import game.model.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.HashSet;
import java.util.Set;

public class GameView {
    private final int TILE_SIZE = 50;
    private Canvas canvas1;
    private Canvas canvas2;
    private Scene scene;
    private GameState state;
    private Label livesLabel1;
    private Label livesLabel2;

    public final Set<KeyCode> pressedKeys1 = new HashSet<>();
    public final Set<KeyCode> pressedKeys2 = new HashSet<>();

    private final Image backgroundImg = new Image("file:src/main/resources/images/Blok64.png");
    private final Image dangerImg = new Image("file:src/main/resources/images/Nebezpecny_blok64.png");
    private final Image rockImg = new Image("file:src/main/resources/images/Zlato64.png");
    private final Image playerImg = new Image("file:src/main/resources/images/Postavicka64.png");

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
        this.scene = new Scene(root);
        draw();

        // KEY HANDLING
        scene.setOnKeyPressed(e -> {
            KeyCode code = e.getCode();

            switch (code) {
                // Pohyb hráče 1
                case W, A, S, D -> pressedKeys1.add(code);

                // Pohyb hráče 2
                case UP, DOWN, LEFT, RIGHT -> pressedKeys2.add(code);

                // Těžení hráče 1
                case B -> {
                    if (state.map1[state.player1.y][state.player1.x] == 'K') {
                        state.player1.breakRock(state.map1, state, true);
                        updateMap(state.map1, state.map2);
                    }
                }

                // Těžení hráče 2
                case M -> {
                    if (state.map2[state.player2.y][state.player2.x] == 'K') {
                        state.player2.breakRock(state.map2, state, false);
                        updateMap(state.map1, state.map2);
                    }
                }
            }
        });

        scene.setOnKeyReleased(e -> {
            KeyCode code = e.getCode();
            pressedKeys1.remove(code);
            pressedKeys2.remove(code);
        });
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

        String labelStyle = "-fx-font-weight: bold; -fx-font-size: 16px;";
        livesLabel1.setText("Lives: " + state.player1.lives);
        livesLabel1.setStyle(labelStyle);
        livesLabel2.setText("Lives: " + state.player2.lives);
        livesLabel2.setStyle(labelStyle);
    }

    private void drawMap(GraphicsContext gc, char[][] map, Player player) {
        gc.clearRect(0, 0, map[0].length * TILE_SIZE, map.length * TILE_SIZE);

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                char c = map[i][j];
                switch (c) {
                    case 'K' -> gc.drawImage(rockImg, j * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    case 'Z' -> gc.drawImage(dangerImg, j * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    case 'X' -> {
                        gc.setFill(Color.BLACK);
                        gc.fillRect(j * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    }
                    case ' ' -> gc.drawImage(backgroundImg, j * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    default -> {
                        gc.setFill(Color.LIGHTGRAY);
                        gc.fillRect(j * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    }
                }
            }
        }

        gc.drawImage(playerImg, player.x * TILE_SIZE, player.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

    public void forceFocus() {
        scene.getRoot().requestFocus(); // požádáme root uzel o focus
    }
}
