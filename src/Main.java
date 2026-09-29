package pathfinder;

import pathfinder.algorithms.AStarResult;
import pathfinder.algorithms.AlgorithmType;
import pathfinder.algorithms.PathfinderAlgorithms;
import pathfinder.core.AppState;
import pathfinder.core.Grid;
import pathfinder.core.MetricsTracker;
import pathfinder.core.Node;
import pathfinder.ui.Colors;
import pathfinder.ui.GridPanel;
import pathfinder.ui.SidePanel;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {

    private final Grid grid;
    private final MetricsTracker metrics;
    private final GridPanel gridPanel;
    private final SidePanel sidePanel;
    private Timer animationTimer;

    public Main() {
        int gridUnits = 25;
        int cellSize = 28;

        this.grid = new Grid(gridUnits);
        this.metrics = new MetricsTracker();

        setTitle("Pathfinder Pro - Decision Strategy Analyzer (Native)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(Colors.BG_DARK);
        setLayout(new BorderLayout());

        this.gridPanel = new GridPanel(grid, cellSize, this::updateStatusFromGrid);
        this.sidePanel = new SidePanel(metrics, this::startPathfinding, this::clearSearch, this::resetMap);

        add(gridPanel, BorderLayout.CENTER);
        add(sidePanel, BorderLayout.EAST);

        pack();
        setLocationRelativeTo(null);
    }

    private void updateStatusFromGrid() {
        if (grid.getStartNode() == null) {
            sidePanel.setStatusText("Defina o ponto A");
        } else if (grid.getTargetNode() == null) {
            sidePanel.setStatusText("Defina o ponto B");
        } else {
            sidePanel.setStatusText("Pronto para executar");
        }
    }

    private void startPathfinding() {
        if (grid.getStartNode() == null || grid.getTargetNode() == null) {
            sidePanel.setStatusText("Defina Ponto A e B!");
            return;
        }

        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }

        grid.clearPathAndExplored();
        sidePanel.setStatusText("A processar rota...");

        AlgorithmType selectedAlgo = sidePanel.getSelectedAlgorithm();
        AStarResult result = PathfinderAlgorithms.run(grid, selectedAlgo);

        int delay = sidePanel.getAnimationDelayMs();

        if (delay == 0) {
            for (Node node : result.exploredNodes) {
                node.setState(AppState.EXPLORED);
            }
            for (Node node : result.path) {
                node.setState(AppState.PATH);
            }
            finishSearch(result);
        } else {
            final int[] exploredIdx = {0};
            final int[] pathIdx = {0};

            animationTimer = new Timer(delay, e -> {
                if (exploredIdx[0] < result.exploredNodes.size()) {
                    Node node = result.exploredNodes.get(exploredIdx[0]++);
                    node.setState(AppState.EXPLORED);
                } else if (pathIdx[0] < result.path.size()) {
                    Node node = result.path.get(pathIdx[0]++);
                    node.setState(AppState.PATH);
                } else {
                    ((Timer) e.getSource()).stop();
                    finishSearch(result);
                }
                gridPanel.repaint();
            });
            animationTimer.start();
        }
    }

    private void finishSearch(AStarResult result) {
        metrics.update(result.executionTimeMs, result.nodesVisited, result.pathCost);
        if (result.success) {
            sidePanel.setStatusText("Rota Encontrada!");
        } else {
            sidePanel.setStatusText("Caminho Inexistente!");
        }
        gridPanel.repaint();
        sidePanel.repaint();
    }

    private void clearSearch() {
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }
        grid.clearPathAndExplored();
        metrics.reset();
        updateStatusFromGrid();
        gridPanel.repaint();
        sidePanel.repaint();
    }

    private void resetMap() {
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }
        grid.resetAll();
        metrics.reset();
        sidePanel.setStatusText("Defina o ponto A");
        gridPanel.repaint();
        sidePanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main app = new Main();
            app.setVisible(true);
        });
    }
}