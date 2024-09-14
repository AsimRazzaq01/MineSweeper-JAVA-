package src;

import src.Minesweeper;

import java.util.Scanner;

public class Main {
    // Main method to start the game
    public static void main(String[] args) {
        // Create a Minesweeper game with specific dimensions and number of mines
        Minesweeper game = new Minesweeper(10, 10, 10);

        Scanner scanner = new Scanner(System.in);

        // Game loop
        while (!game.getGameOver()) {
            game.displayBoard();
            // Get player input for row, col, and action (reveal or flag)
            // For now, just simulate a move (to be replaced with real player input logic)
            System.out.println("Please enter a number between 1 and 10: for Row:  ");
            int row = scanner.nextInt();
            System.out.println("Please enter a number between 1 and 10: for Column:  ");
            int column = scanner.nextInt();
            System.out.println("Please enter (reveal or flag): ");
            String action = scanner.next();

            //Use the above-mentioned inputs from user
            game.playerMove(row-1, column-1, action);

        }
    }
}