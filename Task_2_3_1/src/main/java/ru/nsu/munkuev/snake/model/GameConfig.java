package ru.nsu.munkuev.snake.model;

/**
 * Tunable game parameters. Change these to reshape the game:
 * Изменяемые игровые параметры. Для переделки игры измените:
 * <ul>
 *   <li>{@code width}, {@code height} — размер поля N x M;</li>
 *   <li>{@code foodCount} — количество еды, отображаемое на поле;</li>
 *   <li>{@code winLength} — необходимая для победы длина змейки;</li>
 *   <li>{@code tickMillis} — время одного тика;</li>
 *   <li>{@code cellSize} — размер одной игровой клетки в пикселях.</li>
 * </ul>
 *
 * <p>Для увеличения скорости игры создавайте конфиги с меньшим значением {@code tickMillis}
 * для каждого подуровня
 */
public record GameConfig(
        int width,
        int height,
        int foodCount,
        int winLength,
        int tickMillis,
        int cellSize
) {
    public static GameConfig defaultConfig() {
        return new GameConfig(50, 50, 3, 15, 140, 30);
    }
}
