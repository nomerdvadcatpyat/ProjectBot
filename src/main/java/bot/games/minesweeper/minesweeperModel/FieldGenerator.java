package bot.games.minesweeper.minesweeperModel;

import java.awt.*;
import java.util.HashSet;
import java.util.Random;

public class FieldGenerator {
    public static Cell[][] generateField(int width, int height, int startRow, int startColumn) {
        Cell[][] result = new Cell[height][width];
        int bombsLeft = 10;
        HashSet<Point> bombs = new HashSet<>();
        while (bombsLeft > 0) {
            Random rnd = new Random();
            int x = rnd.nextInt(height);
            int y = rnd.nextInt(width);
            if (isWithinBorders(width, height, x, y) && notNearStart(x, y, startRow, startColumn) &&
                    !bombs.contains(new Point(x, y))) {
                result[x][y] = new Cell(CellType.BOMB);
                bombs.add(new Point(x, y));
                bombsLeft--;
            }
        }
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                int nearestBombsCount = nearestBombsCount(result, i, j);
                if (result[i][j] == null || result[i][j].getType() != CellType.BOMB) {
                    if (nearestBombsCount > 0)
                        result[i][j] = new Cell(nearestBombsCount);
                    else
                        result[i][j] = new Cell(CellType.EMPTY);
                }
            }
        return result;
    }

    public static Cell[][] createEmptyClosedField(int width, int height) {
        Cell[][] result = new Cell[height][width];
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                result[i][j] = new Cell(CellType.EMPTY);
        return result;
    }

    public static boolean isWithinBorders(int width, int height, int x, int y) {
        return x >= 0 && x < height && y >= 0 && y < width;
    }

    private static boolean notNearStart(int x, int y, int startX, int startY) {
        return (x < startX - 1 || x > startX + 1) && (y < startY - 1 || y > startY + 1);
    }

    private static int nearestBombsCount(Cell[][] field, int x, int y) {
        int result = 0;
        for (int i = -1; i <= 1; i++)
            for (int j = -1; j <= 1; j++) {
                int currentX = x + i;
                int currentY = y + j;
                if (!isWithinBorders(field[0].length, field.length,  currentX, currentY) || (i * i) + (j * j) == 0)
                    continue;
                Cell cell = field[currentX][currentY];
                if (cell != null && cell.getType() == CellType.BOMB)
                    result++;
            }
        return result;
    }
}
