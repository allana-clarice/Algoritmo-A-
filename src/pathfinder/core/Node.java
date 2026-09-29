package pathfinder.core;

import java.util.Objects;

public class Node {
    private final int row;
    private final int col;
    private AppState state;

    public Node(int row, int col) {
        this.row = row;
        this.col = col;
        this.state = AppState.EMPTY;
    }

    public int getRow() { return row; }
    public int getCol() { return col; }
    public AppState getState() { return state; }
    public void setState(AppState state) { this.state = state; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return row == node.row && col == node.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}