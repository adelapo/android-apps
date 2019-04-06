package com.example.pi314.myfirstapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements AsyncResponse {

    JSONObject exchangeRates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText moneyInput1 = findViewById(R.id.moneyInput1);
        final EditText moneyInput2 = findViewById(R.id.moneyInput2);

        final Button refreshButton = findViewById(R.id.refreshButton);

        final SyncManager syncManager = new SyncManager(); // Keeps track of when text is edited

        moneyInput1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!syncManager.editingText) {
                    syncManager.editingText = true;
                    moneyInput2.setText(s);
                    syncManager.editingText = false;
                }
            }
        });

        moneyInput2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!syncManager.editingText) {
                    syncManager.editingText = true;
                    moneyInput1.setText(s);
                    syncManager.editingText = false;
                }
            }
        });
    }

    @Override
    public void processFinish(String output) {
        try {
            JSONObject jsonOutput = new JSONObject(output);
            exchangeRates = jsonOutput.getJSONObject("rates");
            System.out.println("1 Euro is $" + exchangeRates.getDouble("USD"));
        } catch (JSONException e) {
            exchangeRates = null;
        }
    }
}
