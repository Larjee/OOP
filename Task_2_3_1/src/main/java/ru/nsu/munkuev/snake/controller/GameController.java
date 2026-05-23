package ru.nsu.munkuev.snake.controller;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import ru.nsu.munkuev.snake.model.Direction;
import ru.nsu.munkuev.snake.model.GameConfig;
import ru.nsu.munkuev.snake.model.GameField;
import ru.nsu.munkuev.snake.model.GameState;
import ru.nsu.munkuev.snake.view.GameView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML-контроллер: связывает модель, представление и ввод пользователя.
 * Управляет игровым циклом через {@link AnimationTimer} с настраиваемым
 * периодом тика из {@link GameConfig#tickMillis()}.
 */
public class GameController implements Initializable {

    @FXML private StackPane boardPane;
    @FXML private Canvas canvas;
    @FXML private Label scoreLabel;
    @FXML private Label targetLabel;

    private GameConfig config;
    private GameField field;
    private GameView view;

    private AnimationTimer loop;
    private long lastTickNs;

    @Override
    public void initialize(URL url, ResourceBundle resources) {
        config = GameConfig.defaultConfig();

        double preferredBoardWidth = Math.min(config.width() * config.cellSize(), 900);
        double preferredBoardHeight = Math.min(config.height() * config.cellSize(), 900);
        boardPane.setPrefSize(preferredBoardWidth, preferredBoardHeight);

        canvas.widthProperty().bind(boardPane.widthProperty());
        canvas.heightProperty().bind(boardPane.heightProperty());

        view = new GameView(canvas);
        newGame();

        canvas.widthProperty().addListener((obs, oldValue, newValue) -> view.render(field));
        canvas.heightProperty().addListener((obs, oldValue, newValue) -> view.render(field));

        loop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                long periodNs = config.tickMillis() * 1_000_000L;
                if (now - lastTickNs >= periodNs) {
                    lastTickNs = now;
                    field.tick();
                    updateHud();
                    view.render(field);
                }
            }
        };
        loop.start();
        view.render(field);
        updateHud();
    }

    private void newGame() {
        field = new GameField(config);
        lastTickNs = 0;
    }

    private void updateHud() {
        scoreLabel.setText("Length: " + field.snake().length());
        targetLabel.setText("Target: " + config.winLength());
    }

    @FXML
    private void onKeyPressed(KeyEvent e) {
        KeyCode code = e.getCode();
        switch (code) {
            case UP, W    -> field.requestTurn(Direction.UP);
            case DOWN, S  -> field.requestTurn(Direction.DOWN);
            case LEFT, A  -> field.requestTurn(Direction.LEFT);
            case RIGHT, D -> field.requestTurn(Direction.RIGHT);
            case SPACE    -> handleSpace();
            default -> { /* ignore */ }
        }
    }

    private void handleSpace() {
        if (field.state() == GameState.READY) {
            field.start();
        } else if (field.state().isFinished()) {
            newGame();
            field.start();
        }
        view.render(field);
        updateHud();
    }
}
