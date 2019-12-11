package bot.games.minesweeper.minesweeperModel;

import java.awt.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class MinesweeperModel {
    private Cell[][] field;
    private boolean isFirstTurn;
    private HashSet<Point> leftCellsToOpen;
    private boolean gameOver;
    private int bombsLeft;

    public MinesweeperModel(int width, int height) {
        this.field = FieldGenerator.createEmptyClosedField(width, height);
        isFirstTurn = true;
        gameOver = false;
        bombsLeft = 10;
    }

    public HashSet<Point> openCell(int x, int y) {
        Cell cell = field[x][y];
        if (cell.getState() == CellState.OPEN ||
                cell.getState() == CellState.MARKED)
            return null;
        if (isFirstTurn) {
            field = FieldGenerator.generateField(field[0].length, field.length, x, y);
            isFirstTurn = false;
            leftCellsToOpen = getNotBombCells(field);
        }
        HashSet<Point> openCells = new HashSet<>();
        if (cell.getType() == CellType.EMPTY)
            openAdjacentCells(openCells, x, y);
        else if (cell.getType() == CellType.NUMBER) {
            cell.setState(CellState.OPEN);
            Point point = new Point(x, y);
            openCells.add(point);
            leftCellsToOpen.remove(point);
        } else {
            openUnmarkedBombs(openCells);
            gameOver = true;
        }
        if (leftCellsToOpen.isEmpty())
            gameOver = true;
        return openCells;
    }

    public Point markCell(int x, int y) {
        Cell cell = field[x][y];
        if (cell.getState() == CellState.CLOSED) {
            cell.setState(CellState.MARKED);
            bombsLeft--;
        } else if (cell.getState() == CellState.MARKED) {
            cell.setState(CellState.CLOSED);
            bombsLeft++;
        }
        return new Point(x, y);
    }

    public int getCellNumber(int x, int y) {
        return field[x][y].getNumber();
    }

    public int getBombsLeftCount() {
        return bombsLeft;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public Cell getCellClone(int x, int y) {
        try {
            return field[x][y].clone();
        } catch (CloneNotSupportedException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private void openAdjacentCells(HashSet<Point> openCells, int x, int y) {
        Queue<Point> queue = new LinkedList<>();
        queue.add(new Point(x, y));
        while (queue.size() != 0) {
            Point point = queue.remove();
            Cell cell = field[point.x][point.y];
            cell.setState(CellState.OPEN);
            if (cell.getType() == CellType.EMPTY) {
                for (int dx = -1; dx <= 1; dx++)
                    for (int dy = -1; dy <= 1; dy++) {
                        if (!FieldGenerator.isWithinBorders(field[0].length, field.length, point.x + dx, point.y + dy) ||
                            field[point.x + dx][point.y + dy].getState() != CellState.CLOSED)
                            continue;
                        queue.add(new Point(point.x + dx, point.y + dy));
                    }
            }
            openCells.add(point);
            leftCellsToOpen.remove(point);
        }
    }

    private void openUnmarkedBombs(HashSet<Point> openCells) {
        for (int i = 0; i < field.length; i++)
            for (int j = 0; j < field[0].length; j++) {
                Cell cell = field[i][j];
                if (cell.getType() == CellType.BOMB && cell.getState() == CellState.CLOSED) {
                    cell.setState(CellState.OPEN);
                    openCells.add(new Point(i, j));
                }
            }
    }

    private static HashSet<Point> getNotBombCells(Cell[][] field) {
        HashSet<Point> result = new HashSet<>();
        for (int i = 0; i < field.length; i++)
            for (int j = 0; j < field[0].length; j++)
                if (field[i][j].getType() != CellType.BOMB)
                    result.add(new Point(i, j));
        return result;
    }
}