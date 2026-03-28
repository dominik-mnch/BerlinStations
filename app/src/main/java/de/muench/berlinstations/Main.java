package de.muench.berlinstations;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import de.muench.berlinstations.calculation.StationFinder;
import de.muench.berlinstations.model.Station;
import de.muench.berlinstations.util.CsvReader;

import java.util.ArrayList;

public class Main extends AppCompatActivity {

    private AutoCompleteTextView autoCompleteTextView;
    private EditText minDistanceEditText, maxDistanceEditText;
    private EditText tramWeightEditText, busWeightEditText;
    private EditText numStationsEditText;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_screen);

        // Bind UI elements
        autoCompleteTextView = findViewById(R.id.stationAutoComplete);
        minDistanceEditText = findViewById(R.id.minDistanceEditText);
        maxDistanceEditText = findViewById(R.id.maxDistanceEditText);
        tramWeightEditText = findViewById(R.id.tramWeightEditText);
        busWeightEditText = findViewById(R.id.busWeightEditText);
        numStationsEditText = findViewById(R.id.numStationsEditText);
        submitButton = findViewById(R.id.submitButton);

        // Example stations in London
        ArrayList<String> stationNames = CsvReader.getBahnStationNames(this);
        ArrayList<String> tramStationNames = CsvReader.getTramStationNames(this);
        ArrayList<String> busStationNames = CsvReader.getBusStationNames(this);
        stationNames.addAll(tramStationNames);
        stationNames.addAll(busStationNames);
        Log.d("stationNames", stationNames.toString());
        String[] stations = new String[stationNames.size()];
        stations = stationNames.toArray(stations);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, stations);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setThreshold(3);

        // Button action
        submitButton.setOnClickListener(view -> {
            String station = autoCompleteTextView.getText().toString().trim();
            String minDistanceString = minDistanceEditText.getText().toString();
            String maxDistanceString = maxDistanceEditText.getText().toString();
            String tramWeightString = tramWeightEditText.getText().toString();
            String busWeightString = busWeightEditText.getText().toString();
            String numResultsString = numStationsEditText.getText().toString();

            if (station.isEmpty() || minDistanceString.isEmpty() || numResultsString.isEmpty()) {
                Toast.makeText(Main.this, "Please fill in required fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!stationNames.contains(station)) {
                Toast.makeText(Main.this, "Please select a valid station", Toast.LENGTH_SHORT).show();
            }

            double minDistance = parseDouble(minDistanceString, "minimum distance");
            double maxDistance = -1.0;
            if (!maxDistanceString.isEmpty()) {
                maxDistance = parseDouble(maxDistanceString, "maximum distance");
            }
            double tramWeight = 0;
            if (!tramWeightString.isEmpty()) {
                tramWeight = parseDouble(tramWeightString, "tram weight");
            }
            double busWeight = 0;
            if (!busWeightString.isEmpty()) {
                busWeight = parseDouble(busWeightString, "bus weight");
            }
            int numStations = parseInt(numResultsString, "number of stations");

            //return if any value was invalid
            if (Double.isNaN(minDistance) || Double.isNaN(maxDistance) || Double.isNaN(tramWeight) || Double.isNaN(busWeight) || numStations == -1)
                return;

            if (Double.compare(tramWeight + busWeight, 1) > 0) {
                Toast.makeText(Main.this, "Combined bus weight and tram weight is too big!", Toast.LENGTH_SHORT).show();
                return;
            }

            StationFinder stationFinder;
            //both optionals unset
            if (maxDistance == -1.0) {
                stationFinder = new StationFinder(station, minDistance, tramWeight, busWeight, numStations);
            } else {
                stationFinder = new StationFinder(station, minDistance, maxDistance, tramWeight, busWeight, numStations);
            }

            ArrayList<Station> filteredStations = stationFinder.filterStations(this);

            Intent intent = new Intent(this, MapActivity.class);
            intent.putParcelableArrayListExtra("filteredStations", filteredStations);
            startActivity(intent);
        });
    }

    private double parseDouble(String value, String fieldName) {
        double doubleValue;
        try {
            doubleValue = Double.parseDouble(value);
        } catch (NumberFormatException e) {
            Toast.makeText(Main.this, "Please enter a valid " + fieldName, Toast.LENGTH_SHORT).show();
            return Double.NaN;
        }
        if (doubleValue < 0) {
            Toast.makeText(Main.this, "Please enter a valid " + fieldName, Toast.LENGTH_SHORT).show();
            return Double.NaN;
        }
        return doubleValue;
    }

    private int parseInt(String value, String fieldName) {
        int intValue;
        try {
            intValue = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            Toast.makeText(Main.this, "Please enter a valid " + fieldName, Toast.LENGTH_SHORT).show();
            return -1;
        }
        if (intValue < 0) {
            Toast.makeText(Main.this, "Please enter a valid " + fieldName, Toast.LENGTH_SHORT).show();
            return -1;
        }
        return intValue;
    }
}
