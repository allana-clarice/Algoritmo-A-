package pathfinder.algorithms;

import pathfinder.core.Node;

public class Heuristic {
    public static double manhattanDistance(Node a, Node b) {
        return Math.abs(a.getRow() - b.getRow()) + Math.abs(a.getCol() - b.getCol());
    }
}