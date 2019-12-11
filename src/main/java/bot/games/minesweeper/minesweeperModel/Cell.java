package bot.games.minesweeper.minesweeperModel;

import bot.tools.locator.Location;

public class Cell implements Cloneable {
    private final CellType type;
    private CellState state;
    private int number;

    public Cell(CellType type) {
        this.type = type;
        state = CellState.CLOSED;
    }

    public Cell(int number) {
        type = CellType.NUMBER;
        this.number = number;
        state = CellState.CLOSED;
    }

    public CellType getType() {
        return type;
    }

    public CellState getState() {
        return state;
    }

    public void setState(CellState state) {
        this.state = state;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public Cell clone() throws CloneNotSupportedException{
        return (Cell) super.clone();
    }
}
