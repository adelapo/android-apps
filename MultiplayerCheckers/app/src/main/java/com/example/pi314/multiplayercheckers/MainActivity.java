package com.example.pi314.multiplayercheckers;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // https://firebase.google.com/docs/database/android/start
        // https://firebase.google.com/docs/database/android/read-and-write

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        Button createGameButton = findViewById(R.id.create_game_button);
        Button joinGameButton = findViewById(R.id.join_game_button);
        final EditText gameIdEntry = findViewById(R.id.game_id_entry);

        createGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // CREATE A NEW GAME
                Random random = new Random();

                int newGameId = random.nextInt(9000) + 1000;
                DatabaseReference newGameRef = database.getReference(Integer.toString(newGameId));

                TicTacToeBoard emptyBoard = new TicTacToeBoard();

                newGameRef.child("players").setValue(1);
                newGameRef.child("board").setValue(emptyBoard.boardAsStringArray());

                Intent intent = new Intent(v.getContext(), GameActivity.class);
                intent.putExtra("game_id", Integer.toString(newGameId));
                intent.putExtra("is_host", "true");
                startActivity(intent);
            }
        });

        joinGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final String gameId = gameIdEntry.getText().toString();

                final DatabaseReference gameRef = database.getReference(gameId);

                gameRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int numPlayers = ((Long) dataSnapshot.child("players").getValue()).intValue();
                        if (numPlayers < 2) {
                            // JOIN THE GAME
                            gameRef.child("players").setValue(numPlayers + 1);

                            Intent intent = new Intent(v.getContext(), GameActivity.class);
                            intent.putExtra("game_id", gameId);
                            intent.putExtra("is_host", "false");
                            startActivity(intent);

                        } else {
                            // GAME IS FULL
                            System.out.println("That game is full.");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                Intent intent = new Intent(v.getContext(), GameActivity.class);
                intent.putExtra("game_id", gameId);
                intent.putExtra("is_host", "false");
                startActivity(intent);
            }
        });


    }
}
