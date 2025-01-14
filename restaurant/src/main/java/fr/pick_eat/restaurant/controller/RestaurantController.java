package fr.pick_eat.restaurant.controller;

import dto.EventDTO;
import fr.pick_eat.restaurant.dto.RestaurantDTO;
import fr.pick_eat.restaurant.entity.RestaurantModel;
import fr.pick_eat.restaurant.service.RestaurantService;
import fr.pick_eat.restaurant.utils.JWTUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Parameter;


import java.util.List;
import java.util.UUID;

@RestController
public class RestaurantController {
    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @PostMapping("/{restaurantId}/comment")
    public ResponseEntity<String> commentRestaurant(@Parameter(hidden=true) @CookieValue("jwt") String jwt, @PathVariable UUID restaurantId, String comment) {
        String userId = JWTUtils.extractUserId(jwt);
        UUID userUuid = UUID.fromString(userId);

        try {
            restaurantService.commentRestaurant(restaurantId, comment, userUuid);
            return ResponseEntity.ok("Comment added");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping("/{restaurantId}/note")
    public ResponseEntity<String> noteRestaurant(@Parameter(hidden=true) @CookieValue("jwt") String jwt, @PathVariable UUID restaurantId, Integer note) {
        String userId = JWTUtils.extractUserId(jwt);
        UUID userUuid = UUID.fromString(userId);

        try {
            restaurantService.noteRestaurant(restaurantId, note, userUuid);
            return ResponseEntity.ok("Note added");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/{restaurantId}")
    public ResponseEntity<String> getRestaurant(@PathVariable UUID restaurantId) {
        try {
            return ResponseEntity.ok(restaurantService.getRestaurant(restaurantId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/restaurant/add")
    public ResponseEntity<String> addRestaurant(RestaurantModel restaurant) {
        try {
            restaurantService.saveRestaurant(restaurant);
            return ResponseEntity.ok("Restaurant added : " + restaurant.getId());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/restaurant/all")
    public ResponseEntity<String> getAllRestaurants() {
        try {
            Iterable<RestaurantModel> listRestaurant = restaurantService.getAllRestaurants();
            return ResponseEntity.ok(listRestaurant.toString());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/generate-restaurants-for-event")
    public ResponseEntity<String> generateRestaurantsForEvent(EventDTO event) {
        try {
            List<RestaurantDTO> listRestaurant = restaurantService.generateRestaurantsForEvent(event);
            return ResponseEntity.ok(listRestaurant.toString());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
