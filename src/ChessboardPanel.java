public class ChessboardPanel  {
    //This is the Backend Solution of the N queens
    public boolean solvePuzzle(int[][] positions) {
        return validRow(positions) && validCol(positions) && validDiagonal(positions);
    }

    public boolean validRow(int[][] positions) {
        for (int row = 0; row < 8; row++) {
            int queenCount = 0;
            for (int col = 0; col < 8; col++) {
                if (positions[row][col] == 1) { // if there is a queen at this position
                    queenCount++; // increment queenCount
                }
            }
            if (queenCount > 1) {
                return false; // if there is more than 1 queen in a row, return false
            }
        }
        return true; // if there is not more than 1 queen in a row, return true
    }

    public boolean validCol(int[][] positions) {
        for (int col = 0; col < 8; col++) {
            int queenCount = 0;
            for (int row = 0; row < 8; row++) {
                if (positions[row][col] == 1) {
                    queenCount++;
                }
            }
            if (queenCount > 1) {
                return false;
            }
        }
        return true;
    }


    public boolean validDiagonal(int[][] board) {
        int size = board.length;

        // Check main diagonals (top-left to bottom-right)
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (board[row][col] == 1) {
                    if (isDiagonalCollision(board, row, col, 1, 1) || // Check lower right
                            isDiagonalCollision(board, row, col, -1, -1)) { // Check upper left
                        return false;
                    }
                }
            }
        }

        // Check secondary diagonals (top-right to bottom-left)
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (board[row][col] == 1) {
                    if (isDiagonalCollision(board, row, col, 1, -1) || // Check lower left
                            isDiagonalCollision(board, row, col, -1, 1)) { // Check upper right
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private static boolean isDiagonalCollision(int[][] board, int row, int col, int rowIncrement, int colIncrement) {
        int size = board.length;

        while (row + rowIncrement >= 0 && row + rowIncrement < size && col + colIncrement >= 0 && col + colIncrement < size) {
            row += rowIncrement;
            col += colIncrement;

            if (board[row][col] == 1) {
                return true; // There is a collision on the diagonal
            }
        }

        return false; // No collision on the diagonal
    }
}