package net.juhonkoti;

public class TetrisGame {

    // Board dimensions
    final int width = 10;
    final int height = 20;

    final int[][] stick = {
            {0, 0, 0, 0},
            {1, 1, 1, 1},
            {0, 0, 0, 0},
            {0, 0, 0, 0}
    };

    final int[][] square = {
            {2, 2},
            {2, 2}
    };

    final int[][] tblock = {
            {0, 3, 0},
            {3, 3, 3},
            {0, 0, 0}
    };

    final int[][] sblock = {
            {0, 4, 0},
            {0, 4, 4},
            {0, 0, 4}
    };

    final int[][] zblock = {
            {0, 0, 5},
            {0, 5, 5},
            {0, 5, 0}
    };

    final int[][] lblock = {
            {6, 6, 0},
            {0, 6, 0},
            {0, 6, 0}
    };

    final int[][] jblock = {
            {0, 7, 7},
            {0, 7, 0},
            {0, 7, 0}
    };

    final int[][] testblock = {
            {1, 2, 3},
            {4, 0, 5},
            {6, 7, 0}
    };

    // List of block options.
    final int[][][] blocks = {stick, square, tblock, sblock, zblock, lblock, jblock};

    // Buffer of the current player block
    // We assume that the block dimensions are always equal (ie. square)
    protected int[][] currentBlock;

    // And the position of the current block, anchored to the
    // top-left xy coordinates of the block
    protected int current_x;
    protected int current_y;

    // Board in x/y dimensions containing all static blocks. Top left is [0][0]
    protected int[][] board;

    // Render buffer. Used to store the representation of all blocks for rendering to screen.
    protected int[][] renderBoard;

    private boolean game_over = false;

    public TetrisGame() {
        board = new int[width][height];
        renderBoard = new int[width][height];
    }

    public int getXY(int x, int y) {
        return renderBoard[x][y];
    }

    public void spawn() {
        // Because currentBlock is rotated in-place, so we need to do a deep copy from it.
        currentBlock = copyBlock(blocks[(int)Math.floor(Math.random() * blocks.length)]);

        current_x = width / 2;
        current_y = 1;
        renderBlock();

    }

    /**
     * Moves the currentBlock left or right.
     * @param direction_x -1 for left, 1 for right side movement.
     */
    public void move(int direction_x) {
        if (canMove(currentBlock, direction_x, 0)) {
            current_x += direction_x;
        }

        checkForCompletedLine();
        renderStaticBoard();
        renderBlock();
    }

    /**
     * Advances the currentBlock down one unit. Also verifies if the game has ended and turns
     * the game_over flag on in this case.
     * @return
     */
    public boolean advance() {
        if (!canMove(currentBlock,0,0)) {
            System.out.println("Game over!");
            game_over = true;
            return true;
        }

        if (canMove(currentBlock,0, 1)) {
            current_y++;
        } else {
            freezeBlock();
            spawn();
        }

        checkForCompletedLine();
        renderStaticBoard();
        renderBlock();
        return false;
    }

    public boolean isGameOver() {
        return game_over;
    }

    /**
     * Rotates the currentBlock, if possible
     */
    public void rotate() {
        int[][] block = rotateBlock(currentBlock);
        if (canMove(block, 0, 0)) {
            currentBlock = block;
            checkForCompletedLine();
            renderStaticBoard();
            renderBlock();
        }
    }

    /**
     * Moves the block down as far as it is possible to go in one move.
     */
    public void down() {
        while (canMove(currentBlock,0, 1)) {
            current_y++;
        }

        checkForCompletedLine();
        renderStaticBoard();
        renderBlock();
    }

