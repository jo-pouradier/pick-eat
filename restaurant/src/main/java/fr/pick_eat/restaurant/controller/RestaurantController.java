package fr.pick_eat.restaurant.controller;

import fr.pick_eat.event.dto.EventDTO;
import fr.pick_eat.restaurant.dto.RestaurantAvisDTO;
import fr.pick_eat.restaurant.dto.RestaurantDTO;
import fr.pick_eat.restaurant.entity.RestaurantModel;
import fr.pick_eat.restaurant.mapper.RestaurantMapper;
import fr.pick_eat.restaurant.service.RestaurantService;
import fr.pick_eat.restaurant.utils.JWTUtils;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
public class RestaurantController {
    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @PostMapping("/{restaurantId}/comment")
    public ResponseEntity<String> commentRestaurant(@Parameter(hidden = true) @CookieValue("jwt") String jwt, @PathVariable UUID restaurantId, String comment) {
        String userId = JWTUtils.extractUserId(jwt);
        UUID userUuid = UUID.fromString(userId);

        try {
            restaurantService.commentRestaurant(restaurantId, comment, userUuid);
            return ResponseEntity.ok("Comment added");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping("/{restaurantId}/comments")
    public ResponseEntity<String> getComments(@PathVariable UUID restaurantId) {
        try {
            List<RestaurantAvisDTO> listAvis = restaurantService.getComments(restaurantId);
            return ResponseEntity.ok(listAvis.toString());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/{restaurantId}/note")
    public ResponseEntity<String> noteRestaurant(@Parameter(hidden = true) @CookieValue("jwt") String jwt, @PathVariable UUID restaurantId, Integer note) {
        String userId = JWTUtils.extractUserId(jwt);
        UUID userUuid = UUID.fromString(userId);

        try {
            restaurantService.noteRestaurant(restaurantId, note, userUuid);
            return ResponseEntity.ok("Note added");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping("/{restaurantId}")
    public ResponseEntity<RestaurantDTO> getRestaurant(@PathVariable UUID restaurantId) {
        try {
            return ResponseEntity.ok(RestaurantMapper.toRestaurantDTO(restaurantService.getRestaurant(restaurantId)));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found");
        }
    }

    @PostMapping("/restaurant/add")
    public ResponseEntity<String> addRestaurant(@RequestBody RestaurantDTO restaurantDTO) {
        try {
            restaurantService.saveRestaurant(RestaurantMapper.toRestaurantModel(restaurantDTO));
            return ResponseEntity.ok("Restaurant added : " + restaurantDTO.getId());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/restaurant/all")
    public ResponseEntity<List<RestaurantDTO>> getAllRestaurants() {
        System.out.println("Get all restaurants");
        try {
            List<RestaurantModel> listRestaurant = (List<RestaurantModel>) restaurantService.getAllRestaurants();
            return ResponseEntity.ok(listRestaurant.stream().map(RestaurantMapper::toRestaurantDTO).toList());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No restaurant found");
        }
    }

    @PostMapping("/generate-restaurants-for-event")
    public ResponseEntity<List<RestaurantDTO>> generateRestaurantsForEvent(@RequestBody EventDTO event) {
        try {
            List<RestaurantDTO> listRestaurant = restaurantService.generateRestaurantsForEvent(event);
            return ResponseEntity.ok(listRestaurant);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No restaurant found in the area");
        }
    }

}
