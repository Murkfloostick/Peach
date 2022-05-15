package nl.windesheim.ictm2o.peach.Backtracking;

public class NQueens {

    public static void main(String[] args) {
        int n = 6;
        nQueens(n);
    }

    private static void nQueens(int noOfQueens) {
        int [] board = new int[noOfQueens];
        placeQueen(board, 0, noOfQueens);
    }

    private static void placeQueen(int[] board, int current, int noOfQueens) {
        if (current == noOfQueens) {
            displayQueens(board);
            return;
        }

        for (int i = 0; i < noOfQueens; i++) {
            board[current] = i;
            if (noKill(board, current)) {
                placeQueen(board, current + 1, noOfQueens);
            }
        }
    }

    private static boolean noKill(int[] board, int currentColumnOfQueen) {

        for (int i = 0; i < currentColumnOfQueen; i++) {
            // same row
            if (board[i] == board[currentColumnOfQueen])
                return false;

            // Diagonal
            if ((currentColumnOfQueen - i) == Math.abs(board[currentColumnOfQueen] - board[i])) {
                return false;
            }
        }
        return true;
    }

    private static void displayQueens(int[] board) {
        System.out.print("\n");

        for (int value : board)
            System.out.printf(value + "%3s" ," ");

        System.out.print("\n\n");

        int n = board.length;

        for (int i = 0; i < n; i++) {
            for (int value : board) {
                if (value == i)
                    System.out.print("Q\t");
                else
                    System.out.print("*\t");
            }
            System.out.print("\n");
        }
    }
}
