package ru.nsu.munkuev.snake.controller;

import ru.nsu.munkuev.snake.model.Direction;
import ru.nsu.munkuev.snake.model.Food;
import ru.nsu.munkuev.snake.model.GameConfig;
import ru.nsu.munkuev.snake.model.GameField;
import ru.nsu.munkuev.snake.model.GameState;
import ru.nsu.munkuev.snake.model.Point;
import ru.nsu.munkuev.snake.view.GameFrame;
import ru.nsu.munkuev.snake.view.GameRenderer;

import java.util.List;

/**
 * Игровой контроллер без привязки к JavaFX.
 *
 * <p>Контроллер управляет моделью, принимает команды от внешнего слоя
 * и передаёт во view готовый снимок состояния игры. JavaFX-классов здесь нет:
 * FXML, Canvas, Label, KeyEvent и таймеры находятся только в пакете {@code view}.
 */
public class GameController {

    private static final int MAX_INITIAL_BOARD_SIZE = 900;

    private final GameConfig config;
    private GameField field;
    private GameRenderer renderer;

    public GameController() {
        this(GameConfig.defaultConfig());
    }

    public GameController(GameConfig config) {
        this.config = config;
        newGame();
    }

    /** Подключает представление, которому контроллер будет отдавать снимки игры */
    public void attachView(GameRenderer renderer) {
        this.renderer = renderer;
        render();
    }

    /** Один игровой тик. */
    public void tick() {
        field.tick();
        render();
    }

    /** Повторно отдаёт во view текущий снимок игры без изменения модели */
    public void redraw() {
        render();
    }

    /** Запускает игру или перезапускает её после победы/поражения */
    public void startOrRestart() {
        if (field.state() == GameState.READY) {
            field.start();
        } else if (field.state().isFinished()) {
            newGame();
            field.start();
        }
        render();
    }

    public void turnUp() {
        requestTurn(Direction.UP);
    }

    public void turnDown() {
        requestTurn(Direction.DOWN);
    }

    public void turnLeft() {
        requestTurn(Direction.LEFT);
    }

    public void turnRight() {
        requestTurn(Direction.RIGHT);
    }

    private void requestTurn(Direction direction) {
        field.requestTurn(direction);
    }

    private void newGame() {
        field = new GameField(config);
    }

    private void render() {
        if (renderer != null) {
            renderer.render(snapshot());
        }
    }

    private GameFrame snapshot() {
        List<Point> foods = field.foods().stream()
                .map(Food::position)
                .toList();

        String overlayTitle = null;
        String overlaySubtitle = null;
        if (field.state() == GameState.READY) {
            overlayTitle = "SNAKE";
            overlaySubtitle = "Press SPACE to start";
        } else if (field.state() == GameState.WON) {
            overlayTitle = "YOU WIN!";
            overlaySubtitle = "Length " + field.snake().length() + " — press SPACE to restart";
        } else if (field.state() == GameState.LOST) {
            overlayTitle = "GAME OVER";
            overlaySubtitle = "Length " + field.snake().length() + " — press SPACE to restart";
        }

        return new GameFrame(
                config.width(),
                config.height(),
                field.snake().body(),
                foods,
                List.copyOf(field.obstacles()),
                overlayTitle,
                overlaySubtitle
        );
    }

    public int snakeLength() {
        return field.snake().length();
    }

    public int targetLength() {
        return config.winLength();
    }

    public int tickMillis() {
        return config.tickMillis();
    }

    public double preferredBoardWidth() {
        return Math.min(config.width() * config.cellSize(), MAX_INITIAL_BOARD_SIZE);
    }

    public double preferredBoardHeight() {
        return Math.min(config.height() * config.cellSize(), MAX_INITIAL_BOARD_SIZE);
    }
}
