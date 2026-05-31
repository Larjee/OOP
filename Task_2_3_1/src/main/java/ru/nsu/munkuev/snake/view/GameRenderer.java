package ru.nsu.munkuev.snake.view;

/**
 * Минимальный интерфейс представления.
 * Контроллер вызывает только этот метод и не знает про JavaFX-детали отрисовки
 */
public interface GameRenderer {

    /** Отрисовывает готовый снимок состояния игры */
    void render(GameFrame frame);
}
