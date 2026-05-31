package ru.nsu.munkuev.snake.model;

/**
 * Кусочек еды. Увеличивает змейку на значение {@link #growth()} cells.
 *
 * <p>Этот класс расширяемый. Например, чтоб добавить новый вид еды, надо унаследовать
 * класс {@code Food} и переопределить {@link #growth()} или {@link #onEaten(Snake)}.
 */
public class Food {

    private final Point position;

    public Food(Point position) {
        this.position = position;
    }

    public Point position() {
        return position;
    }

    /** На сколько единиц растет змейка*/
    public int growth() {
        return 3;
    }

    /** Вызывается игрой, когда змейка съедает кусочек еды */
    public void onEaten(Snake snake) {
        snake.grow(growth());
    }
}
