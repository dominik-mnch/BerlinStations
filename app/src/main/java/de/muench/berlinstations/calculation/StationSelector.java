package de.muench.berlinstations.calculation;

import de.muench.berlinstations.model.Station;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;

import java.util.*;

public class StationSelector {

    // Wrapper for K-Means
    private static class StationPoint implements Clusterable {
        private final double[] point;
        private final Station station;

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

    public static ArrayList<Station> selectBalancedStations(ArrayList<Station> stations, int numToSelect) {
        if (stations.isEmpty() || numToSelect <= 0) {
            return new ArrayList<>();
        }

        // If user requests more stations than available
        if (numToSelect >= stations.size()) {
            return new ArrayList<>(stations);
        }

        // Wrap all stations
        List<StationPoint> points = new ArrayList<>();
        for (Station s : stations) {
            points.add(new StationPoint(s));
        }

        // K-Means clustering
        KMeansPlusPlusClusterer<StationPoint> kmeans = new KMeansPlusPlusClusterer<>(numToSelect, 1000);
        List<CentroidCluster<StationPoint>> clusters = kmeans.cluster(points);

        Random rand = new Random();
        ArrayList<Station> selectedStations = new ArrayList<>();

        // Pick one station per cluster randomly
        for (Cluster<StationPoint> cluster : clusters) {
            List<StationPoint> clusterPoints = cluster.getPoints();
            if (!clusterPoints.isEmpty()) {
                selectedStations.add(clusterPoints.get(rand.nextInt(clusterPoints.size())).getStation());
            }
        }

        return selectedStations;
    }
}

