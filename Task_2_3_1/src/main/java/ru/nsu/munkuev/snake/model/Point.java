package ru.nsu.munkuev.snake.model;

/**
 * координата на поле
 */
public record Point(int x, int y) {

    /**
     * Возвращает точку, сдвинутую по направлению
     */
    public Point shift(Direction d) {
        return new Point(x + d.dx(), y + d.dy());
    }
}
