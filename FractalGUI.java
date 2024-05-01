import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Hauptklasse, die ein Fenster zur Darstellung verschiedener Fraktale bereitstellt.
 * Die GUI ist mittels Java Swing umgesetzt.
 * @author Bernhard Aichinger-Ganas
 * @version 2024-05-01
 */
public class FractalGUI extends JFrame {
    private final JPanel panel;
    private final JButton cantorButton, sierpinskiButton, kochButton, treeButton;
    private Runnable currentDrawing;

    /**
     * Konstruktor, der das Hauptfenster initialisiert und konfiguriert.
     */
    public FractalGUI() {
        super("Fraktale mit Java Swing");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1024, 768);
        setLocationRelativeTo(null);
        setResizable(true);

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (currentDrawing != null) {
                    currentDrawing.run();
                }
            }
        };
        panel.setBackground(Color.WHITE);
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                clearPanel();
                if (currentDrawing != null) {
                    currentDrawing.run();
                }
            }
        });

        // Buttons zur Auswahl des darzustellenden Fraktals
        cantorButton = new JButton("Cantor Set");
        sierpinskiButton = new JButton("Sierpinski Dreieck");
        kochButton = new JButton("Kochkurve");
        treeButton = new JButton("Rekursiver Baum");

        // Event-Handler für die Buttons, die jeweilige Fraktal-Zeichnungen initiieren
        cantorButton.addActionListener(e -> updateCurrentDrawing(() -> drawCantorSet(panel.getGraphics(), 20, 30, panel.getWidth() - 40)));
        sierpinskiButton.addActionListener(e -> updateCurrentDrawing(() -> drawSierpinskiTriangle(panel.getGraphics(), 20, 250, panel.getWidth() - 40, 300)));
        kochButton.addActionListener(e -> updateCurrentDrawing(() -> drawKochCurve(panel.getGraphics(), 20, 400, panel.getWidth() - 40, 5)));
        treeButton.addActionListener(e -> updateCurrentDrawing(() -> drawTree(panel.getGraphics(), panel.getWidth() / 2, panel.getHeight() - 50, -90, 120)));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(cantorButton);
        buttonPanel.add(sierpinskiButton);
        buttonPanel.add(kochButton);
        buttonPanel.add(treeButton);

        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.NORTH);
    }

    /**
     * Setzt das aktuelle Zeichen-Runnable und zeichnet es.
     *
     * @param drawAction Die Zeichenaktion, die aktualisiert und ausgeführt werden soll.
     */
    private void updateCurrentDrawing(Runnable drawAction) {
        currentDrawing = drawAction;
        clearPanel();
        drawAction.run();
    }

    /**
     * Leert das Panel, indem der Hintergrund übermalt wird.
     */
    private void clearPanel() {
        Graphics g = panel.getGraphics();
        g.setColor(panel.getBackground());
        g.fillRect(0, 0, panel.getWidth(), panel.getHeight());
    }

    // Methoden zum Zeichnen verschiedener Fraktale folgen hier:

    /**
     * Zeichnet ein Cantor Set rekursiv.
     */
    private void drawCantorSet(Graphics g, int x, int y, int length) {
        if (length < 1) return;
        g.drawLine(x, y, x + length, y);
        y += 20;
        drawCantorSet(g, x, y, length / 3);
        drawCantorSet(g, x + 2 * length / 3, y, length / 3);
    }

    /**
     * Zeichnet rekursiv ein Sierpinski-Dreieck.
     */
    private void drawSierpinskiTriangle(Graphics g, int x, int y, int size, int height) {
        if (size < 5) return;
        int[] xPoints = {x, x + size / 2, x + size};
        int[] yPoints = {y + height, y, y + height};
        g.drawPolygon(xPoints, yPoints, 3);
        drawSierpinskiTriangle(g, x, y + height / 2, size / 2, height / 2);
        drawSierpinskiTriangle(g, x + size / 2, y, size / 2, height / 2);
        drawSierpinskiTriangle(g, x + size, y + height / 2, size / 2, height / 2);
    }

    /**
     * Zeichnet eine Kochkurve.
     */
    private void drawKochCurve(Graphics g, int x, int y, int length, int depth) {
        if (depth == 0) {
            g.drawLine(x, y, x + length, y);
        } else {
            int segment = length / 3;
            drawKochCurve(g, x, y, segment, depth - 1);
            int x1 = x + segment;
            int y1 = y;
            int x2 = x + segment + (int)(segment * Math.cos(Math.toRadians(60)));
            int y2 = y - (int)(segment * Math.sin(Math.toRadians(60)));
            drawKochCurve(g, x1, y1, segment, depth - 1);
            drawKochCurve(g, x2, y2, segment, depth - 1);
            drawKochCurve(g, x + 2 * segment, y, segment, depth - 1);
        }
    }

    /**
     * Zeichnet einen rekursiven Baum.
     */
    private void drawTree(Graphics g, int x, int y, double angle, double length) {
        if (length < 5) return;
        int xEnd = x + (int) (Math.cos(Math.toRadians(angle)) * length);
        int yEnd = y + (int) (Math.sin(Math.toRadians(angle)) * length);
        g.drawLine(x, y, xEnd, yEnd);
        drawTree(g, xEnd, yEnd, angle - 20, length * 0.8);
        drawTree(g, xEnd, yEnd, angle + 20, length * 0.8);
    }

    /**
     * Startet die GUI über die Ereignisverarbeitungsschleife von Swing.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FractalGUI().setVisible(true));
    }
}
