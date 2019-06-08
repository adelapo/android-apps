package com.example.pi314.tictactoe;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

        class CustomValueEventListener implements ValueEventListener {
            int index;

            CustomValueEventListener(int index) {
                this.index = index;
            }

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    // This happens when the user leaves a game
                    return;
                }

                String mark = dataSnapshot.getValue(String.class);
                System.out.println("Board position " + index + " changed to " + mark + ".");

                if (mark == null || mark.equals("")) {
                    return;
                }

                if (!userMark.equals(mark)) {
                    buttons[index].setText(mark);
                    board.setMark(index / 3, index % 3, mark);
                    turnTracker.toggle();
                    statusText.setText("Your turn.");
                } else {
                    System.out.println("Was own player, so ignoring it...");
                }

                System.out.println(board);
                System.out.println("Is there a winner? " + board.getWinner());

                TicTacToeBoard.Mark winnerMark = board.getWinner();

                if (winnerMark != null) {
                    String winner;

                    if (winnerMark == TicTacToeBoard.Mark.X) {
                        winner = "X";
                    } else {
                        winner = "O";
                    }

                    final boolean userWon = userMark.equals(winner);
                    String endMessage;
                    if (userWon) {
                        endMessage = "You win!";
                    } else {
                        endMessage = "You lost.";
                    }

                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            switch (which) {
//                                case DialogInterface.BUTTON_POSITIVE:
//                                    // YES button clicked
//                                    board = new TicTacToeBoard();
//                                    for (Button button : buttons) {
//                                        button.setText("");
//                                    }
//                                    break;
//
//                                case DialogInterface.BUTTON_NEGATIVE:
//                                    // NO button clicked
//                                    playersRef.setValue(1);
//                                    Intent intent = new Intent(context, MainActivity.class);
//                                    startActivity(intent);
//                                    break;
//                            }
                            gameRef.removeValue();
                            Intent intent = new Intent(context, MainActivity.class);
                            startActivity(intent);
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage(endMessage)
                           .setNeutralButton("OK", dialogClickListener)
                           .show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }

        for (int i = 0; i < 9; i++) {
            boardRef.child(Integer.toString(i)).addValueEventListener(new CustomValueEventListener(i));
        }

        playersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    // This happens when the user leaves a game
                    return;
                }

                if (dataSnapshot.getValue(int.class) == 2 && isHost) {
                    Toast toast = Toast.makeText(context, "A player joined your game.", Toast.LENGTH_LONG);
                    toast.show();

                    statusText.setText("Your turn.");
                } else if (dataSnapshot.getValue(int.class) == 1 && !isHost) {
                    isHost = true;
                    Toast toast = Toast.makeText(context, "You're the host of this game.", Toast.LENGTH_LONG);

                    statusText.setText("No one has joined.");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        class CustomOnClickListener implements View.OnClickListener {
            int row;
            int col;

            CustomOnClickListener(int row, int col) {
                this.row = row;
                this.col = col;
            }

            CustomOnClickListener(int index) {
                row = index / 3;
                col = index % 3;
            }

            @Override
            public void onClick(View v) {
                if (isUserTurn()) {
                    if (board.getMark(row, col) != null) {
                        // Someone already moved there
                        return;
                    }

                    board.setMark(row, col, userMark);
                    int index = row * 3 + col;

                    boardRef.child(Integer.toString(index)).setValue(userMark);
                    buttons[index].setText(userMark);

                    turnTracker.toggle();
                    statusText.setText("Opponent's turn.");

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