package com.example.pi314.tictactoe;

import java.util.ArrayList;

public class TicTacToeBoard {
    enum Mark {
        X, O
    }

    public Mark[] board;

    public TicTacToeBoard() {
        board = new Mark[]{null, null, null, null, null, null, null, null, null};
    }

    Mark getWinner() {
        // Check the rows
        for (int row = 0; row < 3; row++) {
            if (getMark(row, 0) == getMark(row, 1) && getMark(row, 1) == getMark(row, 2) && getMark(row, 0) != null) {
                return getMark(row, 0);
            }
        }

        // Check the columns
        for (int col = 0; col < 3; col++) {
            if (getMark(0, col) == getMark(1, col) && getMark(1, col) == getMark(2, col) && getMark(0, col) != null) {
                return getMark(0, col);
            }
        }

        // Check diagonal \
        if (getMark(0, 0) == getMark(1, 1) && getMark(1, 1) == getMark(2, 2) && getMark(0, 0) != null) {
            return getMark(0, 0);
        }

        // Check diagonal /
        if (getMark(2, 0) == getMark(1, 1) && getMark(1, 1) == getMark(0, 2) && getMark(2, 0) != null) {
            return getMark(2, 0);
        }

        return null;
    }

    public Mark getMark(int row, int col) {
        int index = row * 3 + col;
        return board[index];
    }

    public void setMark(int row, int col, Mark mark) {
        int index = row * 3 + col;
        board[index] = mark;
    }

    public void setMark(int row, int col, String markString) {
        if (markString.equals("X")) {
            setMark(row, col, Mark.X);
        } else if (markString.equals("O")) {
            setMark(row, col, Mark.O);
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (int row = 0; row < 3; row++) {
            Mark mark = getMark(row, 0);
            if (mark == null) {
                stringBuilder.append(" ");
            } else {
                stringBuilder.append(mark);
            }
            stringBuilder.append("|");

            mark = getMark(row, 1);
            if (mark == null) {
                stringBuilder.append(" ");
            } else {
                stringBuilder.append(mark);
            }
            stringBuilder.append("|");

            mark = getMark(row, 2);
            if (mark == null) {
                stringBuilder.append(" ");
            } else {
                stringBuilder.append(mark);
            }

            if (row < 2) {
                stringBuilder.append("\n-----\n");
            }
        }

        return stringBuilder.toString();
    }

    ArrayList<String> boardAsStringArray() {
        ArrayList<String> stringArray = new ArrayList<>();
        for (Mark mark : board) {
            if (mark == Mark.X) {
                stringArray.add("X");
            } else if (mark == Mark.O) {
                stringArray.add("O");
            } else {
                stringArray.add("");
            }
        }
        return stringArray;
    }
}
