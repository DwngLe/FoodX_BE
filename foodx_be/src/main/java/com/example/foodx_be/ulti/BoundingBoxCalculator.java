package com.example.foodx_be.ulti;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class BoundingBoxCalculator {
    // Earth radius in kilometers
    private static final BigDecimal EARTH_RADIUS = new BigDecimal("6371.0");
    private static final MathContext MATH_CONTEXT = new MathContext(15, RoundingMode.HALF_UP);

    public static BigDecimal[] calculateBoundingBox(BigDecimal latitude, BigDecimal longitude, BigDecimal radiusInKm) {
        BigDecimal latRadians = toRadians(latitude);
        BigDecimal lngRadians = toRadians(longitude);

        // Angular distance in radians on a great circle
        BigDecimal angularRadius = radiusInKm.divide(EARTH_RADIUS, MATH_CONTEXT);

        // Latitude bounds
        BigDecimal minLat = latRadians.subtract(angularRadius, MATH_CONTEXT);
        BigDecimal maxLat = latRadians.add(angularRadius, MATH_CONTEXT);

        // Longitude bounds
        BigDecimal deltaLng = BigDecimal.valueOf(
                Math.asin(Math.sin(angularRadius.doubleValue()) / Math.cos(latRadians.doubleValue())));

        BigDecimal minLng = lngRadians.subtract(deltaLng, MATH_CONTEXT);
        BigDecimal maxLng = lngRadians.add(deltaLng, MATH_CONTEXT);

        // Convert back to degrees
        minLat = toDegrees(minLat);
        maxLat = toDegrees(maxLat);
        minLng = toDegrees(minLng);
        maxLng = toDegrees(maxLng);

        return new BigDecimal[]{minLat, minLng, maxLat, maxLng};
    }

    private static BigDecimal toRadians(BigDecimal degrees) {
        return degrees.multiply(BigDecimal.valueOf(Math.PI)).divide(BigDecimal.valueOf(180), MATH_CONTEXT);
    }

    private static BigDecimal toDegrees(BigDecimal radians) {
        return radians.multiply(BigDecimal.valueOf(180)).divide(BigDecimal.valueOf(Math.PI), MATH_CONTEXT);
    }
}
