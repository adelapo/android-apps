package com.example.pi314.tictactoe;

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

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // https://firebase.google.com/docs/database/android/start
        // https://firebase.google.com/docs/database/android/read-and-write

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference("message");

        final TicTacToeBoard board = new TicTacToeBoard(this);

        dbRef.setValue("Hello, world!");

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                System.out.println("VALUE IS " + value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Button createGameButton = findViewById(R.id.create_game_button);
        Button joinGameButton = findViewById(R.id.join_game_button);
        final EditText gameIdEntry = findViewById(R.id.game_id_entry);

        createGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(board);
            }
        });

        joinGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int gameId = Integer.parseInt(gameIdEntry.getText().toString());
            }
        });


    }
}
