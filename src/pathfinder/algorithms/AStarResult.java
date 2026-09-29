package pathfinder.algorithms;

import pathfinder.core.Node;
import java.util.List;

public class AStarResult {
    public final boolean success;
    public final int nodesVisited;
    public final double pathCost;
    public final double executionTimeMs;
    public final List<Node> exploredNodes;
    public final List<Node> path;

    public AStarResult(boolean success, int nodesVisited, double pathCost, double executionTimeMs, List<Node> exploredNodes, List<Node> path) {
        this.success = success;
        this.nodesVisited = nodesVisited;
        this.pathCost = pathCost;
        this.executionTimeMs = executionTimeMs;
        this.exploredNodes = exploredNodes;
        this.path = path;
    }
}