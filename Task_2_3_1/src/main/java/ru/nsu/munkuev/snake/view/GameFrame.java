package ru.nsu.munkuev.snake.view;

import ru.nsu.munkuev.snake.model.Point;

import java.util.List;

/**
 * Данные для отрисовки одного кадра
 *
 * <p>View получает не модель целиком, а только простые значения: размеры поля,
 * точки змейки, точки еды, точки препятствий и текст оверлея
 */
public record GameFrame(
        int width,
        int height,
        List<Point> snakeBody,
        List<Point> foods,
        List<Point> obstacles,
        String overlayTitle,
        String overlaySubtitle
) {
    public boolean hasOverlay() {
        return overlayTitle != null && overlaySubtitle != null;
    }
}
