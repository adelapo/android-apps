package com.example.pi314.multiplayercheckers;

import java.util.ArrayList;
import java.util.List;

public class CheckersPiece {
    String teamColor;
    int gridRow, gridColumn;

    public CheckersPiece(int gridRow, int gridColumn, String teamColor) {
        this.gridRow = gridRow;
        this.gridColumn = gridColumn;
        this.teamColor = teamColor;
    }

    List<List<Integer>> getAvailableMoves(CheckersBoard board) {
        List<List<Integer>> availableMoves = new ArrayList<>();



        return availableMoves;
    }

    @Override
    public String toString() {
        return teamColor.substring(0, 1);
    }
}
