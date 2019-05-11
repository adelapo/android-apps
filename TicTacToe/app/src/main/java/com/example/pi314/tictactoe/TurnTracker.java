package com.example.pi314.tictactoe;

public class TurnTracker {
    boolean hostTurn;

    TurnTracker() {
        hostTurn = true;
    }

    void toggle() {
        hostTurn = !hostTurn;
    }

    boolean getHostTurn() {
        return hostTurn;
    }
}
