package com.example.pi314.myfirstapp;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

// Following is adapted from
// http://www.codexpedia.com/android/asynctask-and-httpurlconnection-sample-in-android/

public class GetExchangeRate extends AsyncTask<String, Void, String> {
    HttpURLConnection urlConnection;
    BufferedReader reader;

    public AsyncResponse delegate;

    public GetExchangeRate(AsyncResponse response) {
        this.delegate = response;
    }

    String jsonData;

    @Override
    protected void onPostExecute(String s) {
        delegate.processFinish(s);
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL("http://data.fixer.io/api/latest?access_key=6a8a0d03e262eaaa2674428c49ca30d9");

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return "";
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            jsonData = buffer.toString();
            return jsonData;
        } catch (IOException e) {
            return null;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    System.out.println("This shouldn't ever happen, hopefully.");
                }
            }
        }
    }
}