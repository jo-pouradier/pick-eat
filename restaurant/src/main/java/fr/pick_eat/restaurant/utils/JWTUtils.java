package fr.pick_eat.restaurant.utils;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

public class JWTUtils {
    public static String extractUserId(String jwt) {
        List<String> parts = Arrays.asList(jwt.replace("Bearer ", "").split("\\."));
        String payload = parts.get(1);
        try {
            JSONObject decodedPayload = new JSONObject(new String(java.util.Base64.getDecoder().decode(payload)));
            return decodedPayload.getString("id");
        } catch (JSONException e) {
            throw new RuntimeException("Error while decoding JWT payload");
        }

    }

}
