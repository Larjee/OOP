package ru.nsu.munkuev.snake.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import ru.nsu.munkuev.snake.model.Food;
import ru.nsu.munkuev.snake.model.GameField;
import ru.nsu.munkuev.snake.model.GameState;
import ru.nsu.munkuev.snake.model.Point;

import java.util.List;

/**
 * Отрисовывает {@link GameField} на {@link Canvas}. Чистое представление:
 * читает данные из модели и никогда её не изменяет.
 */
public class GameView {

    private static final Color BG_DARK = Color.web("#DA70D6");
    private static final Color BG_LIGHT = Color.web("#DDA0DD");
    private static final Color SNAKE_HEAD = Color.web("#6ee7b7");
    private static final Color SNAKE_BODY = Color.web("#34d399");
    private static final Color FOOD = Color.web("#4B0082");
    private static final Color OBSTACLE = Color.web("#52525b");
    private static final Color TEXT = Color.web("#f4f4f5");
    private static final Color OVERLAY = Color.color(0, 0, 0, 0.55);

    private final Canvas canvas;

    public GameView(Canvas canvas) {
        this.canvas = canvas;
    }

    public void render(GameField field) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        int w = field.config().width();
        int h = field.config().height();

        double canvasWidth = canvas.getWidth();
        double canvasHeight = canvas.getHeight();
        if (canvasWidth <= 0 || canvasHeight <= 0) {
            return;
        }

        double cell = Math.min(canvasWidth / w, canvasHeight / h);
        double boardWidth = cell * w;
        double boardHeight = cell * h;
        double offsetX = (canvasWidth - boardWidth) / 2;
        double offsetY = (canvasHeight - boardHeight) / 2;

        gc.clearRect(0, 0, canvasWidth, canvasHeight);

        //Шахматный фон
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                gc.setFill(((x + y) % 2 == 0) ? BG_DARK : BG_LIGHT);
                gc.fillRect(offsetX + x * cell, offsetY + y * cell, cell, cell);
            }
        }

        //Препятствия
        gc.setFill(OBSTACLE);
        for (Point p : field.obstacles()) {
            gc.fillRect(offsetX + p.x() * cell, offsetY + p.y() * cell, cell, cell);
        }

        //Еда
        gc.setFill(FOOD);
        for (Food f : field.foods()) {
            Point p = f.position();
            double pad = cell * 0.15;
            gc.fillOval(offsetX + p.x() * cell + pad, offsetY + p.y() * cell + pad,
                    cell - 2 * pad, cell - 2 * pad);
        }

        //Змейка
        List<Point> body = field.snake().body();
        for (int i = 0; i < body.size(); i++) {
            Point p = body.get(i);
            gc.setFill(i == 0 ? SNAKE_HEAD : SNAKE_BODY);
            double pad = Math.max(0.5, cell * 0.06);
            gc.fillRoundRect(offsetX + p.x() * cell + pad, offsetY + p.y() * cell + pad,
                    cell - 2 * pad, cell - 2 * pad, cell * 0.35, cell * 0.35);
        }

        //Оверлеи
        GameState state = field.state();
        if (state == GameState.READY) {
            drawOverlay(gc, "SNAKE", "Press SPACE to start");
        }
        else if (state == GameState.WON) {
            drawOverlay(gc, "YOU WIN!",
                    "Length " + field.snake().length() + " — press SPACE to restart");
        }
        else if (state == GameState.LOST) {
            drawOverlay(gc, "GAME OVER",
                    "Length " + field.snake().length() + " — press SPACE to restart");
        }
    }

    private void drawOverlay(GraphicsContext gc, String title, String subtitle) {
        gc.setFill(OVERLAY);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFill(TEXT);

        gc.setFont(Font.font("Arial", 42));
        gc.fillText(title, canvas.getWidth() / 2, canvas.getHeight() / 2 - 10);

        gc.setFont(Font.font("Arial", 16));
        gc.fillText(subtitle, canvas.getWidth() / 2, canvas.getHeight() / 2 + 24);
    }
}
