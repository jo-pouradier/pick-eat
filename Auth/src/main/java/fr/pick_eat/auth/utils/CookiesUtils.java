package fr.pick_eat.auth.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.pick_eat.auth.dto.UserBasicDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class CookiesUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static long EXPIRATION_TIME = 3600;

    @Value("${security.jwt.expiration-time}")
    public void setExpirationTime(long expirationTime) {
        EXPIRATION_TIME = expirationTime;
        System.out.println("Expiration time set to: " + EXPIRATION_TIME);
    }

    public static void addUserCookie(UserBasicDto user) {
        String userJson = "null";
        try {
            userJson = objectMapper.writeValueAsString(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (userJson.isBlank()) {
            userJson = "null";
        }
        String encodedUserJson = URLEncoder.encode(userJson, StandardCharsets.UTF_8);

        // Create a custom cookie
        Cookie cookie = new Cookie("user", encodedUserJson);
        cookie.setHttpOnly(false);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge((int) EXPIRATION_TIME); // 1 hour

        // Add the cookie to the response
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        response.addCookie(cookie);
    }

    public static void removeUserCookie() {
        // Create a custom cookie
        Cookie cookie = new Cookie("user", "null");
        cookie.setHttpOnly(false);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        // Add the cookie to the response
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        response.addCookie(cookie);
    }
}
