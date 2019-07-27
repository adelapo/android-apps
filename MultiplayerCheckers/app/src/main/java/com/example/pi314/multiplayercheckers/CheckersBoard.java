package com.example.pi314.multiplayercheckers;

public class CheckersBoard {
    // An 8x8 checkers board

    CheckersPiece[][] grid;

    public CheckersBoard() {
        grid = new CheckersPiece[8][8];
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 8; col++) {
                grid[row][col] = new CheckersPiece(row, col, "red");
                grid[row + 6][col] = new CheckersPiece(row, col, "black");
            }
        }
    }
}
