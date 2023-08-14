package com.rchadha13.weatherapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder> {

    private Context context;
    private List<PlaceInfo> placeOptions = new ArrayList<>(); // Add place options to this list

    public PlaceAdapter(Context context) {
        this.context = context;

        // Populate the placeOptions list with example city names and coordinates
        PlaceInfo[] places = {
                new PlaceInfo("Toronto, Ontario", 43.651070, -79.347015),
                new PlaceInfo("Vancouver, British Columbia", 49.282729, -123.120738),
                new PlaceInfo("Montreal, Quebec", 45.501690, -73.567256),
                new PlaceInfo("Calgary, Alberta", 51.044270, -114.062019),
                new PlaceInfo("Ottawa, Ontario", 45.421530, -75.697193),
                new PlaceInfo("Edmonton, Alberta", 53.544389, -113.490927),
                new PlaceInfo("Quebec City, Quebec", 46.813878, -71.207981),
                new PlaceInfo("Winnipeg, Manitoba", 49.895138, -97.138374),
                new PlaceInfo("Halifax, Nova Scotia", 44.648618, -63.585948),
                new PlaceInfo("Victoria, British Columbia", 48.428421, -123.365644),
                new PlaceInfo("New York City, New York", 40.712776, -74.005974),
                new PlaceInfo("Los Angeles, California", 34.052235, -118.243683),
                new PlaceInfo("Chicago, Illinois", 41.878113, -87.629799),
                new PlaceInfo("Houston, Texas", 29.760427, -95.369804),
                new PlaceInfo("Phoenix, Arizona", 33.448376, -112.074036),
                new PlaceInfo("Philadelphia, Pennsylvania", 39.952583, -75.165222),
                new PlaceInfo("San Antonio, Texas", 29.424122, -98.493629),
                new PlaceInfo("San Diego, California", 32.715328, -117.157256),
                new PlaceInfo("Dallas, Texas", 32.776665, -96.796989),
                new PlaceInfo("San Jose, California", 37.354107, -121.955238)
        };

        placeOptions.addAll(Arrays.asList(places));
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_place, parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        PlaceInfo placeInfo = placeOptions.get(position);
        holder.bind(placeInfo);
    }

    @Override
    public int getItemCount() {
        return placeOptions.size();
    }

    public class PlaceViewHolder extends RecyclerView.ViewHolder {
        private TextView placeTextView;
        private PlaceInfo currentPlace;

        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            placeTextView = itemView.findViewById(R.id.placeTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentPlace != null) {
                        String message = currentPlace.getName() + "\nLat: " + currentPlace.getLatitude() + "\nLong: " + currentPlace.getLongitude();
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        JSONObject placeJson = new JSONObject();
                        try {
                            placeJson.put("name", currentPlace.getName());
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            placeJson.put("latitude", currentPlace.getLatitude());
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            placeJson.put("longitude", currentPlace.getLongitude());
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        SharedPreferences sharedPreferences = itemView.getContext().getSharedPreferences("LocationData", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("selectedPlace", placeJson.toString());
                        editor.apply();

                        // Create an Intent for MainActivity
                        Intent intent = new Intent(itemView.getContext(), MainActivity.class);

// Add flags to start a new task and clear the existing task
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

// Start the activity using the updated Intent
                        itemView.getContext().startActivity(intent);

                    }
                }
            });
        }

        public void bind(PlaceInfo placeInfo) {
            currentPlace = placeInfo;
            placeTextView.setText(placeInfo.getName());
        }
    }
}
