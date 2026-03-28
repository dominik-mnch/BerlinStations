package de.muench.berlinstations.calculation;

import de.muench.berlinstations.model.Station;
import de.muench.berlinstations.model.StationType;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.ml.clustering.Cluster;

import java.util.*;

public class StationSelector {

    private static class StationPoint implements Clusterable {
        private final Station station;
        private final double[] point;

        public StationPoint(Station station) {
            this.station = station;
            this.point = new double[]{station.getLatitude(), station.getLongitude()};
        }

        @Override
        public double[] getPoint() {
            return point;
        }

        public Station getStation() {
            return station;
        }
    }

    /**
     * Select stations globally distributed with exact quotas per StationType.
     *
     * @param stations  all available stations
     * @param bahnCount number of BAHN stations to select
     * @param tramCount number of TRAM stations to select
     * @param busCount  number of BUS stations to select
     * @return list of selected stations
     */
    public static ArrayList<Station> selectBalancedStationsGlobally(
            List<Station> stations,
            int bahnCount,
            int tramCount,
            int busCount
    ) {
        int totalToSelect = bahnCount + tramCount + busCount;
        if (totalToSelect == 0 || stations.isEmpty()) return new ArrayList<>();

        // If requested more than available, return all
        if (totalToSelect >= stations.size()) {
            return new ArrayList<>(stations);
        }

        // Wrap stations for clustering
        List<StationPoint> points = new ArrayList<>();
        for (Station s : stations) {
            points.add(new StationPoint(s));
        }

        // K-Means clustering
        KMeansPlusPlusClusterer<StationPoint> kmeans =
                new KMeansPlusPlusClusterer<>(totalToSelect, 1000);
        List<CentroidCluster<StationPoint>> clusters = kmeans.cluster(points);

        // Quota counters
        int remainingBahn = bahnCount;
        int remainingTram = tramCount;
        int remainingBus = busCount;

        ArrayList<Station> result = new ArrayList<>();
        Random rand = new Random();

        // STEP 1: select stations cluster by cluster
        for (Cluster<StationPoint> cluster : clusters) {
            List<StationPoint> pts = cluster.getPoints();
            if (pts.isEmpty()) continue;

            List<StationPoint> bahns = new ArrayList<>();
            List<StationPoint> trams = new ArrayList<>();
            List<StationPoint> buses = new ArrayList<>();

            for (StationPoint sp : pts) {
                switch (sp.getStation().getType()) {
                    case BAHN:
                        bahns.add(sp);
                        break;
                    case TRAM:
                        trams.add(sp);
                        break;
                    case BUS:
                        buses.add(sp);
                        break;
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

        // STEP 2: fill remaining quotas if some not yet selected
        if (remainingBahn + remainingTram + remainingBus > 0) {
            for (StationPoint sp : points) {
                StationType type = sp.getStation().getType();
                if (remainingBahn > 0 && type == StationType.BAHN) {
                    result.add(sp.getStation());
                    remainingBahn--;
                } else if (remainingTram > 0 && type == StationType.TRAM) {
                    result.add(sp.getStation());
                    remainingTram--;
                } else if (remainingBus > 0 && type == StationType.BUS) {
                    result.add(sp.getStation());
                    remainingBus--;
                }

                if (remainingBahn + remainingTram + remainingBus == 0) break;
            }
        }

        return result;
    }
}