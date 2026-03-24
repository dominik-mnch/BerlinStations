package de.muench.berlinstations.calculation;

import android.content.Context;
import de.muench.berlinstations.model.Station;
import de.muench.berlinstations.util.DistanceCalculator;
import de.muench.berlinstations.util.CsvReader;

import java.util.ArrayList;

public class StationFinder {

    private final String startingStation;
    private final double minDistance;
    private final double maxDistance;
    private final int minDifficulty;
    private final int maxDifficulty;
    private final int numResults;

    public StationFinder(String startingStation, double minDistance, int minDifficulty, int numResults) {
        this.startingStation = startingStation;
        this.minDistance = minDistance;
        this.maxDistance = Double.POSITIVE_INFINITY;
        this.minDifficulty = minDifficulty;
        this.maxDifficulty = Integer.MAX_VALUE;
        this.numResults = numResults;
    }

    public StationFinder(String startingStation, double minDistance, double maxDistance, int minDifficulty, int numResults) {
        this.startingStation = startingStation;
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.minDifficulty = minDifficulty;
        this.maxDifficulty = Integer.MAX_VALUE;
        this.numResults = numResults;
    }

    public StationFinder(String startingStation, double minDistance, int minDifficulty, int maxDifficulty, int numResults) {
        this.startingStation = startingStation;
        this.minDistance = minDistance;
        this.maxDistance = Double.POSITIVE_INFINITY;
        this.minDifficulty = minDifficulty;
        this.maxDifficulty = maxDifficulty;
        this.numResults = numResults;
    }

    public StationFinder(String startingStation, double minDistance, double maxDistance, int minDifficulty, int maxDifficulty, int numResults) {
        this.startingStation = startingStation;
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.minDifficulty = minDifficulty;
        this.maxDifficulty = maxDifficulty;
        this.numResults = numResults;
    }

    public ArrayList<Station> filterStations(Context context) {
        ArrayList<Station> tubeStations = CsvReader.getTubeStations(context);
        ArrayList<Station> busStations = CsvReader.getBusStations(context);
        ArrayList<Station> allStations = new ArrayList<>(tubeStations);
        allStations.addAll(busStations);

        Station startingStation = getStationByName(allStations, this.startingStation);

        //bus stations excluded
        if (minDifficulty > 0) {
            return StationSelector.selectBalancedStations(filterStations(tubeStations, startingStation), numResults);
        }

        ArrayList<Station> filteredTubeStations = filterStations(tubeStations, startingStation);
        ArrayList<Station> filteredBusStations = filterStations(busStations, startingStation);

        ArrayList<Station> selectedTubeStations = StationSelector.selectBalancedStations(filteredTubeStations, (int) Math.ceil(numResults/2.0));
        ArrayList<Station> selectedBusStations = StationSelector.selectBalancedStations(filteredBusStations, (int) Math.floor(numResults/2.0));
        selectedTubeStations.addAll(selectedBusStations);
        return selectedTubeStations;
    }

    private ArrayList<Station> filterStations(ArrayList<Station> stations, Station startingStation) {
        ArrayList<Station> filteredStations = new ArrayList<>();
        for (Station station : stations) {
            if (station.equals(startingStation)) continue;

            if (station.getDifficulty() < minDifficulty) continue;
            if (station.getDifficulty() > maxDifficulty) continue;

            //Log.d("Distance", "Calculating " + station.getName());
            double distance = DistanceCalculator.calculateDistanceKm(startingStation.getLatitude(), startingStation.getLongitude(),
                    station.getLatitude(), station.getLongitude());
            //Log.d("Distance", "Calculated distance: " + distance);

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
