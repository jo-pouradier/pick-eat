package fr.pick_eat.restaurant.utils;

import java.util.List;

public class CoordCalcul {
    public static List<Double> getAreaFromRadius(double lat, double lon, double radius) {
//            Get the area from a radius
//            :param radius: meters
        double radiusInDegrees = radius / 111000f;
        double minLat = lat - radiusInDegrees;
        double maxLat = lat + radiusInDegrees;
        double minLon = lon - radiusInDegrees;
        double maxLon = lon + radiusInDegrees;
        return List.of(minLat, maxLat, minLon, maxLon);
    }
}
