package fr.pick_eat.restaurant.utils;

import java.util.List;

public class CoordCalcul {
    public static List<Float> getAreaFromRadius(float lat, float lon, float radius) {
//            Get the area from a radius
//            :param radius: meters
        float radiusInDegrees = radius / 111000f;
        float minLat = lat - radiusInDegrees;
        float maxLat = lat + radiusInDegrees;
        float minLon = lon - radiusInDegrees;
        float maxLon = lon + radiusInDegrees;
        return List.of(minLat, maxLat, minLon, maxLon);
    }
}
