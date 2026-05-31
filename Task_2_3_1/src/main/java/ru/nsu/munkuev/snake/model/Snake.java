package ru.nsu.munkuev.snake.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Змейка представляет собой цепочку клеток с головы до хвоста
 *
 * <p>Движение реализуется путём добавления клетки перед головой змейки
 * в направлении движения и удалением клетки с конца. При вызове {@link #grow(int)}
 * просто пропускается удаление хвостовой клетки на величину роста змейки, таким образом
 * змейка удлиняется.
 *
 * <p>Изменения направления змейки буфферизированы в маленькой очереди, чтоб исключить поворот
 * змейки на 180 градусов. К примеру изменение LEFT применённое сразу после UP может повернуть
 * змейку на 180 градусов.
 */
public class Snake {

    private static final int MAX_BUFFERED_TURNS = 2;

    private final Deque<Point> body = new ArrayDeque<>();
    private final Deque<Direction> pendingTurns = new ArrayDeque<>();

    private Direction direction;
    private int growthDebt = 0;

    public Snake(Point start, Direction startDirection) {
        body.addFirst(start);
        this.direction = startDirection;
    }

    /**
     * Возвращает клетку, в которой будет голова в следующий тик
     * Полезно для проверки коллизий перед применением движения
     */
    public Point nextHead() {
        Direction nextDir = pendingTurns.isEmpty() ? direction : pendingTurns.peekFirst();
        return body.peekFirst().shift(nextDir);
    }

    /**
     * Передвигает змейку на одну клетку по направлению движения
     */
    public void move() {
        if (!pendingTurns.isEmpty()) {
            direction = pendingTurns.pollFirst();
        }
        body.addFirst(body.peekFirst().shift(direction));
        if (growthDebt > 0) {
            growthDebt--;
        } else {
            body.pollLast();
        }
    }

    /**
     * Запрашивает изменение направления. Изменение кладётся в буфер и применяется
     * на следующем тике. Поворот на 180 градусов относительно последнего направления
     * в очереди молча игнорируется, чтобы змейка не врезалась сама в себя
     */
    public void turn(Direction newDirection) {
        Direction reference = pendingTurns.isEmpty() ? direction : pendingTurns.peekLast();
        if (newDirection == reference || newDirection == reference.opposite()) {
            return;
        }
        if (pendingTurns.size() < MAX_BUFFERED_TURNS) {
            pendingTurns.addLast(newDirection);
        }
    }

    /** Увеличивает змейку на {@code cells} клеток при следующих движениях */
    public void grow(int cells) {
        if (cells > 0) {
            growthDebt += cells;
        }
    }

    public Point head() {
        return body.peekFirst();
    }

    public int length() {
        return body.size();
    }

    public Direction direction() {
        return direction;
    }

    /** Защитная копия тела змейки от головы к хвосту */
    public List<Point> body() {
        return new ArrayList<>(body);
    }

    /**
     * Возвращает true, если заданная точка входит в тело змейки. Хвостовая клетка
     * исключается, когда змейка не должна расти, потому что на этом тике хвост
     * уйдёт с этой позиции
     */
    public boolean collidesWithSelf(Point p) {
        boolean tailLeavesThisTick = growthDebt == 0;
        int i = 0;
        int last = body.size() - 1;
        for (Point cell : body) {
            if (tailLeavesThisTick && i == last) {
                break;
            }
            if (cell.equals(p)) {
                return true;
            }
            i++;
        }
        return false;
    }
}
