package ru.nsu.munkuev.snake.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Полный игровой мир: размеры поля, змейка, еда, препятствия и текущее
 * состояние {@link GameState}. Все игровые правила находятся здесь.
 *
 * <p>Сейчас препятствий по умолчанию нет, но поле уже проверяет столкновения
 * с ними — заполните {@link #obstacles}, например из описания уровня, чтобы добавить стены.
 */
public class GameField {

    private final GameConfig config;
    private final Random random;

    private final Snake snake;
    private final List<Food> foods = new ArrayList<>();
    private final Set<Point> obstacles = new HashSet<>();

    private GameState state = GameState.READY;

    public GameField(GameConfig config) {
        this(config, new Random());
    }

    /** Конструктор с передаваемым генератором случайных чисел */
    public GameField(GameConfig config, Random random) {
        this.config = config;
        this.random = random;
        Point start = new Point(config.width() / 2, config.height() / 2);
        this.snake = new Snake(start, Direction.RIGHT);

        for (int i = 0; i < config.foodCount(); i++) {
            spawnFood();
        }
    }

    /**
     * Один тик игры. Работает только при {@link GameState#RUNNING}
     */
    public void tick() {
        if (state != GameState.RUNNING) {
            return;
        }
        Point next = snake.nextHead();

        //Столкновение со стеной
        if (next.x() < 0 || next.x() >= config.width()
                || next.y() < 0 || next.y() >= config.height()) {
            state = GameState.LOST;
            return;
        }
        //Столкновение с препятствием
        if (obstacles.contains(next)) {
            state = GameState.LOST;
            return;
        }
        //Столкновение с собой
        if (snake.collidesWithSelf(next)) {
            state = GameState.LOST;
            return;
        }

        Food eaten = foodAt(next);

        if (eaten != null) {
            eaten.onEaten(snake);
        }
        snake.move();
        if (eaten != null) {
            foods.remove(eaten);
            spawnFood();
        }
        if (snake.length() >= config.winLength()) {
            state = GameState.WON;
        }
    }

    /** Передаёт змейке запрос на поворот */
    public void requestTurn(Direction d) {
        if (state == GameState.RUNNING) {
            snake.turn(d);
        }
    }

    /** Запускает игру из состояния READY. */
    public void start() {
        if (state == GameState.READY) {
            state = GameState.RUNNING;
        }
    }

    private Food foodAt(Point p) {
        for (Food f : foods) {
            if (f.position().equals(p)) {
                return f;
            }
        }
        return null;
    }

    /**
     * Размещает новый кусочек еды на случайной свободной клетке. <P>Переопределить
     * метод или заменить создаваемый объект, чтобы добавить новые виды еды
     */
    protected void spawnFood() {
        List<Point> free = freeCells();
        if (free.isEmpty()) {
            return;
        }
        Point p = free.get(random.nextInt(free.size()));
        foods.add(new Food(p));
    }

    private List<Point> freeCells() {
        Set<Point> taken = new HashSet<>(obstacles);
        taken.addAll(snake.body());
        for (Food f : foods) {
            taken.add(f.position());
        }
        List<Point> free = new ArrayList<>();
        for (int x = 0; x < config.width(); x++) {
            for (int y = 0; y < config.height(); y++) {
                Point p = new Point(x, y);
                if (!taken.contains(p)) {
                    free.add(p);
                }
            }
        }
        return free;
    }


    public GameConfig config() {
        return config;
    }

    public Snake snake() {
        return snake;
    }

    public List<Food> foods() {
        return Collections.unmodifiableList(foods);
    }

    public Set<Point> obstacles() {
        return Collections.unmodifiableSet(obstacles);
    }

    public GameState state() {
        return state;
    }
}
