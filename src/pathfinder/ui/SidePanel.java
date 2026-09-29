package pathfinder.ui;

import pathfinder.algorithms.AlgorithmType;
import pathfinder.core.MetricsTracker;

import javax.swing.*;
import java.awt.*;

public class SidePanel extends JPanel {
    private final MetricsTracker metrics;
    private String statusText = "Defina o ponto A";

    private final JComboBox<AlgorithmType> algoComboBox;
    private final JSlider speedSlider;
    private final ModernButton btnRun;
    private final ModernButton btnClear;
    private final ModernButton btnReset;

    public SidePanel(MetricsTracker metrics, Runnable onRun, Runnable onClear, Runnable onReset) {
        this.metrics = metrics;
        setLayout(null);
        setBackground(Colors.BG_DARK);
        setPreferredSize(new Dimension(350, 730));

        algoComboBox = new JComboBox<>(AlgorithmType.values());
        algoComboBox.setBounds(25, 125, 300, 35);
        algoComboBox.setFont(new Font("Segoe UI", Font.BOLD, 12));
        algoComboBox.setBackground(Colors.CARD_BG);
        algoComboBox.setForeground(Colors.TEXT_PRIMARY);
        algoComboBox.setFocusable(false);
        add(algoComboBox);

        speedSlider = new JSlider(0, 50, 10);
        speedSlider.setBounds(25, 195, 300, 30);
        speedSlider.setBackground(Colors.PANEL_BG);
        speedSlider.setForeground(Colors.ACCENT_CYAN);
        speedSlider.setFocusable(false);
        add(speedSlider);

        btnRun = new ModernButton("EXECUTAR BUSCA", IconHelper.createButtonIcon("RUN", 12), true);
        btnRun.setBounds(25, 575, 300, 42);
        btnRun.addActionListener(e -> onRun.run());

        btnClear = new ModernButton("LIMPAR BUSCA", IconHelper.createButtonIcon("CLEAR", 12), false);
        btnClear.setBounds(25, 625, 300, 38);
        btnClear.addActionListener(e -> onClear.run());

        btnReset = new ModernButton("NOVO MAPA", IconHelper.createButtonIcon("RESET", 12), false);
        btnReset.setBounds(25, 670, 300, 38);
        btnReset.addActionListener(e -> onReset.run());

        add(btnRun);
        add(btnClear);
        add(btnReset);
    }

    public AlgorithmType getSelectedAlgorithm() {
        return (AlgorithmType) algoComboBox.getSelectedItem();
    }

    public int getAnimationDelayMs() {
        return speedSlider.getValue();
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int panelX = 10;
        int panelY = 10;
        int panelW = 330;
        int panelH = 710;

        g2.setColor(Colors.PANEL_BG);
        g2.fillRoundRect(panelX, panelY, panelW, panelH, 18, 18);
        g2.setColor(Colors.PANEL_BORDER);
        g2.setStroke(new BasicStroke(1.2f));
        g2.drawRoundRect(panelX, panelY, panelW, panelH, 18, 18);

        g2.setColor(Colors.TEXT_PRIMARY);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 20));
        g2.drawString("PATHFINDER", panelX + 15, panelY + 32);

        g2.setColor(Colors.ACCENT_CYAN);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
        g2.drawString("PRO NATIVE v2.0", panelX + 15, panelY + 48);

        g2.setColor(Colors.PANEL_BORDER);
        g2.drawLine(panelX + 15, panelY + 60, panelX + panelW - 15, panelY + 60);

        g2.setColor(Colors.TEXT_SECONDARY);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
        g2.drawString("ALGORITMO DE BUSCA", panelX + 15, panelY + 78);
        g2.drawString("ATRASO DA ANIMAÇÃO (MS)", panelX + 15, panelY + 148);

        g2.drawString("DESEMPENHO", panelX + 15, panelY + 222);
        drawStatCard(g2, panelX + 15, panelY + 232, 300, 48, "Tempo", String.format("%.3f", metrics.getExecutionTimeMs()), "ms", Colors.ACCENT_CYAN);
        drawStatCard(g2, panelX + 15, panelY + 288, 145, 48, "Nós Vis.", String.valueOf(metrics.getNodesVisited()), "", Colors.ACCENT_GREEN);
        drawStatCard(g2, panelX + 170, panelY + 288, 145, 48, "Custo Rota", String.format("%.1f", metrics.getPathCost()), "", Colors.ACCENT_PURPLE);

        g2.drawString("STATUS", panelX + 15, panelY + 354);
        g2.setColor(Colors.CARD_BG);
        g2.fillRoundRect(panelX + 15, panelY + 364, 300, 38, 10, 10);
        g2.setColor(Colors.CARD_BORDER);
        g2.drawRoundRect(panelX + 15, panelY + 364, 300, 38, 10, 10);

        g2.setColor(Colors.ACCENT_CYAN);
        g2.fillOval(panelX + 28, panelY + 379, 8, 8);

        g2.setColor(Colors.TEXT_PRIMARY);
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        g2.drawString(statusText, panelX + 44, panelY + 388);

        g2.setColor(Colors.TEXT_SECONDARY);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
        g2.drawString("LEGENDA", panelX + 15, panelY + 422);

        int lyStart = panelY + 438;

        IconHelper.drawStartIcon(g2, panelX + 18, lyStart, 16);
        g2.setColor(Colors.TEXT_SECONDARY);
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        g2.drawString("Início (A)", panelX + 42, lyStart + 13);

        IconHelper.drawTargetIcon(g2, panelX + 18, lyStart + 22, 16);
        g2.drawString("Destino (B)", panelX + 42, lyStart + 35);

        g2.setColor(Colors.EXPLORED_COLOR);
        g2.fillRect(panelX + 18, lyStart + 46, 16, 16);
        g2.setColor(Colors.TEXT_SECONDARY);
        g2.drawString("Explorado", panelX + 42, lyStart + 59);

        g2.setColor(Colors.PATH_COLOR);
        g2.fillRect(panelX + 18, lyStart + 68, 16, 16);
        g2.setColor(Colors.TEXT_SECONDARY);
        g2.drawString("Caminho", panelX + 42, lyStart + 81);

        IconHelper.drawWallIcon(g2, panelX + 18, lyStart + 90, 16);
        g2.setColor(Colors.TEXT_SECONDARY);
        g2.drawString("Obstáculo", panelX + 42, lyStart + 103);

        g2.dispose();
    }

    private void drawStatCard(Graphics2D g2, int x, int y, int w, int h, String title, String value, String unit, Color accent) {
        g2.setColor(Colors.CARD_BG);
        g2.fillRoundRect(x, y, w, h, 10, 10);
        g2.setColor(Colors.CARD_BORDER);
        g2.drawRoundRect(x, y, w, h, 10, 10);

        g2.setColor(accent);
        g2.fillRoundRect(x, y + 5, 4, h - 10, 2, 2);

        g2.setColor(Colors.TEXT_MUTED);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
        g2.drawString(title.toUpperCase(), x + 12, y + 15);

        g2.setColor(Colors.TEXT_PRIMARY);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
        String valStr = unit.isEmpty() ? value : value + " " + unit;
        g2.drawString(valStr, x + 12, y + 34);
    }
}