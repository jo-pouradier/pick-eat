package fr.pick_eat.restaurant.controller;

import fr.pick_eat.restaurant.entity.RestaurantModel;
import fr.pick_eat.restaurant.service.RestaurantService;
import org.apache.tomcat.util.json.ParseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.test.web.servlet.MockMvc;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RestaurantControllerTest {


    private RestaurantService restaurantService;

    @Test
    public void testGetAllRestaurants() throws FileNotFoundException, JSONException, ParseException {
        // Arrange
        String resto_path = "get_restaurants_data/data/all_restaurants_lyon.json";
        String resto_details_path = "get_restaurants_data/data/restaurant_details.json";


        // Act
        restaurantService.parseRestaurants(resto_path, resto_details_path);
        Iterable<RestaurantModel> restos = restaurantService.getAllRestaurants();

        // Assert
        assertNotNull(restos);
    }

}
