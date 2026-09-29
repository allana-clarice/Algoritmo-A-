package pathfinder.algorithms;

import pathfinder.core.AppState;
import pathfinder.core.Grid;
import pathfinder.core.Node;

import java.util.*;

public class PathfinderAlgorithms {

    private static class PriorityItem implements Comparable<PriorityItem> {
        final Node node;
        final double priority;

        PriorityItem(Node node, double priority) {
            this.node = node;
            this.priority = priority;
        }

        @Override
        public int compareTo(PriorityItem o) {
            return Double.compare(this.priority, o.priority);
        }
    }

    public static AStarResult run(Grid grid, AlgorithmType type) {
        if (type == AlgorithmType.GREEDY) {
            return runGreedyBestFirst(grid);
        }
        return runAStar(grid);
    }

    public static AStarResult runGreedyBestFirst(Grid grid) {
        Node startNode = grid.getStartNode();
        Node targetNode = grid.getTargetNode();

        if (startNode == null || targetNode == null) {
            return new AStarResult(false, 0, 0.0, 0.0, Collections.emptyList(), Collections.emptyList());
        }

        long startTime = System.nanoTime();

        PriorityQueue<PriorityItem> openSet = new PriorityQueue<>();
        openSet.add(new PriorityItem(startNode, Heuristic.manhattanDistance(startNode, targetNode)));

        Map<Node, Node> cameFrom = new HashMap<>();
        Set<Node> visited = new HashSet<>();
        visited.add(startNode);

        int nodesVisited = 0;
        List<Node> exploredNodes = new ArrayList<>();

        while (!openSet.isEmpty()) {
            PriorityItem currentItem = openSet.poll();
            Node current = currentItem.node;

            nodesVisited++;

            if (current.equals(targetNode)) {
                long endTime = System.nanoTime();
                double elapsedMs = (endTime - startTime) / 1_000_000.0;

                List<Node> path = reconstructPath(cameFrom, targetNode, startNode);
                return new AStarResult(true, nodesVisited, path.size() + 1, elapsedMs, exploredNodes, path);
            }

            if (!current.equals(startNode) && !current.equals(targetNode)) {
                exploredNodes.add(current);
            }

            for (Node neighbor : grid.getNeighbors(current)) {
                if (neighbor.getState() == AppState.WALL || visited.contains(neighbor)) {
                    continue;
                }

                visited.add(neighbor);
                cameFrom.put(neighbor, current);
                double h = Heuristic.manhattanDistance(neighbor, targetNode);
                openSet.add(new PriorityItem(neighbor, h));
            }
        }

        long endTime = System.nanoTime();
        double elapsedMs = (endTime - startTime) / 1_000_000.0;
        return new AStarResult(false, nodesVisited, 0.0, elapsedMs, exploredNodes, Collections.emptyList());
    }

    public static AStarResult runAStar(Grid grid) {
        Node startNode = grid.getStartNode();
        Node targetNode = grid.getTargetNode();

        if (startNode == null || targetNode == null) {
            return new AStarResult(false, 0, 0.0, 0.0, Collections.emptyList(), Collections.emptyList());
        }

        long startTime = System.nanoTime();

        PriorityQueue<PriorityItem> openSet = new PriorityQueue<>();
        openSet.add(new PriorityItem(startNode, 0.0));

        Map<Node, Node> cameFrom = new HashMap<>();
        Map<Node, Double> gScore = new HashMap<>();
        Map<Node, Double> fScore = new HashMap<>();

        gScore.put(startNode, 0.0);
        fScore.put(startNode, Heuristic.manhattanDistance(startNode, targetNode));

        int nodesVisited = 0;
        List<Node> exploredNodes = new ArrayList<>();

        while (!openSet.isEmpty()) {
            PriorityItem currentItem = openSet.poll();
            Node current = currentItem.node;

            if (currentItem.priority > fScore.getOrDefault(current, Double.POSITIVE_INFINITY)) {
                continue;
            }

            nodesVisited++;

            if (current.equals(targetNode)) {
                long endTime = System.nanoTime();
                double elapsedMs = (endTime - startTime) / 1_000_000.0;

                List<Node> path = reconstructPath(cameFrom, targetNode, startNode);
                return new AStarResult(true, nodesVisited, path.size() + 1, elapsedMs, exploredNodes, path);
            }

            if (!current.equals(startNode) && !current.equals(targetNode)) {
                exploredNodes.add(current);
            }

            for (Node neighbor : grid.getNeighbors(current)) {
                if (neighbor.getState() == AppState.WALL) {
                    continue;
                }

                double tentativeG = gScore.getOrDefault(current, Double.POSITIVE_INFINITY) + 1.0;

                if (tentativeG < gScore.getOrDefault(neighbor, Double.POSITIVE_INFINITY)) {
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tentativeG);
                    double f = tentativeG + Heuristic.manhattanDistance(neighbor, targetNode);
                    fScore.put(neighbor, f);
                    openSet.add(new PriorityItem(neighbor, f));
                }
            }
        }

        long endTime = System.nanoTime();
        double elapsedMs = (endTime - startTime) / 1_000_000.0;
        return new AStarResult(false, nodesVisited, 0.0, elapsedMs, exploredNodes, Collections.emptyList());
    }

    private static List<Node> reconstructPath(Map<Node, Node> cameFrom, Node targetNode, Node startNode) {
        List<Node> path = new ArrayList<>();
        Node curr = cameFrom.get(targetNode);
        while (curr != null && !curr.equals(startNode)) {
            path.add(curr);
            curr = cameFrom.get(curr);
        }
        Collections.reverse(path);
        return path;
    }
}