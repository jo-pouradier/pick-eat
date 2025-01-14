package fr.pick_eat.auth.filter;

import fr.pick_eat.auth.entity.UserBasic;
import fr.pick_eat.auth.service.AuthenticationService;
import fr.pick_eat.auth.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final Logger logger = Logger.getLogger(JwtAuthenticationFilter.class.getName());

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    public JwtAuthenticationFilter(@Lazy JwtService jwtService, @Lazy AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
        logger.setLevel(java.util.logging.Level.OFF);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Cookie jwtCookie = Arrays.stream(cookies)
                    .filter(cookie -> "jwt".equals(cookie.getName()))
                    .findFirst()
                    .orElse(null);

            if (jwtCookie != null) {
                String jwt = jwtCookie.getValue();
                if (jwtService.isTokenExpired(jwt)) {
                    response.addCookie(jwtService.generateDeleteCookie());
                    filterChain.doFilter(request, response);
                    return;
                }
                String username = jwtService.extractUsername(jwt);
                String roles = jwtService.extractRoles(jwt);
                UUID uuid = jwtService.extractUuid(jwt);
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    Optional<UserBasic> userDetailsOptional = this.authenticationService.findByEmail(username);
                    UserDetails userDetails = null;
                    logger.info(username + " " + roles + " " + uuid);
                    if (userDetailsOptional.isEmpty()) {
                        if (username.endsWith("@guest.com")) {
                            userDetails = User.withUsername(username).authorities(roles.split(",")).password("").build();
                            logger.info(request.getRequestURI() + "\njwtCookie = " + jwtCookie.getValue().substring(250) + "\nGuest found: " + userDetails.getUsername());
                        } else {
                            logger.info(request.getRequestURI() + "\njwtCookie = " + jwtCookie.getValue().substring(250) + "\nUser/Guest not found");
                        }
                    } else {
                        userDetails = userDetailsOptional.get();
                        logger.info(request.getRequestURI() + "\njwtCookie = " + jwtCookie.getValue().substring(250) + "\nUser found: " + userDetails.getUsername());
                    }
                    if (userDetails != null) {
                        if (jwtService.validateToken(jwt, userDetails)) {
                            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());
                            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        }
                    }
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}