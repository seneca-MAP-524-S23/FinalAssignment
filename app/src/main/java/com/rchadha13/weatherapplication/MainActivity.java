package com.rchadha13.weatherapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 123;
    TextView citytextview;
    LinearLayout AllCharts ;
    TextView updatedat;
    TextView windtextview, winddirectiontextview, current_temperaturetextview;
    LineChart linechart1, linechart2, linechart3, linechart4, linechart5;

    ImageButton fullscreen1, fullscreen2, fullscreen3, fullscreen4, fullscreen5;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        citytextview = findViewById(R.id.city);
        updatedat = findViewById(R.id.updatedat);
        windtextview = findViewById(R.id.wind);
        winddirectiontextview = findViewById(R.id.winddirection);
        current_temperaturetextview = findViewById(R.id.temperature);

        AllCharts = findViewById(R.id.allcharts);

        fullscreen1 = findViewById(R.id.fullscreen1);
        fullscreen2 = findViewById(R.id.fullscreen2);
        fullscreen3 = findViewById(R.id.fullscreen3);
        fullscreen4 = findViewById(R.id.fullscreen4);
        fullscreen5 = findViewById(R.id.fullscreen5);

        fullscreen1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FullScreenChart.class);
                intent.putExtra("chart", "temp");
                startActivity(intent);
            }
        });
        fullscreen2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FullScreenChart.class);
                intent.putExtra("chart", "humi");
                startActivity(intent);
            }
        });
        fullscreen3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FullScreenChart.class);
                intent.putExtra("chart", "prep");
                startActivity(intent);
            }
        });
        fullscreen4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FullScreenChart.class);
                intent.putExtra("chart", "wind");
                startActivity(intent);
            }
        });
        fullscreen5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FullScreenChart.class);
                intent.putExtra("chart", "visi");
                startActivity(intent);
            }
        });


        linechart1 = findViewById(R.id.chart1);
        linechart1.setBackgroundColor(Color.WHITE);
        linechart1.setGridBackgroundColor(Color.WHITE);
        linechart1.setDrawGridBackground(true);
        linechart1.setDrawBorders(true);
        linechart1.getDescription().setEnabled(false);
        linechart1.setPinchZoom(false);

        linechart2 = findViewById(R.id.chart2);
        linechart2.setBackgroundColor(Color.WHITE);
        linechart2.setGridBackgroundColor(Color.WHITE);
        linechart2.setDrawGridBackground(true);
        linechart2.setDrawBorders(true);
        linechart2.getDescription().setEnabled(false);
        linechart2.setPinchZoom(false);

        linechart3 = findViewById(R.id.chart3);
        linechart3.setBackgroundColor(Color.WHITE);
        linechart3.setGridBackgroundColor(Color.WHITE);
        linechart3.setDrawGridBackground(true);
        linechart3.setDrawBorders(true);
        linechart3.getDescription().setEnabled(false);
        linechart3.setPinchZoom(false);

        linechart4 = findViewById(R.id.chart4);
        linechart4.setBackgroundColor(Color.WHITE);
        linechart4.setGridBackgroundColor(Color.WHITE);
        linechart4.setDrawGridBackground(true);
        linechart4.setDrawBorders(true);
        linechart4.getDescription().setEnabled(false);
        linechart4.setPinchZoom(false);

        linechart5 = findViewById(R.id.chart5);
        linechart5.setBackgroundColor(Color.WHITE);
        linechart5.setGridBackgroundColor(Color.WHITE);
        linechart5.setDrawGridBackground(true);
        linechart5.setDrawBorders(true);
        linechart5.getDescription().setEnabled(false);
        linechart5.setPinchZoom(false);

        ImageButton openCitySelectionButton = findViewById(R.id.currentlocation);
        openCitySelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Request permission
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            LOCATION_PERMISSION_REQUEST_CODE);
                } else {
                    // Permission already granted, proceed with getting location
                    getCurrentLocation();
                    editor.remove("selectedPlace");
                    editor.apply();
                }
            }
        });

        sharedPreferences = getSharedPreferences("LocationData", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        ImageButton currentlocation = findViewById(R.id.mapicon);
        currentlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open CitySelectionFragment
                Intent intent = new Intent(MainActivity.this, Locations.class);
                startActivity(intent);

            }
        });


        String selectedPlaceJson = sharedPreferences.getString("selectedPlace", null);
        if (selectedPlaceJson != null) {
            try {
                JSONObject placeJson = new JSONObject(selectedPlaceJson);
                loaddetails(placeJson.getDouble("latitude"),placeJson.getDouble("longitude"), false);
                String formattedJson = placeJson.toString(4);
                Toast.makeText(this, formattedJson, Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
                // Handle JSON parsing error
            }
        } else {
            // Handle case where "selectedPlace" was not found in SharedPreferences
            // Check if permission is granted
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // Request permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_REQUEST_CODE);
            } else {
                // Permission already granted, proceed with getting location
                getCurrentLocation();
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            AllCharts.setVisibility(View.VISIBLE);
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            AllCharts.setVisibility(View.GONE);
        }
    }

    // Handle permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, get location
                getCurrentLocation();
            } else {
                // Permission denied, handle accordingly
//                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Get current location
    private void getCurrentLocation() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        loaddetails(latitude, longitude, true);

                        // Use latitude and longitude as needed
                    } else {
                        Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loaddetails(double latitude, double longitude, boolean current) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String city = addresses.get(0).getLocality();
        if (current) {
            citytextview.setText("Current location : " + city);
        } else {
            citytextview.setText("Viewing for  : " + city);
        }


        editor.putFloat("latitude", (float) latitude);
        editor.putFloat("longitude", (float) longitude);
        editor.putString("currentcity", city);
        editor.apply();
        WeatherApiCaller.callWeatherApi(latitude, longitude, new WeatherApiCaller.WeatherApiCallback() {
            @Override
            public void onWeatherDataReceived(JSONObject response) throws JSONException {
                if (response != null) {
                    // Convert JSONObject to a string
                    String jsonResponse = response.toString();
                    editor.putString("data",jsonResponse);
                    String timestampString = response.getString("current_timestamp");
                    long timestamp = Long.parseLong(timestampString);
                    Date date = new Date(timestamp * 1000); // Convert to milliseconds

                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                    String formattedTime = sdf.format(date);

                    updatedat.setText("Updated At : " + formattedTime);
                    JSONObject currentWeather = new JSONObject(response.getString("current_weather"));
                    double temperature = currentWeather.getDouble("temperature");
                    double windspeed = currentWeather.getDouble("windspeed");
                    int winddirection = currentWeather.getInt("winddirection");
                    current_temperaturetextview.setText( Double.toString(temperature));
                    windtextview.setText(Double.toString(windspeed) + " Mph");
                    winddirectiontextview.setText(Integer.toString(winddirection) + " degree");

                    // Extract time and temperature data from JSON arrays
                    JSONObject hourly = response.getJSONObject("hourly");
                    JSONArray timeJSONArray = hourly.getJSONArray("time");
                    JSONArray temperatureJSONArray = hourly.getJSONArray("temperature_2m");

                    ArrayList<Entry> tempArrayList = new ArrayList<>();

                    for (int i = 0; i < timeJSONArray.length(); i++) {
                        String time = timeJSONArray.getString(i); // Assuming the time is in a valid format
                        double temperatureentry = temperatureJSONArray.getDouble(i);

                        tempArrayList.add(new Entry(i, (float) temperatureentry)); // Use the index i as x value
                    }

                    LineDataSet set1 = new LineDataSet(tempArrayList, "Hourly Temperature");
                    set1.setAxisDependency(YAxis.AxisDependency.LEFT);
                    set1.setColor(Color.RED);
                    set1.setLineWidth(3f);
                    linechart1.setExtraOffsets(10f, 5f, 10f, 0f); // Adjust left and right margins
                    set1.setFillAlpha(255);
                    set1.setDrawCircles(false); // Disable drawing circles at entry points

                    LineData data = new LineData(set1);
                    data.setDrawValues(false);

                    linechart1.setData(data);

// Customize X-axis labels
                    XAxis xAxis = linechart1.getXAxis();
                    xAxis.setGranularity(1f);
                    xAxis.setLabelCount(2, true); // Set the number of labels to show (first and last)
                    xAxis.setValueFormatter(new ValueFormatter() {
                        @Override
                        public String getFormattedValue(float value) {
                            int index = (int) value;
                            if (index == 0) {
                                return formatDate(timeJSONArray.optString(0, ""));
                            }
                            if (index == timeJSONArray.length() - 1) {
                                return formatDate(timeJSONArray.optString(timeJSONArray.length() - 1, ""));
                            }
                            return ""; // Return an empty string for all other indices
                        }

                        private String formatDate(String time) {
                            try {
                                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault());
                                Date date = inputFormat.parse(time);

                                SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM HH:mm", Locale.getDefault());
                                return outputFormat.format(date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            return "";
                        }
                    });

                    linechart1.invalidate();

                    // Extract time and relative humidity data from JSON arrays
                    JSONArray relativeHumidityJSONArray = hourly.getJSONArray("relativehumidity_2m");

                    ArrayList<Entry> humidityArrayList = new ArrayList<>();

                    for (int i = 0; i < timeJSONArray.length(); i++) {
                        double humidityEntry = relativeHumidityJSONArray.getDouble(i);

                        humidityArrayList.add(new Entry(i, (float) humidityEntry)); // Use the index i as x value
                    }

                    LineDataSet humidityDataSet = new LineDataSet(humidityArrayList, "Hourly Relative Humidity");
                    humidityDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
                    humidityDataSet.setColor(Color.BLUE);
                    humidityDataSet.setLineWidth(3f);
                    humidityDataSet.setFillAlpha(255);
                    humidityDataSet.setDrawCircles(false); // Disable drawing circles at entry points

                    LineData humidityData = new LineData(humidityDataSet);
                    humidityData.setDrawValues(false);

                    LineChart linechart2 = findViewById(R.id.chart2); // Assuming you have defined chart2 in your layout XML
                    linechart2.setData(humidityData);

// Customize X-axis labels
                    XAxis xAxis2 = linechart2.getXAxis();
                    xAxis2.setGranularity(1f);
                    xAxis2.setLabelCount(2, true); // Set the number of labels to show (first and last)
                    xAxis2.setValueFormatter(new ValueFormatter() {
                        @Override
                        public String getFormattedValue(float value) {
                            int index = (int) value;
                            if (index == 0) {
                                return formatDate(timeJSONArray.optString(0, ""));
                            }
                            if (index == timeJSONArray.length() - 1) {
                                return formatDate(timeJSONArray.optString(timeJSONArray.length() - 1, ""));
                            }
                            return ""; // Return an empty string for all other indices
                        }

                        private String formatDate(String time) {
                            try {
                                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault());
                                Date date = inputFormat.parse(time);

                                SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM\nHH:mm", Locale.getDefault());
                                return outputFormat.format(date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            return "";
                        }
                    });

                    linechart2.setExtraOffsets(10f, 5f, 10f, 0f); // Adjust left and right margins
                    linechart2.invalidate();

                    // Extract time and precipitation probability data from JSON arrays
                    JSONArray precipitationProbabilityJSONArray = hourly.getJSONArray("precipitation_probability");

                    ArrayList<Entry> probabilityArrayList = new ArrayList<>();

                    for (int i = 0; i < timeJSONArray.length(); i++) {
                        double probabilityEntry = precipitationProbabilityJSONArray.getDouble(i);

                        probabilityArrayList.add(new Entry(i, (float) probabilityEntry)); // Use the index i as x value
                    }

                    LineDataSet probabilityDataSet = new LineDataSet(probabilityArrayList, "Hourly Precipitation Probability");
                    probabilityDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
                    probabilityDataSet.setColor(Color.GREEN);
                    probabilityDataSet.setLineWidth(3f);
                    probabilityDataSet.setFillAlpha(255);
                    probabilityDataSet.setDrawCircles(false); // Disable drawing circles at entry points

                    LineData probabilityData = new LineData(probabilityDataSet);
                    probabilityData.setDrawValues(false);

                    LineChart linechart3 = findViewById(R.id.chart3); // Assuming you have defined chart3 in your layout XML
                    linechart3.setData(probabilityData);

// Customize X-axis labels
                    XAxis xAxis3 = linechart3.getXAxis();
                    xAxis3.setGranularity(1f);
                    xAxis3.setLabelCount(5, true); // Set the number of labels to show (evenly spaced)
                    xAxis3.setValueFormatter(new ValueFormatter() {
                        @Override
                        public String getFormattedValue(float value) {
                            int index = (int) value;
                            if (index >= 0 && index < timeJSONArray.length()) {
                                return formatDate(timeJSONArray.optString(index, ""));
                            }
                            return ""; // Return an empty string for invalid indices
                        }

                        private String formatDate(String time) {
                            try {
                                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault());
                                Date date = inputFormat.parse(time);

                                SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM\nHH:mm", Locale.getDefault());
                                return outputFormat.format(date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            return "";
                        }
                    });

                    linechart3.setExtraOffsets(10f, 5f, 10f, 0f); // Adjust left and right margins
                    linechart3.invalidate();

                    // Extract time and wind speed data from JSON arrays
                    JSONArray windSpeedJSONArray = hourly.getJSONArray("windspeed_10m");

                    ArrayList<Entry> windSpeedArrayList = new ArrayList<>();

                    for (int i = 0; i < timeJSONArray.length(); i++) {
                        double windSpeedEntry = windSpeedJSONArray.getDouble(i);

                        windSpeedArrayList.add(new Entry(i, (float) windSpeedEntry)); // Use the index i as x value
                    }

                    LineDataSet windSpeedDataSet = new LineDataSet(windSpeedArrayList, "Hourly Wind Speed");
                    windSpeedDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
                    windSpeedDataSet.setColor(Color.parseColor("#800080"));
                    windSpeedDataSet.setLineWidth(3f);
                    windSpeedDataSet.setFillAlpha(255);
                    windSpeedDataSet.setDrawCircles(false); // Disable drawing circles at entry points

                    LineData windSpeedData = new LineData(windSpeedDataSet);
                    windSpeedData.setDrawValues(false);

                    LineChart linechart4 = findViewById(R.id.chart4); // Assuming you have defined chart4 in your layout XML
                    linechart4.setData(windSpeedData);

// Customize X-axis labels
                    XAxis xAxis4 = linechart4.getXAxis();
                    xAxis4.setGranularity(1f);
                    xAxis4.setLabelCount(2, true); // Set the number of labels to show (first and last)
                    xAxis4.setValueFormatter(new ValueFormatter() {
                        @Override
                        public String getFormattedValue(float value) {
                            int index = (int) value;
                            if (index == 0) {
                                return formatDate(timeJSONArray.optString(0, ""));
                            }
                            if (index == timeJSONArray.length() - 1) {
                                return formatDate(timeJSONArray.optString(timeJSONArray.length() - 1, ""));
                            }
                            return ""; // Return an empty string for all other indices
                        }

                        private String formatDate(String time) {
                            try {
                                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault());
                                Date date = inputFormat.parse(time);

                                SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM HH:mm", Locale.getDefault());
                                return outputFormat.format(date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            return "";
                        }
                    });

                    linechart4.setExtraOffsets(10f, 5f, 10f, 0f); // Adjust left and right margins
                    linechart4.invalidate();

                    // Extract time and visibility data from JSON arrays
                    JSONArray visibilityJSONArray = hourly.getJSONArray("visibility");

                    ArrayList<Entry> visibilityArrayList = new ArrayList<>();

                    for (int i = 0; i < timeJSONArray.length(); i++) {
                        double visibilityEntry = visibilityJSONArray.getDouble(i);

                        visibilityArrayList.add(new Entry(i, (float) visibilityEntry)); // Use the index i as x value
                    }

                    LineDataSet visibilityDataSet = new LineDataSet(visibilityArrayList, "Hourly Visibility");
                    visibilityDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
                    visibilityDataSet.setColor(Color.parseColor("#FFA500"));
                    visibilityDataSet.setLineWidth(3f);
                    visibilityDataSet.setFillAlpha(255);
                    visibilityDataSet.setDrawCircles(false); // Disable drawing circles at entry points

                    LineData visibilityData = new LineData(visibilityDataSet);
                    visibilityData.setDrawValues(false);

                    LineChart linechart5 = findViewById(R.id.chart5); // Assuming you have defined chart5 in your layout XML
                    linechart5.setData(visibilityData);

// Customize X-axis labels
                    XAxis xAxis5 = linechart5.getXAxis();
                    xAxis5.setGranularity(1f);
                    xAxis5.setLabelCount(2, true); // Set the number of labels to show (first and last)
                    xAxis5.setValueFormatter(new ValueFormatter() {
                        @Override
                        public String getFormattedValue(float value) {
                            int index = (int) value;
                            if (index == 0) {
                                return formatDate(timeJSONArray.optString(0, ""));
                            }
                            if (index == timeJSONArray.length() - 1) {
                                return formatDate(timeJSONArray.optString(timeJSONArray.length() - 1, ""));
                            }
                            return ""; // Return an empty string for all other indices
                        }

                        private String formatDate(String time) {
                            try {
                                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault());
                                Date date = inputFormat.parse(time);

                                SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM HH:mm", Locale.getDefault());
                                return outputFormat.format(date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            return "";
                        }
                    });

                    linechart5.setExtraOffsets(10f, 5f, 10f, 0f); // Adjust left and right margins
                    linechart5.invalidate();




                    // Show the JSON response as a toast
                    Toast.makeText(MainActivity.this, jsonResponse, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
