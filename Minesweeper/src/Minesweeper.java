import java.util.Random;

public class Minesweeper {

    // Data members
    private char[][] board;   // The game board where cells will be displayed
    private boolean[][] mines; // Array to track the locations of mines
    private boolean[][] revealed; // Array to track which cells have been revealed
    private final boolean[][] flagged; // // Array to track which cells have been flagged
    private int rows; // Number of rows in the board
    private int cols; // Number of columns in the board
    private int numMines; // Number of mines in the game
    private boolean gameOver; // Boolean to check if the game is over


    // Constructor to initialize the board with the specified dimensions and number of mines
    public Minesweeper(int rows, int cols, int numMines) {
        this.rows = rows;
        this.cols = cols;
        this.numMines = numMines;
        this.board = new char[rows][cols];
        this.mines = new boolean[rows][cols];
        this.revealed = new boolean[rows][cols];
        this.flagged = new boolean[rows][cols];
        this.gameOver = false;

        // Initialize the board and place the mines
        initializeBoard();
        placeMines();
        calculateNumbers();
        NearbyMines(rows, cols);

    }


    // Call methods to initialize the board and place mines
    public boolean getGameOver() {
        return this.gameOver;
    }

    public void setGameOver(boolean status) {
        this.gameOver = status;
        if (gameOver){
            revealAllMines(); //Reveal all mines when game over
        }

    }

    // Method to initialize the game board with empty values
    private void initializeBoard() {
        // TODO: Implement this method
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j] = '*';   // set unrevealed to " "
                revealed[i][j] = false;
                flagged[i][j] = false;
            }
        }
    }

    // Method to randomly place mines on the board
    private void placeMines() {
        // TODO: Implement this method
        Random random = new Random();
        int randomCol;
        int randomRow;
        int MinesPlaced = 0;

        while (MinesPlaced < numMines) {
            randomRow = random.nextInt(rows);
            randomCol = random.nextInt(cols);
            if (!mines[randomRow][randomCol]) {
                mines[randomRow][randomCol] = true;
                MinesPlaced++;
            }
        }
    }

    // Method to calculate numbers on the board for non-mine cells
    private void calculateNumbers() {
        // TODO: Implement this method

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (flagged[i][j]) {
                    board[i][j] = 'F';
                    System.out.print("F ");  // Show flagged cells
                }
                if (mines[i][j]) {
                    board[i][j] = 'X';
                }
                if (!mines[i][j]) {
                    int mineCount = NearbyMines(i, j);
                    if (mineCount > 0) {
                        board[i][j] = (char) (mineCount + '0'); // convert number to char
                    } else {
                        board[i][j] = '*';  // empty box
                    }
                }
            }
        }
    }

    //MineNearbyChecker
    private int NearbyMines(int row, int col) {
        int nearbyCount = 0;
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                //check out of bounds
                if (i >= 0 && i < rows && j >= 0 && j < cols && !(i == row && j == col)) {
                    if (mines[i][j]) {
                        nearbyCount++;
                    }
                }
            }
        }
        return nearbyCount;
    }

    // Method to display the current state of the board
    public void displayBoard() {
        // TODO: Implement this method
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (flagged[i][j]) {
                    System.out.print("F ");  // Show flagged cells
                    flagged[i][j] = true;
                    flagCell(rows, cols);
                } else if (revealed[i][j]) {
                    System.out.print(board[i][j] + " "); // Show revealed cells
                    revealed[i][j] = true;
                    revealCell(rows, cols);
                } else {
                    System.out.print("_ "); //Show unrevealed cells
                }
                if (mines[i][j] && revealed[i][j]) {
                    System.out.print("X ");
                    checkLoss(rows, cols);
                }
            }
            System.out.println();
        }
    }

    // Method to handle a player's move (reveal a cell or place a flag)
    public void playerMove(int row, int col, String action) {
        // TODO: Implement this method
        if (action.equals("reveal")) {
            if (!flagged[row][col]) { // Only reveal if the cell is not flagged
                revealCell(row, col);
                checkWin(row, col);
            }
            if (mines[row][col]) {
                setGameOver(true);
            }
            else {
                revealCell(row, col);
            }
        }
        if (action.equals("flag")) {
            flagCell(row, col);
        }
    }

    // Method to check if the player has won the game
    public boolean checkWin(int row, int col) {
        // TODO: Implement this method
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (!mines[i][j] && !revealed[i][j]) {  // Not all non-Mines revealed so cant win yet
                    return false;
                }
            }
        }
        System.out.println("Congratulations! You've won the game.");
        setGameOver(true);
        return true;
    }

    // Method to check if the player has lost the game
    public boolean checkLoss(int row, int col) {
        // TODO: Implement this method
        if (mines[row][col] && revealed[row][col]) {
            System.out.println("Game Over! You hit a mine.");
            setGameOver(true);
            return true;
        }
        return false;
    }


    // Method to reveal a cell (and adjacent cells if necessary)
    private void revealCell(int row, int col) {
        // TODO: Implement this method
        // Prevent revealing a cell that is already revealed or out of bounds
        if (row < 0 || row >= rows || col < 0 || col >= cols || revealed[row][col]) {
            return;
        }

        // If it's a mine, we lose the game
        if (mines[row][col]) {
            board[row][col] = 'X';
            checkLoss(row, col);
            return;
        }

        // Reveal the current cell
        revealed[row][col] = true;
        checkWin(row,col);

        if (board[row][col] == '*') {
            for (int i = row - 1; i <= row + 1; i++) {
                for (int j = col - 1; j <= col + 1; j++) {
                    // Ensure we don't go out of bounds or reveal the current cell
                    if (i >= 0 && i < rows && j >= 0 && j < cols && !(i == row && j == col)) {
                        revealCell(i, j);
                        checkWin(row,col);
                    }
                }
            }
        }
    }


    // Method to flag a cell as containing a mine
    private void flagCell(int row, int col) {
        // TODO: Implement this method
        if (row < 0 || row >= board.length || col < 0 || col >= board.length) {
            return;
        }
        // Prevent flagging or unflagging an already revealed cell
        if (revealed[row][col] && !flagged[row][col]) {
           revealCell(row, col);
            return;
        }else
            revealed[row][col] = true;

        // Toggle the flag state
        if (board[row][col] == 'F') {
            // Unflag the cell
            unflagCell(row,col);
        }
        else {
            // Flag the cell
            revealed[row][col] = false;
            flagged[row][col] = true;
            board[row][col] = 'F';
        }
    }


    // Method to unflag a cell
    private void unflagCell(int row, int col) {
        // TODO: Implement this method
        if (board[row][col] == 'F') {
            flagged[row][col] = false;
            revealed[row][col] = false;
            board[row][col] = '*';  // Set it back to its default unrevealed state
        }
    }


    // Method to reveal all mines on the board
    private void revealAllMines() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (mines[i][j]) {
                    board[i][j] = 'X';
                    System.out.println(mines[i][j] + "F ");
                }
            }
        }
    }


}
