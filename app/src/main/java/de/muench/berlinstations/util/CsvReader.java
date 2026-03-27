package de.muench.berlinstations.util;

import android.content.Context;
import de.muench.berlinstations.R;
import de.muench.berlinstations.model.Station;

import java.io.*;
import java.util.ArrayList;

public class CsvReader {

    public static ArrayList<Station> getBahnStations(Context context) {
        ArrayList<Station> stations = new ArrayList<>();

        try (InputStream inputStream = context.getResources().openRawResource(R.raw.bahn);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] values = line.split(",");
                //leave out header
                if (values[1].equals("name")) continue;
                String name = values[1];
                double latitude = Double.parseDouble(values[2].replace(",", "."));
                double longitude = Double.parseDouble(values[3].replace(",", "."));
                Station station = new Station(name, latitude, longitude);
                stations.add(station);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return stations;
    }

    public static ArrayList<String> getBahnStationNames(Context context) {
        ArrayList<String> stationNames = new ArrayList<>();

        try (InputStream inputStream = context.getResources().openRawResource(R.raw.bahn);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] values = line.split(",");
                //leave out header
                if (values[1].equals("name")) continue;
                String name = values[1];
                stationNames.add(name);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return stationNames;
    }

    public static ArrayList<Station> getTramStations(Context context) {
        ArrayList<Station> stations = new ArrayList<>();

        try (InputStream inputStream = context.getResources().openRawResource(R.raw.tram);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] values = line.split(",");
                //leave out header
                if (values[1].equals("name")) continue;
                String name = values[1];
                double latitude = Double.parseDouble(values[2]);
                double longitude = Double.parseDouble(values[3]);
                Station station = new Station(name, latitude, longitude);
                stations.add(station);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return stations;
    }

    public static ArrayList<String> getTramStationNames(Context context) {
        ArrayList<String> stationNames = new ArrayList<>();

        try (InputStream inputStream = context.getResources().openRawResource(R.raw.tram);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] values = line.split(",");
                //leave out header
                if (values[1].equals("name")) continue;
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

        try (InputStream inputStream = context.getResources().openRawResource(R.raw.bus);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] values = line.split(",");
                //leave out header
                if (values[1].equals("name")) continue;
                String name = values[1];
                double latitude = Double.parseDouble(values[2]);
                double longitude = Double.parseDouble(values[3]);
                Station station = new Station(name, latitude, longitude);
                stations.add(station);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return stations;
    }

    public static ArrayList<String> getBusStationNames(Context context) {
        ArrayList<String> stationNames = new ArrayList<>();

        try (InputStream inputStream = context.getResources().openRawResource(R.raw.bus);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] values = line.split(",");
                //leave out header
                if (values[1].equals("name")) continue;
                String name = values[1];
                stationNames.add(name);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return stationNames;
    }
}
