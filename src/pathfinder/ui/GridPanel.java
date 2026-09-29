package pathfinder.ui;

import pathfinder.core.AppState;
import pathfinder.core.Grid;
import pathfinder.core.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GridPanel extends JPanel {
    private final Grid grid;
    private final int cellSize;

    private boolean isDrawingWall = false;
    private boolean isErasingWall = false;
    private Point hoverCell = null;

    private final Runnable onStateChanged;

    public GridPanel(Grid grid, int cellSize, Runnable onStateChanged) {
        this.grid = grid;
        this.cellSize = cellSize;
        this.onStateChanged = onStateChanged;

        int dimension = grid.getSize() * cellSize + 20;
        setPreferredSize(new Dimension(dimension, dimension));
        setBackground(Colors.BG_DARK);

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleMouseClick(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isDrawingWall = false;
                isErasingWall = false;
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                handleMouseDrag(e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                updateHoverCell(e.getPoint());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hoverCell = null;
                repaint();
            }
        };

        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
    }

    private void updateHoverCell(Point p) {
        int col = (p.x - 10) / cellSize;
        int row = (p.y - 10) / cellSize;

        if (row >= 0 && row < grid.getSize() && col >= 0 && col < grid.getSize()) {
            hoverCell = new Point(col, row);
        } else {
            hoverCell = null;
        }
        repaint();
    }

    private void handleMouseClick(MouseEvent e) {
        int col = (e.getX() - 10) / cellSize;
        int row = (e.getY() - 10) / cellSize;

        if (row < 0 || row >= grid.getSize() || col < 0 || col >= grid.getSize()) {
            return;
        }

        Node node = grid.getNode(row, col);

        if (SwingUtilities.isLeftMouseButton(e)) {
            if (grid.getStartNode() == null) {
                grid.setStartNode(node);
                node.setState(AppState.START);
            } else if (grid.getTargetNode() == null && !node.equals(grid.getStartNode())) {
                grid.setTargetNode(node);
                node.setState(AppState.TARGET);
            } else if (!node.equals(grid.getStartNode()) && !node.equals(grid.getTargetNode())) {
                node.setState(AppState.WALL);
                isDrawingWall = true;
            }
        } else if (SwingUtilities.isRightMouseButton(e)) {
            if (node.equals(grid.getStartNode())) {
                grid.setStartNode(null);
                node.setState(AppState.EMPTY);
            } else if (node.equals(grid.getTargetNode())) {
                grid.setTargetNode(null);
                node.setState(AppState.EMPTY);
            } else {
                node.setState(AppState.EMPTY);
                isErasingWall = true;
            }
        }

        onStateChanged.run();
        repaint();
    }

    private void handleMouseDrag(MouseEvent e) {
        int col = (e.getX() - 10) / cellSize;
        int row = (e.getY() - 10) / cellSize;

        if (row < 0 || row >= grid.getSize() || col < 0 || col >= grid.getSize()) {
            return;
        }

        Node node = grid.getNode(row, col);
        if (node != null && !node.equals(grid.getStartNode()) && !node.equals(grid.getTargetNode())) {
            if (isDrawingWall) {
                node.setState(AppState.WALL);
            } else if (isErasingWall && node.getState() == AppState.WALL) {
                node.setState(AppState.EMPTY);
            }
        }

        updateHoverCell(e.getPoint());
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int gridPixelSize = grid.getSize() * cellSize;

        g2.setColor(Colors.GRID_BG);
        g2.fillRect(10, 10, gridPixelSize, gridPixelSize);

        g2.setColor(Colors.PANEL_BORDER);
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRect(10, 10, gridPixelSize, gridPixelSize);

        for (int r = 0; r < grid.getSize(); r++) {
            for (int c = 0; c < grid.getSize(); c++) {
                int x = 10 + c * cellSize;
                int y = 10 + r * cellSize;
                Node node = grid.getNode(r, c);

                if (node.getState() == AppState.WALL) {
                    IconHelper.drawWallIcon(g2, x, y, cellSize);
                } else if (node.getState() == AppState.EXPLORED) {
                    g2.setColor(Colors.EXPLORED_COLOR);
                    g2.fillRect(x + 1, y + 1, cellSize - 1, cellSize - 1);
                } else if (node.getState() == AppState.PATH) {
                    g2.setColor(Colors.PATH_COLOR);
                    g2.fillRect(x + 1, y + 1, cellSize - 1, cellSize - 1);
                }

                g2.setColor(Colors.GRID_LINE);
                g2.drawRect(x, y, cellSize, cellSize);
            }
        }

        if (hoverCell != null) {
            int hx = 10 + hoverCell.x * cellSize;
            int hy = 10 + hoverCell.y * cellSize;
            g2.setColor(new Color(255, 255, 255, 25));
            g2.fillRect(hx, hy, cellSize, cellSize);
        }

        if (grid.getStartNode() != null) {
            int sx = 10 + grid.getStartNode().getCol() * cellSize;
            int sy = 10 + grid.getStartNode().getRow() * cellSize;
            IconHelper.drawStartIcon(g2, sx, sy, cellSize);
        }

        if (grid.getTargetNode() != null) {
            int tx = 10 + grid.getTargetNode().getCol() * cellSize;
            int ty = 10 + grid.getTargetNode().getRow() * cellSize;
            IconHelper.drawTargetIcon(g2, tx, ty, cellSize);
        }

        g2.dispose();
    }
}