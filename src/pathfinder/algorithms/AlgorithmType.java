package pathfinder.algorithms;

public enum AlgorithmType {
    GREEDY("Busca Gulosa (Greedy)"),
    ASTAR("A* (A-Star)");

    private final String displayName;

    AlgorithmType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}