package de.muench.berlinstations.util;

import android.content.Context;
import de.muench.berlinstations.R;
import de.muench.berlinstations.model.Station;

import java.io.*;
import java.util.ArrayList;

public class CsvReader {

    public static ArrayList<Station> getTubeStations(Context context) {
        ArrayList<Station> stations = new ArrayList<>();

        try (InputStream inputStream = context.getResources().openRawResource(R.raw.stations);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] values = line.split(";");
                //leave out header
                if (values[1].equals("Name")) continue;
                String name = values[1];
                double latitude = Double.parseDouble(values[2].replace(",", "."));
                double longitude = Double.parseDouble(values[3].replace(",", "."));
                int difficulty = Integer.parseInt(values[4]);
                Station station = new Station(name, latitude, longitude, difficulty);
                stations.add(station);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return stations;
    }

    public static ArrayList<String> getTubeStationNames(Context context) {
        ArrayList<String> stationNames = new ArrayList<>();

        try (InputStream inputStream = context.getResources().openRawResource(R.raw.stations);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] values = line.split(";");
                //leave out header
                if (values[1].equals("Name")) continue;
                String name = values[1];
                stationNames.add(name);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return stationNames;
    }

    public static ArrayList<Station> getBusStations(Context context) {
        ArrayList<Station> stations = new ArrayList<>();

        try (InputStream inputStream = context.getResources().openRawResource(R.raw.filtered);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] values = line.split(",");
                //leave out header
                if (values[0].equals("Name")) continue;
                String name = values[0];
                double latitude = Double.parseDouble(values[1]);
                double longitude = Double.parseDouble(values[2]);
                int difficulty = 0;
                Station station = new Station(name, latitude, longitude, difficulty);
                stations.add(station);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return stations;
    }

    public static ArrayList<String> getBusStationNames(Context context) {
        ArrayList<String> stationNames = new ArrayList<>();

        try (InputStream inputStream = context.getResources().openRawResource(R.raw.filtered);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] values = line.split(",");
                //leave out header
                if (values[0].isBlank()) continue;
                if (values[0].equals("Name")) continue;
                String name = values[0];
                stationNames.add(name);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return stationNames;
    }
}
