package fr.pick_eat.auth.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.pick_eat.auth.dto.UserBasicDto;
import fr.pick_eat.auth.entity.UserBasic;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class CookiesUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

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
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(3600); // 1 hour

        // Add the cookie to the response
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        response.addCookie(cookie);
    }

    public static void removeUserCookie() {
        // Create a custom cookie
        Cookie cookie = new Cookie("user", "");
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        // Add the cookie to the response
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        response.addCookie(cookie);
    }
}
