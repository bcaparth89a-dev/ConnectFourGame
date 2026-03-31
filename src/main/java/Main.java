import java.util.Scanner;

public class Main {

    // Board size
    static int rows = 6;
    static int cols = 7;

    // Game board
    static char[][] board = new char[rows][cols];

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // Fill board with empty spaces
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j] = '.';
            }
        }

        char player = 'X';  // First player
        boolean gameOver = false;

        while (!gameOver) {

            // Print board
            printBoard();

            // Ask user input
            System.out.print("Player " + player + ", choose column (0-6): ");
            int col = sc.nextInt();

            // Check valid input
            if (col < 0 || col >= cols) {
                System.out.println("Wrong column! Try again.");
                continue;
            }

            // Drop piece
            boolean placed = false;

            for (int i = rows - 1; i >= 0; i--) {
                if (board[i][col] == '.') {
                    board[i][col] = player;
                    placed = true;
                    break;
                }
            }

            // If column full
            if (!placed) {
                System.out.println("Column is full! Try another.");
                continue;
            }

            // Check win
            if (checkWin(player)) {
                printBoard();
                System.out.println("Player " + player + " wins!");
                gameOver = true;
                break;
            }

            // Check draw
            if (isFull()) {
                printBoard();
                System.out.println("Game Draw!");
                gameOver = true;
                break;
            }

            // Switch player
            if (player == 'X') {
                player = 'O';
            } else {
                player = 'X';
            }
        }

        sc.close();
    }

    // Print board
    static void printBoard() {
        System.out.println();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println("0 1 2 3 4 5 6");
        System.out.println();
    }

    // Check if board is full
    static boolean isFull() {
        for (int j = 0; j < cols; j++) {
            if (board[0][j] == '.') {
                return false;
            }
        }
        return true;
    }

    // Check win
    static boolean checkWin(char p) {

        // Horizontal
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols - 3; j++) {
                if (board[i][j] == p &&
                        board[i][j+1] == p &&
                        board[i][j+2] == p &&
                        board[i][j+3] == p) {
                    return true;
                }
            }
        }

        // Vertical
        for (int i = 0; i < rows - 3; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[i][j] == p &&
                        board[i+1][j] == p &&
                        board[i+2][j] == p &&
                        board[i+3][j] == p) {
                    return true;
                }
            }
        }

        // Diagonal down-right
        for (int i = 0; i < rows - 3; i++) {
            for (int j = 0; j < cols - 3; j++) {
                if (board[i][j] == p &&
                        board[i+1][j+1] == p &&
                        board[i+2][j+2] == p &&
                        board[i+3][j+3] == p) {
                    return true;
                }
            }
        }

        // Diagonal up-right
        for (int i = 3; i < rows; i++) {
            for (int j = 0; j < cols - 3; j++) {
                if (board[i][j] == p &&
                        board[i-1][j+1] == p &&
                        board[i-2][j+2] == p &&
                        board[i-3][j+3] == p) {
                    return true;
                }
            }
        }

        return false;
    }
}