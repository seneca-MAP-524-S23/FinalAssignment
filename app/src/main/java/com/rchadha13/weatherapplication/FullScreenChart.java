package com.rchadha13.weatherapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class FullScreenChart extends AppCompatActivity {

    LineChart fullscreenlayout;
    TextView fullupdatedat, fullcity;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_chart);

        fullscreenlayout = findViewById(R.id.fullscreenlayout);
        fullupdatedat = findViewById(R.id.fullupdatedat);
        fullcity = findViewById(R.id.fullcity);
        sharedPreferences = getSharedPreferences("LocationData", MODE_PRIVATE);
        fullcity.setText(sharedPreferences.getString("currentcity", ""));
        // Retrieve the JSON string from SharedPreferences
        String jsonString = sharedPreferences.getString("data", null);
        JSONObject response;
        try {
            response = new JSONObject(jsonString);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        String timestampString = null;
        JSONObject hourly;
        JSONArray timeJSONArray;
        try {
            timestampString = response.getString("current_timestamp");
            // Extract time and temperature data from JSON arrays
            hourly = response.getJSONObject("hourly");
            timeJSONArray = hourly.getJSONArray("time");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        long timestamp = Long.parseLong(timestampString);
        Date date = new Date(timestamp * 1000); // Convert to milliseconds

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String formattedTime = sdf.format(date);

        fullupdatedat.setText("Updated At : " + formattedTime);


        // Retrieve parameters from the intent
        String chart = getIntent().getStringExtra("chart");
        if (chart.equals("temp")){
            JSONArray temperatureJSONArray = null;
            try {
                temperatureJSONArray = hourly.getJSONArray("temperature_2m");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            ArrayList<Entry> tempArrayList = new ArrayList<>();

            for (int i = 0; i < timeJSONArray.length(); i++) {
                try {
                    String time = timeJSONArray.getString(i); // Assuming the time is in a valid format
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                double temperatureentry = 0;
                try {
                    temperatureentry = temperatureJSONArray.getDouble(i);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                tempArrayList.add(new Entry(i, (float) temperatureentry)); // Use the index i as x value
            }

            LineDataSet set1 = new LineDataSet(tempArrayList, "Hourly Temperature");
            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
            set1.setColor(Color.RED);
            set1.setLineWidth(3f);
            fullscreenlayout.setExtraOffsets(10f, 5f, 10f, 0f); // Adjust left and right margins
            set1.setFillAlpha(255);
            set1.setDrawCircles(false); // Disable drawing circles at entry points

            LineData data = new LineData(set1);
            data.setDrawValues(false);

            fullscreenlayout.setData(data);

// Customize X-axis labels
            XAxis xAxis = fullscreenlayout.getXAxis();
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

            fullscreenlayout.invalidate();
        } else if (chart.equals("humi")) {
            JSONArray relativeHumidityJSONArray = null;
            try {
                relativeHumidityJSONArray = hourly.getJSONArray("relativehumidity_2m");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            ArrayList<Entry> humidityArrayList = new ArrayList<>();

            for (int i = 0; i < timeJSONArray.length(); i++) {
                double humidityEntry = 0;
                try {
                    humidityEntry = relativeHumidityJSONArray.getDouble(i);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

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
            // Assuming you have defined chart2 in your layout XML
            fullscreenlayout.setData(humidityData);

// Customize X-axis labels
            XAxis xAxis2 = fullscreenlayout.getXAxis();
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

            fullscreenlayout.setExtraOffsets(10f, 5f, 10f, 0f); // Adjust left and right margins
            fullscreenlayout.invalidate();
        } else if (chart.equals("prep")) {
            // Extract time and precipitation probability data from JSON arrays
            JSONArray precipitationProbabilityJSONArray = null;
            try {
                precipitationProbabilityJSONArray = hourly.getJSONArray("precipitation_probability");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            ArrayList<Entry> probabilityArrayList = new ArrayList<>();

            for (int i = 0; i < timeJSONArray.length(); i++) {
                double probabilityEntry = 0;
                try {
                    probabilityEntry = precipitationProbabilityJSONArray.getDouble(i);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

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

            LineChart fullscreenlayout = findViewById(R.id.fullscreenlayout); // Assuming you have defined chart3 in your layout XML
            fullscreenlayout.setData(probabilityData);

// Customize X-axis labels
            XAxis xAxis3 = fullscreenlayout.getXAxis();
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

            fullscreenlayout.setExtraOffsets(10f, 5f, 10f, 0f); // Adjust left and right margins
            fullscreenlayout.invalidate();
        } else if (chart.equals("wind")) {
// Extract time and wind speed data from JSON arrays
            JSONArray windSpeedJSONArray = null;
            try {
                windSpeedJSONArray = hourly.getJSONArray("windspeed_10m");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            ArrayList<Entry> windSpeedArrayList = new ArrayList<>();

            for (int i = 0; i < timeJSONArray.length(); i++) {
                double windSpeedEntry = 0;
                try {
                    windSpeedEntry = windSpeedJSONArray.getDouble(i);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

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

            LineChart fullscreenlayout = findViewById(R.id.fullscreenlayout); // Assuming you have defined chart4 in your layout XML
            fullscreenlayout.setData(windSpeedData);

// Customize X-axis labels
            XAxis xAxis4 = fullscreenlayout.getXAxis();
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

            fullscreenlayout.setExtraOffsets(10f, 5f, 10f, 0f); // Adjust left and right margins
            fullscreenlayout.invalidate();
        } else if (chart.equals("visi")) {
// Extract time and visibility data from JSON arrays
            JSONArray visibilityJSONArray = null;
            try {
                visibilityJSONArray = hourly.getJSONArray("visibility");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            ArrayList<Entry> visibilityArrayList = new ArrayList<>();

            for (int i = 0; i < timeJSONArray.length(); i++) {
                double visibilityEntry = 0;
                try {
                    visibilityEntry = visibilityJSONArray.getDouble(i);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

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

            LineChart fullscreenlayout = findViewById(R.id.fullscreenlayout); // Assuming you have defined chart5 in your layout XML
            fullscreenlayout.setData(visibilityData);

// Customize X-axis labels
            XAxis xAxis5 = fullscreenlayout.getXAxis();
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

            fullscreenlayout.setExtraOffsets(10f, 5f, 10f, 0f); // Adjust left and right margins
            fullscreenlayout.invalidate();
        }

    }
}