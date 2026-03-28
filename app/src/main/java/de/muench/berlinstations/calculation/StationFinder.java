package de.muench.berlinstations.calculation;

import android.content.Context;
import de.muench.berlinstations.model.Station;
import de.muench.berlinstations.util.DistanceCalculator;
import de.muench.berlinstations.util.CsvReader;

import java.util.ArrayList;
import java.util.Random;

public class StationFinder {

    private final String startingStation;
    private final double minDistance;
    private final double maxDistance;
    private final double tramWeight;
    private final double busWeight;
    private final int numResults;

    public StationFinder(String startingStation, double minDistance, double tramWeight, double busWeight, int numResults) {
        this.startingStation = startingStation;
        this.minDistance = minDistance;
        this.maxDistance = Double.POSITIVE_INFINITY;
        this.tramWeight = tramWeight;
        this.busWeight = busWeight;
        this.numResults = numResults;
    }

    public StationFinder(String startingStation, double minDistance, double maxDistance, double tramWeight, double busWeight, int numResults) {
        this.startingStation = startingStation;
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.tramWeight = tramWeight;
        this.busWeight = busWeight;
        this.numResults = numResults;
    }

    public ArrayList<Station> filterStations(Context context) {
        ArrayList<Station> stations = new ArrayList<>(CsvReader.getStations(context));

        int bahnCount = 0;
        int tramCount = 0;
        int busCount = 0;

        Random random = new Random();

        for (int i = 0; i < numResults; i++) {
            double r = random.nextDouble();

            if (Double.compare(r, tramWeight) < 0) {
                tramCount++;
            } else if (Double.compare(r, tramWeight + busWeight) < 0) {
                busCount++;
            } else {
                bahnCount++;
            }
        }

        Station startStation = getStationByName(stations, startingStation);

        ArrayList<Station> filteredStations = new ArrayList<>(filterStations(stations, startStation));

        return StationSelector.selectBalancedStationsGlobally(filteredStations, bahnCount, tramCount, busCount);
    }

    private ArrayList<Station> filterStations(ArrayList<Station> stations, Station startingStation) {
        ArrayList<Station> filteredStations = new ArrayList<>();
        for (Station station : stations) {
            if (station.equals(startingStation)) continue;

            double distance = DistanceCalculator.calculateDistanceKm(startingStation.getLatitude(), startingStation.getLongitude(),
                    station.getLatitude(), station.getLongitude());

            if (distance < minDistance) continue;
            if (distance > maxDistance) continue;

            filteredStations.add(station);
        }

        return filteredStations;
    }

    private Station getStationByName(ArrayList<Station> stations, String name) {
        for (Station station : stations) {
            if (station.getName().equals(name)) {
                return station;
            }
        }
        return null;
    }
}
