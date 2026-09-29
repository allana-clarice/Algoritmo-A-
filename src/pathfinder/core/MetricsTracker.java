package pathfinder.core;

public class MetricsTracker {
    private double executionTimeMs;
    private int nodesVisited;
    private double pathCost;

    public void update(double executionTimeMs, int nodesVisited, double pathCost) {
        this.executionTimeMs = executionTimeMs;
        this.nodesVisited = nodesVisited;
        this.pathCost = pathCost;
    }

    public void reset() {
        this.executionTimeMs = 0.0;
        this.nodesVisited = 0;
        this.pathCost = 0.0;
    }

    public double getExecutionTimeMs() { return executionTimeMs; }
    public int getNodesVisited() { return nodesVisited; }
    public double getPathCost() { return pathCost; }
}