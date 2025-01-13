package fr.pick_eat.event.utils;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.security.oauth2.jwt.JwtDecoder;

public class JWTUtils {

    public static String extractUserIdCookie(String jwt, JwtDecoder decoder) {
        return decoder.decode(jwt).getClaimAsString("uuid");
        
    }
    public static String extractUserIdAuth(String jwt) {
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
