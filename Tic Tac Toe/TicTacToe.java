import java.util.Scanner;

/**
 * A simple console-based Tic-Tac-Toe game for two players.
 */
public class TicTacToe {

    private char[][] board;
    private char currentPlayer;
    private boolean gameEnded;

    /**
     * Constructor to initialize the game.
     */
    public TicTacToe() {
        board = new char[3][3];
        currentPlayer = 'X';
        gameEnded = false;
        initializeBoard();
    }

    /**
     * Sets up the initial empty board.
     */
    private void initializeBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = '-';
            }
        }
    }

    /**
     * Prints the current state of the board to the console.
     */
    public void printBoard() {
        System.out.println("-------------");
        for (int i = 0; i < 3; i++) {
            System.out.print("| ");
            for (int j = 0; j < 3; j++) {
                System.out.print(board[i][j] + " | ");
            }
            System.out.println();
            System.out.println("-------------");
        }
    }

    /**
     * The main game loop.
     */
    public void startGame() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Tic-Tac-Toe Game Started!");
        
        while (!gameEnded) {
            printBoard();
            System.out.println("Player " + currentPlayer + ", enter your move (row and column, e.g., 1 1):");

            int row = -1;
            int col = -1;

            // Loop until a valid move is entered
            while (true) {
                try {
                    row = scanner.nextInt() - 1;
                    col = scanner.nextInt() - 1;

                    if (row >= 0 && row < 3 && col >= 0 && col < 3 && board[row][col] == '-') {
                        board[row][col] = currentPlayer;
                        break;
                    } else {
                        System.out.println("This move is not valid. Try again.");
                    }
                } catch (Exception e) {
                    System.out.println("Invalid input. Please enter numbers for row and column.");
                    scanner.next(); // Clear the invalid input
                }
            }

            // Check for game end conditions
            if (checkForWin()) {
                printBoard();
                System.out.println("Player " + currentPlayer + " wins!");
                gameEnded = true;
            } else if (isBoardFull()) {
                printBoard();
                System.out.println("The game is a draw!");
                gameEnded = true;
            } else {
                // Switch to the other player
                switchPlayer();
            }
        }
        scanner.close();
    }

    /**
     * Switches the current player from 'X' to 'O' or vice versa.
     */
    private void switchPlayer() {
        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
    }

    /**
     * Checks if the board is full, resulting in a draw.
     * @return true if the board is full, false otherwise.
     */
    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == '-') {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks if the current player has won the game.
     * @return true if the current player has won, false otherwise.
     */
    private boolean checkForWin() {
        // Check rows and columns
        for (int i = 0; i < 3; i++) {
            if ((board[i][0] == currentPlayer && board[i][1] == currentPlayer && board[i][2] == currentPlayer) ||
                (board[0][i] == currentPlayer && board[1][i] == currentPlayer && board[2][i] == currentPlayer)) {
                return true;
            }
        }
        // Check diagonals
        if ((board[0][0] == currentPlayer && board[1][1] == currentPlayer && board[2][2] == currentPlayer) ||
            (board[0][2] == currentPlayer && board[1][1] == currentPlayer && board[2][0] == currentPlayer)) {
            return true;
        }
        return false;
    }

    /**
     * The main method to run the Tic-Tac-Toe game.
     */
    public static void main(String[] args) {
        TicTacToe game = new TicTacToe();
        game.startGame();
    }
}