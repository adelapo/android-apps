package com.example.pi314.myfirstapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AsyncResponse {

    JSONObject exchangeRates;

    Spinner spinner1, spinner2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText moneyInput1 = findViewById(R.id.moneyInput1);
        final EditText moneyInput2 = findViewById(R.id.moneyInput2);

        final Button refreshButton = findViewById(R.id.refreshButton);

        final SyncManager syncManager = new SyncManager(); // Keeps track of when text is edited

        new GetExchangeRate(this).execute();

        spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);

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

                    double amount;
                    if (s.toString().equals("")) {
                        amount = 0;
                    } else {
                        amount = Double.parseDouble(s.toString());
                    }
                    String fromCurrency = spinner1.getSelectedItem().toString();
                    String toCurrency = spinner2.getSelectedItem().toString();

                    double convertedAmount = makeConversion(fromCurrency, toCurrency, amount);

                    moneyInput2.setText(Double.toString(convertedAmount));
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

                    double amount;
                    if (s.toString().equals("")) {
                        amount = 0;
                    } else {
                        amount = Double.parseDouble(s.toString());
                    }
                    String fromCurrency = spinner1.getSelectedItem().toString();
                    String toCurrency = spinner2.getSelectedItem().toString();

                    double convertedAmount = makeConversion(fromCurrency, toCurrency, amount);

                    moneyInput1.setText(Double.toString(convertedAmount));

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

            // Also update the spinner
            Iterator<String> keys = exchangeRates.keys();
            List<String> currencies = new ArrayList<>();

            while (keys.hasNext()) {
                currencies.add(keys.next());
            }
            System.out.println(currencies);

            // AdapterView is a view that holds data; ArrayAdapter provides data as an array
            ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, currencies);
            arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, currencies);
            arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinner1.setAdapter(arrayAdapter1);
            spinner2.setAdapter(arrayAdapter1);

        } catch (JSONException e) {
            exchangeRates = null;
        }
    }

    public double makeConversion(String fromCurrency, String toCurrency, double amount) {
        try {
            return (amount / exchangeRates.getDouble(fromCurrency)) * exchangeRates.getDouble(toCurrency);
        } catch (JSONException e) {
            return 1.0;
        }
    }
}
