import java.util.Scanner;

public class TicTacToe {
    private char[][] board;
    private char currentPlayer;
    private String player1Name;
    private String player2Name;
    private Scanner scanner;
    private int movesCount;

    public TicTacToe(String player1, String player2) {
        board = new char[3][3];
        scanner = new Scanner(System.in);
        currentPlayer = 'X';
        movesCount = 0;
        player1Name = player1;
        player2Name = player2;
        initializeBoard();
    }

    private void initializeBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
            }
        }
    }

    private void displayBoard() {
        System.out.println("\n╔═══════════════════════════╗");
        System.out.println("║      TIC TAC TOE          ║");
        System.out.println("╚═══════════════════════════╝");
        System.out.println("\n     1   2   3");
        System.out.println("   ┌───┬───┬───┐");
        
        for (int i = 0; i < 3; i++) {
            System.out.print(" " + (i + 1) + " │");
            for (int j = 0; j < 3; j++) {
                System.out.print(" " + board[i][j] + " │");
            }
            System.out.println();
            if (i < 2) {
                System.out.println("   ├───┼───┼───┤");
            }
        }
        System.out.println("   └───┴───┴───┘");
    }

    private void displayPlayerInfo() {
        String currentPlayerName = (currentPlayer == 'X') ? player1Name : player2Name;
        System.out.println("\n" + currentPlayerName + "'s turn (" + currentPlayer + ")");
    }

    private boolean makeMove(int row, int col) {
        if (row < 0 || row >= 3 || col < 0 || col >= 3) {
            System.out.println("Invalid position! Please enter row and column between 1-3.");
            return false;
        }

        if (board[row][col] != ' ') {
            System.out.println("Position already occupied! Choose another position.");
            return false;
        }

        board[row][col] = currentPlayer;
        movesCount++;
        return true;
    }

    private boolean checkWin() {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == currentPlayer && 
                board[i][1] == currentPlayer && 
                board[i][2] == currentPlayer) {
                return true;
            }
        }

        for (int j = 0; j < 3; j++) {
            if (board[0][j] == currentPlayer && 
                board[1][j] == currentPlayer && 
                board[2][j] == currentPlayer) {
                return true;
            }
        }

        if (board[0][0] == currentPlayer && 
            board[1][1] == currentPlayer && 
            board[2][2] == currentPlayer) {
            return true;
        }

        if (board[0][2] == currentPlayer && 
            board[1][1] == currentPlayer && 
            board[2][0] == currentPlayer) {
            return true;
        }

        return false;
    }

    private boolean checkDraw() {
        return movesCount == 9;
    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
    }

    public void play() {
        boolean gameRunning = true;

        System.out.println("\n╔═══════════════════════════════════════╗");
        System.out.println("║   WELCOME TO TIC TAC TOE GAME!        ║");
        System.out.println("╚═══════════════════════════════════════╝");
        System.out.println("\n" + player1Name + " (X) vs " + player2Name + " (O)");
        System.out.println("\nHow to play:");
        System.out.println("- Enter row number (1-3) and column number (1-3)");
        System.out.println("- Get 3 in a row to win!\n");

        while (gameRunning) {
            displayBoard();
            displayPlayerInfo();

            System.out.print("Enter row (1-3): ");
            int row = scanner.nextInt() - 1;
            System.out.print("Enter column (1-3): ");
            int col = scanner.nextInt() - 1;

            if (makeMove(row, col)) {
                if (checkWin()) {
                    displayBoard();
                    String winner = (currentPlayer == 'X') ? player1Name : player2Name;
                    System.out.println("\n╔═══════════════════════════════════════╗");
                    System.out.println("║  🎉 " + winner + " WINS! 🎉");
                    System.out.println("╚═══════════════════════════════════════╝");
                    gameRunning = false;
                } else if (checkDraw()) {
                    displayBoard();
                    System.out.println("\n╔═══════════════════════════════════════╗");
                    System.out.println("║      IT'S A DRAW! WELL PLAYED!        ║");
                    System.out.println("╚═══════════════════════════════════════╝");
                    gameRunning = false;
                } else {
                    switchPlayer();
                }
            }
        }

        System.out.print("\nPlay again? (y/n): ");
        char playAgain = scanner.next().charAt(0);
        if (playAgain == 'y' || playAgain == 'Y') {
            initializeBoard();
            currentPlayer = 'X';
            movesCount = 0;
            play();
        } else {
            System.out.println("\nThank you for playing Tic Tac Toe!");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("╔═══════════════════════════════════════╗");
        System.out.println("║      TIC TAC TOE GAME SETUP           ║");
        System.out.println("╚═══════════════════════════════════════╝");
        
        System.out.print("\nEnter Player 1 name (X): ");
        String player1 = scanner.nextLine();
        
        System.out.print("Enter Player 2 name (O): ");
        String player2 = scanner.nextLine();

        TicTacToe game = new TicTacToe(player1, player2);
        game.play();
        
        scanner.close();
    }
}
