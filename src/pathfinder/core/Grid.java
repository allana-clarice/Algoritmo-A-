package pathfinder.core;

import java.util.ArrayList;
import java.util.List;

public class Grid {
    private final int size;
    private final Node[][] nodes;
    private Node startNode;
    private Node targetNode;

    public Grid(int size) {
        this.size = size;
        this.nodes = new Node[size][size];
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                nodes[r][c] = new Node(r, c);
            }
        }
    }

    public int getSize() { return size; }
    public Node getNode(int r, int c) { return nodes[r][c]; }

    public Node getStartNode() { return startNode; }
    public void setStartNode(Node startNode) { this.startNode = startNode; }

    public Node getTargetNode() { return targetNode; }
    public void setTargetNode(Node targetNode) { this.targetNode = targetNode; }

    public List<Node> getNeighbors(Node node) {
        List<Node> neighbors = new ArrayList<>();
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int[] dir : directions) {
            int nr = node.getRow() + dir[0];
            int nc = node.getCol() + dir[1];
            if (nr >= 0 && nr < size && nc >= 0 && nc < size) {
                neighbors.add(nodes[nr][nc]);
            }
        }
        return neighbors;
    }

    public void clearPathAndExplored() {
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                Node n = nodes[r][c];
                if (n.getState() == AppState.EXPLORED || n.getState() == AppState.PATH) {
                    n.setState(AppState.EMPTY);
                }
            }
        }
        if (startNode != null) startNode.setState(AppState.START);
        if (targetNode != null) targetNode.setState(AppState.TARGET);
    }

    public void resetAll() {
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                nodes[r][c].setState(AppState.EMPTY);
            }
        }
        startNode = null;
        targetNode = null;
    }
}