package com.rchadha13.weatherapplication;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherApiCaller {

    private static final String BASE_URL = "https://api.open-meteo.com/v1/forecast";
    private static final String API_PARAMETERS = "&current_weather=true&hourly=temperature_2m,relativehumidity_2m,precipitation_probability,windspeed_10m,visibility&temperature_unit=fahrenheit";

    public interface WeatherApiCallback {
        void onWeatherDataReceived(JSONObject response) throws JSONException; // Change the parameter type to JSONObject
    }

    public static void callWeatherApi(double latitude, double longitude, WeatherApiCallback callback) {
        String apiUrl = BASE_URL + "?latitude=" + latitude + "&longitude=" + longitude + API_PARAMETERS;
        new WeatherApiTask(callback).execute(apiUrl);
    }

    private static class WeatherApiTask extends AsyncTask<String, Void, JSONObject> {

        private final WeatherApiCallback callback;

        WeatherApiTask(WeatherApiCallback callback) {
            this.callback = callback;
        }

        @Override
        protected JSONObject doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();
                connection.disconnect();

                try {
                    JSONObject jsonResponse = new JSONObject(response.toString());

                    // Add current timestamp as a key-value pair
                    long currentTimestamp = System.currentTimeMillis() / 1000;
                    jsonResponse.put("current_timestamp", currentTimestamp);

                    return jsonResponse;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONObject response) {
            if (callback != null) {
                try {
                    callback.onWeatherDataReceived(response);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}