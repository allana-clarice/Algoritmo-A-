package pathfinder.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ModernButton extends JButton {
    private final boolean isPrimary;
    private boolean isHovered = false;
    private boolean isPressed = false;

    public ModernButton(String text, Icon icon, boolean isPrimary) {
        super(text, icon);
        this.isPrimary = isPrimary;
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFont(new Font("Segoe UI", Font.BOLD, 13));
        setForeground(Colors.TEXT_PRIMARY);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setIconTextGap(10);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                isPressed = false;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                isPressed = true;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int yOffset = isPressed ? 2 : 0;

        Color startColor;
        Color endColor;
        Color borderColor;

        if (isPrimary) {
            if (isHovered) {
                startColor = new Color(37, 99, 235);
                endColor = new Color(79, 70, 229);
                borderColor = new Color(147, 197, 253);
            } else {
                startColor = new Color(29, 78, 216);
                endColor = new Color(67, 56, 202);
                borderColor = new Color(96, 165, 250);
            }
        } else {
            if (isHovered) {
                startColor = new Color(30, 41, 59);
                endColor = new Color(51, 65, 85);
                borderColor = new Color(100, 116, 139);
            } else {
                startColor = new Color(15, 23, 42);
                endColor = new Color(30, 41, 59);
                borderColor = new Color(51, 65, 85);
            }
        }

        if (!isPressed) {
            g2.setColor(new Color(0, 0, 0, 80));
            g2.fillRoundRect(0, 3, width, height - 3, 10, 10);
        }

        GradientPaint gradient = new GradientPaint(0, yOffset, startColor, 0, yOffset + height - 3, endColor);
        g2.setPaint(gradient);
        g2.fillRoundRect(0, yOffset, width, height - 3, 10, 10);

        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(1.2f));
        g2.drawRoundRect(0, yOffset, width - 1, height - 4, 10, 10);

        g2.dispose();

        super.paintComponent(g);
    }
}