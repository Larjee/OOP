package ru.nsu.munkuev.snake.model;

/**
 * состояние игровой сессии.
 */
public enum GameState {
    READY,
    RUNNING,
    WON,
    LOST;

    public boolean isFinished() {
        return this == WON || this == LOST;
    }
}
