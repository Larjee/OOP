package ru.nsu.munkuev.snake.view;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import ru.nsu.munkuev.snake.controller.GameController;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * JavaFX-адаптер между FXML и обычным игровым контроллером
 *
 * <p>Все JavaFX-зависимости находятся здесь, в пакете {@code view}.
 * Игровой {@link GameController} не знает про Canvas, Label, KeyEvent и AnimationTimer
 */
public class JavaFxGameController implements Initializable {

    @FXML private StackPane boardPane;
    @FXML private Canvas canvas;
    @FXML private Label scoreLabel;
    @FXML private Label targetLabel;

    private GameController controller;
    private GameView view;
    private AnimationTimer loop;
    private long lastTickNs;

    @Override
    public void initialize(URL url, ResourceBundle resources) {
        controller = new GameController();

        boardPane.setPrefSize(controller.preferredBoardWidth(), controller.preferredBoardHeight());
        canvas.widthProperty().bind(boardPane.widthProperty());
        canvas.heightProperty().bind(boardPane.heightProperty());

        view = new GameView(canvas);
        controller.attachView(view);

        canvas.widthProperty().addListener((obs, oldValue, newValue) -> controller.redraw());
        canvas.heightProperty().addListener((obs, oldValue, newValue) -> controller.redraw());

        loop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                long periodNs = controller.tickMillis() * 1_000_000L;
                if (now - lastTickNs >= periodNs) {
                    lastTickNs = now;
                    controller.tick();
                    updateHud();
                }
            }
        };
        loop.start();
        updateHud();
    }

    private void updateHud() {
        scoreLabel.setText("Length: " + controller.snakeLength());
        targetLabel.setText("Target: " + controller.targetLength());
    }

    @FXML
    private void onKeyPressed(KeyEvent event) {
        KeyCode code = event.getCode();
        switch (code) {
            case UP, W -> controller.turnUp();
            case DOWN, S -> controller.turnDown();
            case LEFT, A -> controller.turnLeft();
            case RIGHT, D -> controller.turnRight();
            case SPACE -> handleSpace();
            default -> { /* ignore */ }
        }
    }

    private void handleSpace() {
        controller.startOrRestart();
        lastTickNs = 0;
        updateHud();
    }
}
