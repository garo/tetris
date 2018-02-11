package net.juhonkoti;

public class TetrisCLI implements TetrisUserInterface {

    private TetrisGame game;

    public TetrisCLI(TetrisGame game) {
        this.game = game;
    }

    @Override
    public void draw() {

        // Draw top edge
        System.out.print('+');
        for (int i = 0; i < game.width; i++) {
            System.out.print('-');
        }
        System.out.println('+');

        // Draw actual board with contents
        for (int y = 0; y < game.height; y++) {
            System.out.print('|');
            for (int x = 0; x < game.width; x++) {
                System.out.print(blockToPrintable(game.getXY(x, y)));
            }
            System.out.println('|');
        }

        // Draw bottom edge
        System.out.print('+');
        for (int i = 0; i < game.width; i++) {
            System.out.print('-');
        }
        System.out.println('+');

    }

    private char blockToPrintable(int block) {
        switch (block) {
            case 0:
                return ' ';
            case 1:
                return 'X';
        }
        return ' ';
    }

}
