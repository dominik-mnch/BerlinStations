package de.muench.berlinstations.calculation;

import de.muench.berlinstations.model.Station;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.ml.clustering.Cluster;

import java.util.*;

public class StationSelector {

    private enum Mode { BAHN, TRAM, BUS }

    private static class StationPoint implements Clusterable {
        private final Station station;
        private final double[] point;
        private final Mode mode;

        public StationPoint(Station station, Mode mode) {
            this.station = station;
            this.mode = mode;
            this.point = new double[]{station.getLatitude(), station.getLongitude()};
        }

        @Override
        public double[] getPoint() {
            return point;
        }

        public Station getStation() { return station; }
        public Mode getMode() { return mode; }
    }

    public static ArrayList<Station> selectBalancedStationsGlobally(
            ArrayList<Station> bahnStations, int bahnCount,
            ArrayList<Station> tramStations, int tramCount,
            ArrayList<Station> busStations, int busCount
    ) {

        int totalToSelect = bahnCount + tramCount + busCount;
        if (totalToSelect == 0) return new ArrayList<>();

        // Build unified point list
        List<StationPoint> allPoints = new ArrayList<>();

        for (Station s : bahnStations) allPoints.add(new StationPoint(s, Mode.BAHN));
        for (Station s : tramStations) allPoints.add(new StationPoint(s, Mode.TRAM));
        for (Station s : busStations)  allPoints.add(new StationPoint(s, Mode.BUS));

        // If requested more than existing
        if (totalToSelect >= allPoints.size()) {
            ArrayList<Station> all = new ArrayList<>();
            allPoints.forEach(p -> all.add(p.getStation()));
            return all;
        }

        // Run K-Means on ALL stations
        KMeansPlusPlusClusterer<StationPoint> kmeans =
                new KMeansPlusPlusClusterer<>(totalToSelect, 1000);

        List<CentroidCluster<StationPoint>> clusters = kmeans.cluster(allPoints);

        // Quota counters
        int remainingBahn = bahnCount;
        int remainingTram = tramCount;
        int remainingBus = busCount;

        ArrayList<Station> result = new ArrayList<>();
        Random rand = new Random();

        // STEP 1: Try to satisfy quotas using cluster-local choices
        for (Cluster<StationPoint> c : clusters) {
            List<StationPoint> pts = c.getPoints();
            if (pts.isEmpty()) continue;

            List<StationPoint> bahns = new ArrayList<>();
            List<StationPoint> trams = new ArrayList<>();
            List<StationPoint> buses = new ArrayList<>();

            for (StationPoint sp : pts) {
                switch (sp.getMode()) {
                    case BAHN: bahns.add(sp);
                    case TRAM: trams.add(sp);
                    case BUS: buses.add(sp);
                }
            }

            Station selected = null;

            if (remainingBahn > 0 && !bahns.isEmpty()) {
                selected = bahns.get(rand.nextInt(bahns.size())).getStation();
                remainingBahn--;
            } else if (remainingTram > 0 && !trams.isEmpty()) {
                selected = trams.get(rand.nextInt(trams.size())).getStation();
                remainingTram--;
            } else if (remainingBus > 0 && !buses.isEmpty()) {
                selected = buses.get(rand.nextInt(buses.size())).getStation();
                remainingBus--;
            }

            if (selected != null) result.add(selected);
        }

        // STEP 2: If some quota is still unfilled (rare), fill from the remaining pool
        if (remainingBahn + remainingTram + remainingBus > 0) {
            for (StationPoint sp : allPoints) {
                if (remainingBahn > 0 && sp.getMode() == Mode.BAHN) {
                    result.add(sp.getStation());
                    remainingBahn--;
                } else if (remainingTram > 0 && sp.getMode() == Mode.TRAM) {
                    result.add(sp.getStation());
                    remainingTram--;
                } else if (remainingBus > 0 && sp.getMode() == Mode.BUS) {
                    result.add(sp.getStation());
                    remainingBus--;
                }

                if (remainingBahn + remainingTram + remainingBus == 0)
                    break;
            }
        }

        return result;
    }
}