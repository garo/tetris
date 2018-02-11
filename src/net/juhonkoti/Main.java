package net.juhonkoti;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Main extends JFrame implements KeyListener {
    final int BLOCK_SIZE = 30;

    private int currentKey = 0;
    private boolean pause = false;

    private class DrawCanvas extends JPanel {

        TetrisGame game;

        public DrawCanvas(TetrisGame game) {
            this.game = game;

        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(Color.BLACK);

            // Draw actual board with contents
            g.setColor(Color.BLUE);
            g.drawRect(0, 0, 10, 10);
            for (int y = 0; y < game.height; y++) {

                for (int x = 0; x < game.width; x++) {
                    int b = game.getXY(x, y);
                    Color color = Color.WHITE;
                    switch (b) {
                        case 0:
                            color = Color.BLACK;
                            break;
                        case 1:
                            color = Color.RED;
                            break;
                        case 2:
                            color = Color.GREEN;
                            break;
                        case 3:
                            color = Color.BLUE;
                            break;
                        case 4:
                            color = Color.CYAN;
                            break;
                        case 5:
                            color = Color.MAGENTA;
                            break;
                        case 6:
                            color = Color.YELLOW;
                            break;
                        case 7:
                            color = Color.pink;
                            break;

                    }
                    g.setColor(color);

                    // Draw the blocks one pixel too small so we get a nice grid lining
                    g.fillRect(x * BLOCK_SIZE, y * BLOCK_SIZE,
                            BLOCK_SIZE - 1, BLOCK_SIZE- 1);
                }
            }

            if (pause) {
                g.setFont(new Font("Verdana", Font.PLAIN,12));
                g.setColor(Color.WHITE);
                g.drawString("Pause! press p to unpause.", 40, game.height * BLOCK_SIZE / 2);

            } else if (game.isGameOver()) {
                g.setFont(new Font("Verdana", Font.PLAIN,22));
                g.setColor(Color.WHITE);
                g.drawString("Game over!", 80, game.height * BLOCK_SIZE / 4);

            }
        }
    }

    public void keyPressed(KeyEvent e) {
        currentKey = e.getKeyCode();
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {

    }

    private DrawCanvas canvas;

    public Main() {
        TetrisGame game = new TetrisGame();
        game.spawn();

        canvas = new DrawCanvas(game);
        canvas.setPreferredSize(new Dimension(game.width * BLOCK_SIZE,
                game.height * BLOCK_SIZE));

        Container cp = getContentPane();
        cp.add(canvas);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setTitle("Tetris!");
        setVisible(true);
        addKeyListener(this);

        Thread updateThread = new Thread() {
            @Override
            public void run() {
                int lastAdvance = 0;
                boolean game_over = false;
                do {
                    repaint();
                    try {
                        Thread.sleep(20);

                    } catch(Exception e) {}

                    // Handle key processing once per frame and then reset the currentKey so
                    // that the handler can store next key onto a clear table.
                    switch (currentKey) {
                        case KeyEvent.VK_LEFT:
                            game.move(-1);
                            break;
                        case KeyEvent.VK_RIGHT:
                            game.move(1);
                            break;
                        case KeyEvent.VK_UP:
                            game.rotate();
                            break;
                        case KeyEvent.VK_DOWN:
                        case KeyEvent.VK_SPACE:
                            game.down();
                            break;
                        case KeyEvent.VK_P:
                            pause = !pause;
                            break;
                    }
                    currentKey = 0;

                    if (!pause && lastAdvance++ > 10) {
                        lastAdvance = 0;
                        game_over = game.advance();
                    }


                } while(true);
            }
        };
        updateThread.start();

    }

    public static void main(String[] args) {
        new Testing();

        SwingUtilities.invokeLater(() -> {
            new Main();
        });
    }
}
