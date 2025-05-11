package game.view;

import game.model.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.*;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;

public class GameView {
    private final int TILE_SIZE = 50; // VELIKOST BLOKU promenna v px
    private Canvas canvas1;
    private Canvas canvas2;
    private Scene scene;
    private GameState state;
    private Label livesLabel1;
    private Label livesLabel2;
    //nacitani fotek
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

        HBox root = new HBox(left, right);//horizontalni
        root.setSpacing(20);
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

        //pridavame vyraznejsi text na zivoty
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


    private void handleInput(KeyEvent e) {
        switch (e.getCode()) {
            // Player 1
            case W -> {
                state.player1.setDirection(0, -1);
                state.player1.move(state.map1);
            }
            case S -> {
                state.player1.setDirection(0, 1);
                state.player1.move(state.map1);
            }
            case A -> {
                state.player1.setDirection(-1, 0);
                state.player1.move(state.map1);
            }
            case D -> {
                state.player1.setDirection(1, 0);
                state.player1.move(state.map1);
            }
            case B -> {
                state.player1.breakRock(state.map1, state, true);
                state.player1.setDirection(0, 0); // Reset
            }

            // Player 2
            case UP -> {
                state.player2.setDirection(0, -1);
                state.player2.move(state.map2);
            }
            case DOWN -> {
                state.player2.setDirection(0, 1);
                state.player2.move(state.map2);
            }
            case LEFT -> {
                state.player2.setDirection(-1, 0);
                state.player2.move(state.map2);
            }
            case RIGHT -> {
                state.player2.setDirection(1, 0);
                state.player2.move(state.map2);
            }
            case M -> {
                state.player2.breakRock(state.map2, state, false);
                state.player2.setDirection(0, 0); // Reset
            }
        }

        draw();
    }
}
