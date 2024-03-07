import java.util.Scanner;
import java.util.Random;

public class SudokuGame {
    private static final int SIZE = 9;
    private static final int SUBGRID_SIZE = 3;
    private static int[][] board = new int[SIZE][SIZE];
    private static int[][] solvedBoard = new int[SIZE][SIZE];
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        generatePuzzle();
        playGame();
    }

    private static void generatePuzzle() {
        // Generate a solved Sudoku board
        generateSolvedBoard();

        // Copy the solved board to create the puzzle
        copyBoard();

        // Remove some numbers to create a puzzle
        removeNumbers();
    }

    private static void generateSolvedBoard() {
        solveSudoku(solvedBoard);
    }

    private static void solveSudoku(int[][] board) {
        solveSudokuHelper(board);
    }

    private static boolean solveSudokuHelper(int[][] board) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == 0) {
                    for (int num = 1; num <= SIZE; num++) {
                        if (isValidMove(board, row, col, num)) {
                            board[row][col] = num;

                            if (solveSudokuHelper(board)) {
                                return true;
                            }

                            board[row][col] = 0;  // Backtrack if the solution is not found
                        }
                    }
                    return false;  // No valid number found for this cell
                }
            }
        }
        return true;  // All cells are filled
    }

    private static boolean isValidMove(int[][] board, int row, int col, int num) {
        // Check if the number is already in the row or column
        for (int i = 0; i < SIZE; i++) {
            if (board[row][i] == num || board[i][col] == num) {
                return false;
            }
        }

        // Check if the number is already in the subgrid
        int startRow = row - row % SUBGRID_SIZE;
        int startCol = col - col % SUBGRID_SIZE;
        for (int i = 0; i < SUBGRID_SIZE; i++) {
            for (int j = 0; j < SUBGRID_SIZE; j++) {
                if (board[startRow + i][startCol + j] == num) {
                    return false;
                }
            }
        }

        return true;
    }

    private static void copyBoard() {
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(solvedBoard[i], 0, board[i], 0, SIZE);
        }
    }

    private static void removeNumbers() {
        Random random = new Random();
        int numberOfClues = SIZE * SIZE / 2; // You can adjust the difficulty by changing this value

        for (int i = 0; i < numberOfClues; i++) {
            int row = random.nextInt(SIZE);
            int col = random.nextInt(SIZE);

            while (board[row][col] == 0) {
                row = random.nextInt(SIZE);
                col = random.nextInt(SIZE);
            }

            board[row][col] = 0;
        }
    }

    private static void playGame() {
        while (true) {
            displayBoard();

            System.out.println("Enter your move (row, column, value), or enter '0' to exit:");
            int row = scanner.nextInt();
            if (row == 0) {
                System.out.println("Exiting Sudoku game. Goodbye!");
                break;
            }

            int col = scanner.nextInt();
            int value = scanner.nextInt();

            if (isValidMove(board, row - 1, col - 1, value)) {
                board[row - 1][col - 1] = value;
                if (isSolved()) {
                    displayBoard();
                    System.out.println("Congratulations! You solved the Sudoku puzzle!");
                    break;
                }
            } else {
                System.out.println("Invalid move. Try again.");
            }
        }
    }

    private static boolean isSolved() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] != solvedBoard[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    private static void displayBoard() {
        System.out.println("Sudoku Board:");
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.out.print(board[i][j] == 0 ? " -" : " " + board[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }
}
