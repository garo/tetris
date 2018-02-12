import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Miminum example how to create a graphical UI with draw functionality,
 * the ability to print text onto the screen and to listen for key events.
 */
public class Example extends JFrame implements KeyListener {

    private class DrawCanvas extends JPanel {

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(Color.BLACK);

            g.setColor(Color.BLUE);
            g.fillRect(50, 50, 30, 30);

            g.setFont(new Font("Verdana", Font.PLAIN,12));
            g.setColor(Color.WHITE);
            g.drawString("Hello, World!", 40, 30);
        }
    }

    public void keyPressed(KeyEvent e) {
        System.out.println("Key pressed: " + e.getKeyCode());
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {

    }

    private DrawCanvas canvas;

    public Example() {

        canvas = new DrawCanvas();
        canvas.setPreferredSize(new Dimension(600, 600));

        Container cp = getContentPane();
        cp.add(canvas);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setTitle("Tetris!");
        setVisible(true);
        addKeyListener(this);

        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Example();
        });
    }
}
