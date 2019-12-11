package bot.games.minesweeper;

import bot.games.minesweeper.minesweeperModel.Cell;
import bot.games.minesweeper.minesweeperModel.CellType;
import bot.games.minesweeper.minesweeperModel.MinesweeperModel;
import org.json.JSONObject;
import java.awt.*;
import java.util.HashSet;
import java.util.Iterator;

public class Minesweeper {
    private MinesweeperModel minesweeperModel;
    public boolean isPushToOpen;

    public Minesweeper() {
        minesweeperModel = new MinesweeperModel(8, 9);
        isPushToOpen = true;
    }

    public String getAnswer(JSONObject object) {
        int x = object.getInt("x");
        int y = object.getInt("y");
        HashSet<Point> openCells;
        String message = "\"message\":\"Бомб осталось: " + minesweeperModel.getBombsLeftCount() + "\"";
        if (isPushToOpen) {
            openCells = minesweeperModel.openCell(x, y);
        } else {
            minesweeperModel.markCell(x, y);
            return "{\"markedCell\":{\"x\":" + x + ",\"y\":" + y + "}," + message + "}";
        }
        if (openCells == null)
            return "{" + message + "}";
        return "{" + getPointJSONArrayInString(openCells) + "," + message + "}";
    }

    public boolean isGameOver() {
        return minesweeperModel.isGameOver();
    }

    public int getCellEmojiCode(int x, int y) {
        Cell cell = minesweeperModel.getCellClone(x, y);
        switch (cell.getState()) {
            case OPEN:
                switch (cell.getType()) {
                    case BOMB:
                        return 0x1F4A5;  //взрыв
                    case NUMBER:
                        return 0x1D7D8 + cell.getNumber();  //цифры 1-8
                    default:
                        return 0x0020; // 0x2B1C - белый квадрат  0x0020 - пробел
                }
            case MARKED:
                if (cell.getType() == CellType.BOMB)
                    if (isGameOver())
                        return 0x1F4A3;  //найденная бомба
                return 0x1F6A9;  //флажок
            default:
                return 0x2B1B;  //черный квадрат
        }
    }

    public String getGameInfo() {
        return "Бомб осталось: " + minesweeperModel.getBombsLeftCount();
    }

    String getPointJSONArrayInString (HashSet<Point> openCells) {
        StringBuilder result = new StringBuilder();
        result.append("\"openCells\":[");
        Iterator<Point> iterator = openCells.iterator();
        while (iterator.hasNext()){
            Point cell = iterator.next();
            result.append("{\"x\":");
            result.append(cell.x);
            result.append(",\"y\":");
            result.append(cell.y);
            result.append("}");
            if (iterator.hasNext())
                result.append(",");
        }
        result.append("]");
        return result.toString();
    }
}
