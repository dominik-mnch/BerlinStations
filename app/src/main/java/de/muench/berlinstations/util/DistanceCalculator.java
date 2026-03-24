package de.muench.berlinstations.util;

public class DistanceCalculator {

    /**
     * Calculates approximate distance between two latitude/longitude points
     * assuming a small area (flat Earth approximation).
     *
     * @param lat1 Latitude of the first point in decimal degrees
     * @param lon1 Longitude of the first point in decimal degrees
     * @param lat2 Latitude of the second point in decimal degrees
     * @param lon2 Longitude of the second point in decimal degrees
     * @return Distance in kilometers
     */
    public static double calculateDistanceKm(double lat1, double lon1, double lat2, double lon2) {
        // Approximate conversion factors
        final double KM_PER_DEGREE_LAT = 111.32; // 1 degree latitude ≈ 111.32 km
        final double KM_PER_DEGREE_LON = 111.32 * Math.cos(Math.toRadians((lat1 + lat2) / 2));

        double deltaLatKm = (lat2 - lat1) * KM_PER_DEGREE_LAT;
        //Log.d("Distance", "Latitude: " + deltaLatKm);
        double deltaLonKm = (lon2 - lon1) * KM_PER_DEGREE_LON;
        //Log.d("Distance", "Longitude: " + deltaLonKm);

        return Math.sqrt(deltaLatKm * deltaLatKm + deltaLonKm * deltaLonKm);
    }
}