    /**
     * Returns true if the currentBlock can be moved towards the direction coordinates.
     *
     * @param direction_x -1, 0 or 1
     * @param direction_y 0 or 1
     * @return
     */
    public boolean canMove(int[][] block, int direction_x, int direction_y) {
        for (int y = current_y; y < current_y + block.length; y++) {
            for (int x = current_x; x < current_x + block.length; x++) {
                if (block[x - current_x][y - current_y] == 0) {
                    continue;
                }
                if (x + direction_x >= width || y + direction_y >= height)
                    return false;
                if (x + direction_x < 0 || y + direction_y < 0)
                    return false;

                if (board[x + direction_x][y + direction_y] != 0 &&
                        block[x - current_x][y - current_y] != 0) {
                    return false;
                }

            }
        }

        return true;
    }

    /**
     * Verifies if any of the game lines are completed. Completed lines
     * are removed and the lines above that are dropped down.
     */
    public void checkForCompletedLine() {
        for (int y = height - 1; y >= 0; y--) {
            boolean completed = true;
            for (int x = 0; x < width; x++) {
                if (board[x][y] == 0) {
                    completed = false;
                }
            }

            if (completed) {
                System.out.println("Line " + y + " is completed");
                for (int i = y; i > 0; i--) {
                    for (int x = 0; x < width; x++) {
                        board[x][i] = board[x][i - 1];
                    }
                }

                // This makes the next loop iteration to re-check current line
                y++;
            }
        }
    }





    // {0, 0, D},    0, 0, 0     0, A, 0     D, C, 0
    // {0, B, C},    A, B, 0     C, B, 0     0, B, A
    // {0, A, 0}     0, C, D     D, 0, 0     0, 0, 0

    // 1 2 3     7 8 1    (0,0) (1,0) (2,0)       (0,2) (0,1) (0,0)
    // 8   4     6 0 2    (0,1) (   ) (2,1)       (1,2) (   ) (1,0)
    // 7 6 5     5 4 3    (0,2) (1,2) (2,2)       (2,2) (2,1) (2,0)
    public int[][] rotateBlock(int[][] block) {
        int[][] rotated = new int[block.length][block.length];

        if (block.length == 2) {
            return block;
        } else if (block.length == 3) {
            rotated[0][0] = block[0][2];
            rotated[1][0] = block[0][1];
            rotated[2][0] = block[0][0];
            rotated[2][1] = block[1][0];
            rotated[2][2] = block[2][0];
            rotated[1][2] = block[2][1];
            rotated[0][2] = block[2][2];
            rotated[0][1] = block[1][2];
            rotated[1][1] = block[1][1];
        } else if (block.length == 4) {
            for (int y = 0; y < block.length; y++) {
                for (int x = 0; x < block.length; x++) {
                    rotated[y][x] = block[x][y];
                }
            }
        }

        return rotated;
    }

    /**
     * Renders the static board to the renderBoard
     */
    public void renderStaticBoard() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                renderBoard[x][y] = board[x][y];
            }
        }
    }

    /**
     * Renders the currentBlock to the renderBoard
     */
    public void renderBlock() {
        for (int y = current_y; y < current_y + currentBlock.length; y++) {
            for (int x = current_x; x < current_x + currentBlock.length; x++) {
                if (currentBlock[x - current_x][y - current_y] > 0)
                    renderBoard[x][y] = currentBlock[x - current_x][y - current_y];
            }
        }
    }

    /**
     * Commits the currentBlock to the static board
     */
    public void freezeBlock() {
        for (int y = current_y; y < current_y + currentBlock.length; y++) {
            for (int x = current_x; x < current_x + currentBlock.length; x++) {
                if (currentBlock[x - current_x][y - current_y] > 0)
                    board[x][y] = currentBlock[x - current_x][y - current_y];
            }
        }
    }

    /**
     * Copies does a deep copy of the block passed as argument and returns the copy.
     * @param block
     * @return
     */
    public int[][] copyBlock(int[][] block) {
        int[][] copy = new int[block.length][block.length];
        for (int y = 0; y < block.length; y++) {
            for (int x = 0; x < block.length; x++) {
                copy[x][y] = block[x][y];
            }
        }

        return copy;
    }

}

