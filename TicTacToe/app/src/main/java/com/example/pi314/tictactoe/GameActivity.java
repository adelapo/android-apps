package com.example.pi314.tictactoe;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GameActivity extends AppCompatActivity {

    int gameId;
    boolean isHost;
    String userMark;

    TicTacToeBoard board;

    final TurnTracker turnTracker = new TurnTracker();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        board = new TicTacToeBoard();

        // Determine if user is host
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String extraGameId = extras.getString("game_id");
            String extraIsHost = extras.getString("is_host");

            if (extraGameId != null && extraIsHost != null) {
                gameId = Integer.parseInt(extraGameId);
                isHost = Boolean.parseBoolean(extraIsHost);
            }
        }

        TextView gameIdTextView = findViewById(R.id.game_id_text);
        gameIdTextView.setText("Game ID: " + gameId);

        final TextView statusText = findViewById(R.id.status_text);

        if (isHost) {
            userMark = "X";

            statusText.setText("No one has joined.");
        } else {
            userMark = "O";
            turnTracker.toggle();

            statusText.setText("Opponent's turn.");
        }

        final DatabaseReference gameRef = FirebaseDatabase.getInstance().getReference(Integer.toString(gameId));
        final DatabaseReference boardRef = gameRef.child("board");
        final DatabaseReference playersRef = gameRef.child("players");

        final Context context = this;

        final Button[] buttons = new Button[]{findViewById(R.id.button_00),
                findViewById(R.id.button_01),
                findViewById(R.id.button_02),
                findViewById(R.id.button_10),
                findViewById(R.id.button_11),
                findViewById(R.id.button_12),
                findViewById(R.id.button_20),
                findViewById(R.id.button_21),
                findViewById(R.id.button_22)};

        boardRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!isUserTurn()) {
                    int row, col;
                    for (int i = 0; i < 9; i++) {
                        row = i / 3;
                        col = i % 3;

                        String mark = (String) dataSnapshot.child(Integer.toString(i)).getValue();
                        buttons[i].setText(mark);
                        board.setMark(row, col, mark);
                    }
                    turnTracker.toggle();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        playersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(int.class) == 2 && isHost) {
                    Toast toast = Toast.makeText(context, "A player joined your game.", Toast.LENGTH_LONG);
                    toast.show();

                    statusText.setText("Your turn.");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        class CustomOnClickListener implements View.OnClickListener {
            int row;
            int col;

            public CustomOnClickListener(int row, int col) {
                this.row = row;
                this.col = col;
            }

            public CustomOnClickListener(int index) {
                row = index / 3;
                col = index % 3;
            }

            @Override
            public void onClick(View v) {
                if (isUserTurn()) {
                    board.setMark(row, col, userMark);
                    int index = row * 3 + col;

                    boardRef.child(Integer.toString(index)).setValue(userMark);
                    buttons[index].setText(userMark);

                    turnTracker.toggle();

                } else {
                    Toast toast = Toast.makeText(context, "It's not your turn.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }

        for (int i = 0; i < 9; i++) {
            buttons[i].setOnClickListener(new CustomOnClickListener(i));
        }
    }

    boolean isUserTurn() {
        return isHost == turnTracker.getHostTurn();
    }
}