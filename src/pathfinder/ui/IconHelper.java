package pathfinder.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;

public class IconHelper {

    public static void drawStartIcon(Graphics2D g2, int x, int y, int size) {
        Graphics2D g = (Graphics2D) g2.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(new Color(56, 189, 248, 50));
        g.fillOval(x - 2, y - 2, size + 4, size + 4);

        int pad = size / 4;
        Path2D triangle = new Path2D.Double();
        triangle.moveTo(x + pad + 2, y + pad);
        triangle.lineTo(x + size - pad + 2, y + size / 2.0);
        triangle.lineTo(x + pad + 2, y + size - pad);
        triangle.closePath();

        g.setColor(Colors.ACCENT_CYAN);
        g.fill(triangle);
        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke(1.5f));
        g.draw(triangle);

        g.dispose();
    }

    public static void drawTargetIcon(Graphics2D g2, int x, int y, int size) {
        Graphics2D g = (Graphics2D) g2.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int cx = x + size / 2;
        int cy = y + size / 2;
        int r1 = size / 2 - 2;
        int r2 = size / 3;
        int r3 = Math.max(2, size / 6);

        g.setColor(new Color(244, 63, 94, 50));
        g.fillOval(cx - r1 - 2, cy - r1 - 2, (r1 + 2) * 2, (r1 + 2) * 2);

        g.setColor(Colors.ACCENT_RED);
        g.setStroke(new BasicStroke(2.0f));
        g.drawOval(cx - r1, cy - r1, r1 * 2, r1 * 2);
        g.drawOval(cx - r2, cy - r2, r2 * 2, r2 * 2);

        g.setColor(Color.WHITE);
        g.fillOval(cx - r3, cy - r3, r3 * 2, r3 * 2);

        g.dispose();
    }

    public static void drawWallIcon(Graphics2D g2, int x, int y, int size) {
        Graphics2D g = (Graphics2D) g2.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(Colors.WALL_COLOR);
        g.fillRect(x + 1, y + 1, size - 1, size - 1);

        g.setColor(new Color(255, 255, 255, 25));
        g.drawLine(x + 1, y + size / 2, x + size - 1, y + size / 2);
        g.drawLine(x + size / 2, y + 1, x + size / 2, y + size / 2);
        g.drawLine(x + size / 4, y + size / 2, x + size / 4, y + size - 1);
        g.drawLine(x + (3 * size) / 4, y + size / 2, x + (3 * size) / 4, y + size - 1);

        g.setColor(Colors.WALL_BORDER);
        g.drawRect(x + 1, y + 1, size - 2, size - 2);

        g.dispose();
    }

    public static ImageIcon createButtonIcon(String type, int size) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if ("RUN".equals(type)) {
            Path2D p = new Path2D.Double();
            p.moveTo(3, 2);
            p.lineTo(size - 2, size / 2.0);
            p.lineTo(3, size - 2);
            p.closePath();
            g2.setColor(Color.WHITE);
            g2.fill(p);
        } else if ("CLEAR".equals(type)) {
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2.0f));
            g2.drawArc(2, 2, size - 4, size - 4, 45, 280);
            g2.drawLine(size - 4, 2, size - 1, 6);
        } else if ("RESET".equals(type)) {
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(1.8f));
            g2.drawRect(2, 2, size - 5, size - 5);
            g2.drawLine(2, 2, size - 3, size - 3);
        }

        g2.dispose();
        return new ImageIcon(img);
    }
}