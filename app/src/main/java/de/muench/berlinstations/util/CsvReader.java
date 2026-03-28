package de.muench.berlinstations.util;

import android.content.Context;
import de.muench.berlinstations.R;
import de.muench.berlinstations.model.Station;
import de.muench.berlinstations.model.StationType;

import java.io.*;
import java.util.ArrayList;

public class CsvReader {

    public static ArrayList<Station> getStations(Context context) {
        ArrayList<Station> stations = new ArrayList<>();
        stations.addAll(getBahnStations(context));
        stations.addAll(getTramStations(context));
        stations.addAll(getBusStations(context));
        return stations;
    }

    public static ArrayList<String> getStationNames(Context context) {
        ArrayList<String> stationNames = new ArrayList<>();
        stationNames.addAll(getBahnStationNames(context));
        stationNames.addAll(getTramStationNames(context));
        stationNames.addAll(getBusStationNames(context));
        return stationNames;
    }

    private static ArrayList<Station> getBahnStations(Context context) {
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
                Station station = new Station(name, StationType.BAHN, latitude, longitude);
                stations.add(station);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return stations;
    }

    private static ArrayList<String> getBahnStationNames(Context context) {
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

    private static ArrayList<Station> getTramStations(Context context) {
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
                Station station = new Station(name, StationType.TRAM, latitude, longitude);
                stations.add(station);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return stations;
    }

    private static ArrayList<String> getTramStationNames(Context context) {
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

    private static ArrayList<Station> getBusStations(Context context) {
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
                Station station = new Station(name, StationType.BUS, latitude, longitude);
                stations.add(station);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return stations;
    }

    private static ArrayList<String> getBusStationNames(Context context) {
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
