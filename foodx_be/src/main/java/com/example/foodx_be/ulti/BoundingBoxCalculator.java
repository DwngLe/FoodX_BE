package com.example.foodx_be.ulti;

public class BoundingBoxCalculator {
    // Earth radius in kilometers
    private static final double EARTH_RADIUS = 6371.0;

    public static double[] calculateBoundingBox(double latitude, double longitude, double radiusInKm) {
        double latRadians = Math.toRadians(latitude);
        double lngRadians = Math.toRadians(longitude);

        // Angular distance in radians on a great circle
        double angularRadius = radiusInKm / EARTH_RADIUS;

        // Latitude bounds
        double minLat = latRadians - angularRadius;
        double maxLat = latRadians + angularRadius;

        // Longitude bounds
        double deltaLng = Math.asin(Math.sin(angularRadius) / Math.cos(latRadians));
        double minLng = lngRadians - deltaLng;
        double maxLng = lngRadians + deltaLng;

        // Convert back to degrees
        minLat = Math.toDegrees(minLat);
        maxLat = Math.toDegrees(maxLat);
        minLng = Math.toDegrees(minLng);
        maxLng = Math.toDegrees(maxLng);

        return new double[]{minLat, minLng, maxLat, maxLng};
    }
}
