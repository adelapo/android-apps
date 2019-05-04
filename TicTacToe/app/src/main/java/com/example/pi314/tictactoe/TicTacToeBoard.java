package com.example.pi314.tictactoe;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class TicTacToeBoard extends View {
    enum Mark {
        X, O
    }

    public Mark[] board;

    Paint redPaint, bluePaint, blackPaint;

    public TicTacToeBoard(Context context) {
        super(context);
        board = new Mark[]{null, null, null, null, null, null, null, null, null};

        redPaint = new Paint();
        redPaint.setColor(Color.RED);
        redPaint.setStrokeWidth(10);

        bluePaint = new Paint();
        bluePaint.setColor(Color.BLUE);
        bluePaint.setStrokeWidth(10);

        blackPaint = new Paint();
        blackPaint.setColor(Color.BLACK);
        blackPaint.setStrokeWidth(10);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        // Draw the board
        canvas.drawLine((float) width / 3, (float) 0.2 * height,
                (float) width / 3, (float) 0.8 * height, blackPaint);

        canvas.drawLine((float) 2 * width / 3, (float) 0.2 * height,
                (float) 2 * width / 3, (float) 0.8 * height, blackPaint);

        canvas.drawLine((float) 0.1 * width, (float) height / 3,
                (float) 0.9 * width, (float) height / 3, blackPaint);

        canvas.drawLine((float) 0.1 * width, (float) 2 * height / 3,
                (float) 0.9 * width, (float) 2 * height / 3, blackPaint);

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
}
